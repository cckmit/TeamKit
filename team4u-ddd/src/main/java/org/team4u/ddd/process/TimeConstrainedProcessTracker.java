package org.team4u.ddd.process;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import org.team4u.base.error.NestedException;
import org.team4u.ddd.domain.model.ConcurrencySafeAggregateRoot;
import org.team4u.ddd.process.strategy.RetryStrategy;

import java.util.Date;

/**
 * 超时跟踪器
 *
 * @author jay.wu
 */
public class TimeConstrainedProcessTracker extends ConcurrencySafeAggregateRoot {

    /**
     * 跟踪器标识
     */
    private String trackerId;
    /**
     * 需要跟踪的处理标识
     */
    private String processId;
    /**
     * 是否已完成
     */
    private boolean completed;
    /**
     * 描述
     */
    private String description;

    /**
     * 是否已完成所有通知
     */
    private boolean processInformedOfTimeout;
    /**
     * 超时时间类型
     */
    private String processTimedOutEventType;
    /**
     * 超时时间
     */
    private Date timeoutOccursOn;
    /**
     * 重试策略
     */
    private RetryStrategy retryStrategy;
    /**
     * 已重试次数
     */
    private int retryCount;

    public TimeConstrainedProcessTracker(
            String trackerId,
            String processId,
            String description,
            Date processCreateTime,
            RetryStrategy retryStrategy,
            String processTimedOutEventType) {
        super();

        setTrackerId(trackerId);
        setRetryStrategy(retryStrategy);
        setDescription(description);
        setProcessId(processId);
        setProcessTimedOutEventType(processTimedOutEventType);
        setTimeoutOccursOn(DateUtil.offsetSecond(processCreateTime, retryIntervalSec()));
    }

    protected TimeConstrainedProcessTracker() {
        super();
    }

    public RetryStrategy retryStrategy() {
        return retryStrategy;
    }

    public int retryIntervalSec() {
        return retryStrategy.nextIntervalSec(retryCount());
    }

    public void completed() {
        setCompleted(true);
    }

    public boolean isCompleted() {
        return completed;
    }

    public String description() {
        return description;
    }

    public String processId() {
        return processId;
    }

    public boolean isProcessInformedOfTimeout() {
        return processInformedOfTimeout;
    }

    private void setProcessInformedOfTimeout(boolean isProcessInformedOfTimeout) {
        processInformedOfTimeout = isProcessInformedOfTimeout;
    }

    public String processTimedOutEventType() {
        return processTimedOutEventType;
    }

    public boolean hasTimedOut() {
        Date now = new Date();
        return (timeoutOccursOn().equals(now) || timeoutOccursOn().before(now));
    }

    public void informProcessTimedOut() {
        if (!isProcessInformedOfTimeout() && hasTimedOut()) {
            ProcessTimedOutEvent processTimedOutEvent;

            if (maxRetriesPermitted() == 0 || retryIntervalSec() < 0) {
                processTimedOutEvent = processTimedOutEvent();
                setProcessInformedOfTimeout(true);
            } else {
                incrementRetryCount();
                processTimedOutEvent = processTimedOutEventWithRetries();

                if (totalRetriesReached()) {
                    setProcessInformedOfTimeout(true);
                } else {
                    setTimeoutOccursOn(
                            DateUtil.offsetSecond(timeoutOccursOn(), retryIntervalSec())
                    );
                }
            }

            publishEvent(processTimedOutEvent);
        }
    }

    public int retryCount() {
        return retryCount;
    }

    public String trackerId() {
        return trackerId;
    }

    public Date timeoutOccursOn() {
        return timeoutOccursOn;
    }

    public int maxRetriesPermitted() {
        return retryStrategy.maxRetriesPermitted();
    }

    @Override
    public boolean equals(Object anObject) {
        boolean equalObjects = false;

        if (anObject != null && getClass() == anObject.getClass()) {
            TimeConstrainedProcessTracker typedObject = (TimeConstrainedProcessTracker) anObject;
            equalObjects =
                    trackerId().equals(typedObject.trackerId()) &&
                            processId().equals(typedObject.processId());
        }

        return equalObjects;
    }

    private void incrementRetryCount() {
        setRetryCount(retryCount + 1);
    }

    public TimeConstrainedProcessTracker setRetryStrategy(RetryStrategy retryStrategy) {
        this.retryStrategy = retryStrategy;
        return this;
    }

    private void setDescription(String aDescription) {
        this.description = aDescription;
    }

    private ProcessTimedOutEvent processTimedOutEvent() {
        try {
            return ReflectUtil.newInstance(
                    ClassUtil.loadClass(processTimedOutEventType()),
                    processId(),
                    description()
            );
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Error creating new ProcessTimedOut instance because: "
                            + e.getMessage());
        }
    }

    private ProcessTimedOutEvent processTimedOutEventWithRetries() {
        try {
            return ReflectUtil.newInstance(
                    ClassUtil.loadClass(processTimedOutEventType()),
                    processId(),
                    maxRetriesPermitted(),
                    retryCount(),
                    description()
            );
        } catch (Exception e) {
            throw new NestedException(
                    "Error creating new ProcessTimedOut instance error",
                    e
            );
        }
    }

    private void setProcessTimedOutEventType(String processTimedOutEventType) {
        Assert.notEmpty(processTimedOutEventType, "ProcessTimedOutEventType is required.");

        this.processTimedOutEventType = processTimedOutEventType;
    }

    private void setProcessId(String processId) {
        Assert.notNull(processId, "ProcessId is required.");

        this.processId = processId;
    }

    private void setTrackerId(String trackerId) {
        Assert.notNull(trackerId, "TrackerId is required.");

        this.trackerId = trackerId;
    }

    private void setTimeoutOccursOn(Date timeoutOccursOn) {
        Assert.notNull(timeoutOccursOn, "TimeoutOccursOn is required.");

        this.timeoutOccursOn = timeoutOccursOn;
    }

    private boolean totalRetriesReached() {
        return retryCount() >= maxRetriesPermitted();
    }

    private void setCompleted(boolean completed) {
        this.completed = completed;
    }

    private void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }
}
