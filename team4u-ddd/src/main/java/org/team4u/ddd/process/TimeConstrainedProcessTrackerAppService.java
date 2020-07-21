package org.team4u.ddd.process;

import cn.hutool.core.util.IdUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.team4u.ddd.process.strategy.RetryStrategyRepository;
import org.team4u.base.error.OptimisticLockingFailureException;
import org.team4u.base.log.LogMessage;
import org.team4u.base.log.LogMessages;

import java.util.Collection;
import java.util.Date;

/**
 * 超时跟踪器应用服务
 *
 * @author jay.wu
 */
public class TimeConstrainedProcessTrackerAppService {

    private final Log log = LogFactory.get();

    private final TimeConstrainedProcessTrackerRepository trackerRepository;
    private final RetryStrategyRepository retryStrategyRepository;

    public TimeConstrainedProcessTrackerAppService(TimeConstrainedProcessTrackerRepository trackerRepository,
                                                   RetryStrategyRepository retryStrategyRepository) {
        this.trackerRepository = trackerRepository;
        this.retryStrategyRepository = retryStrategyRepository;
    }

    /**
     * 创建并保存跟踪器
     *
     * @param processId         关联的处理标识
     * @param processCreateTime 处理开始时间
     * @param retryStrategyId   重试策略标识
     * @param eventClass        超时后发布事件类型
     * @return 跟踪器
     */
    public TimeConstrainedProcessTracker createAndSaveTracker(String processId,
                                                              Date processCreateTime,
                                                              String retryStrategyId,
                                                              Class<? extends ProcessTimedOutEvent> eventClass) {
        return createAndSaveTracker(processId,
                processCreateTime,
                retryStrategyId,
                eventClass,
                null);
    }

    /**
     * 创建并保存跟踪器
     *
     * @param processId         关联的处理标识
     * @param processCreateTime 处理开始时间
     * @param retryStrategyId   重试策略标识
     * @param eventClass        超时后发布事件类型
     * @param description       运行时上下文
     * @return 跟踪器
     */
    public TimeConstrainedProcessTracker createAndSaveTracker(String processId,
                                                              Date processCreateTime,
                                                              String retryStrategyId,
                                                              Class<? extends ProcessTimedOutEvent> eventClass,
                                                              String description) {
        TimeConstrainedProcessTracker tracker = createTracker(
                processId,
                processCreateTime,
                retryStrategyId,
                eventClass,
                description
        );

        saveTracker(tracker);

        return tracker;
    }

    /**
     * 创建跟踪器
     *
     * @param processId         关联的处理标识
     * @param processCreateTime 处理开始时间
     * @param retryStrategyId   重试策略标识
     * @param eventClass        超时后发布事件类型
     * @param description       运行时上下文
     * @return 跟踪器
     */
    public TimeConstrainedProcessTracker createTracker(String processId,
                                                       Date processCreateTime,
                                                       String retryStrategyId,
                                                       Class<? extends ProcessTimedOutEvent> eventClass,
                                                       String description) {
        return new TimeConstrainedProcessTracker(
                IdUtil.simpleUUID(),
                processId,
                description,
                processCreateTime,
                retryStrategyRepository.strategyOf(retryStrategyId),
                eventClass.getName());
    }

    /**
     * 保存跟踪器
     *
     * @param tracker 跟踪器
     */
    public void saveTracker(TimeConstrainedProcessTracker tracker) {
        if (tracker == null) {
            return;
        }

        trackerRepository.save(tracker);

        LogMessage lm = LogMessages.createWithMasker(this.getClass().getSimpleName(), "createAndSaveTracker")
                .append("trackerId", tracker.trackerId())
                .append("processId", tracker.processId())
                .append("timeoutOccursOn", tracker.timeoutOccursOn())
                .append("retryStrategyId", tracker.retryStrategy().strategyId())
                .append("totalRetriesPermitted", tracker.maxRetriesPermitted())
                .append("processTimedOutEventType", tracker.processTimedOutEventType());
        log.info(lm.success().toString());
    }

    /**
     * 关闭跟踪器
     *
     * @param processId                         关联的处理标识
     * @param eventClass                        超时后发布事件类型
     * @param shouldRemoveTrackerAfterCompleted 任务完成后是否移除跟踪器
     */
    public void closeTracker(String processId,
                             Class<? extends ProcessTimedOutEvent> eventClass,
                             boolean shouldRemoveTrackerAfterCompleted) {
        TimeConstrainedProcessTracker tracker = trackerRepository.trackerOfProcessId(processId, eventClass.getName());
        closeTracker(tracker, shouldRemoveTrackerAfterCompleted);
    }

    /**
     * 关闭跟踪器
     *
     * @param tracker                           跟踪器
     * @param shouldRemoveTrackerAfterCompleted 任务完成后是否移除跟踪器
     */
    public void closeTracker(TimeConstrainedProcessTracker tracker, boolean shouldRemoveTrackerAfterCompleted) {
        if (tracker == null) {
            return;
        }

        LogMessage lm = LogMessages.createWithMasker(this.getClass().getSimpleName(), "closeTracker")
                .append("processId", tracker.processId())
                .append("shouldRemoveTrackerAfterCompleted", shouldRemoveTrackerAfterCompleted)
                .append("processTimedOutEventType", tracker.processTimedOutEventType());
        try {
            completeTracker(tracker);

            if (shouldRemoveTrackerAfterCompleted) {
                removeTracker(tracker);
            }

            log.info(lm.success().toString());
        } catch (OptimisticLockingFailureException e) {
            log.warn(lm.fail("Optimistic locking failure").toString());
        } catch (Exception e) {
            log.error(lm.fail().toString(), e);
        }
    }

    /**
     * 完成跟踪器
     *
     * @param tracker 跟踪器
     */
    public void completeTracker(TimeConstrainedProcessTracker tracker) {
        if (tracker == null) {
            return;
        }

        tracker.completed();
        trackerRepository.save(tracker);
    }

    /**
     * 处理完成跟踪器
     */
    public void completeTracker(String processId, Class<? extends ProcessTimedOutEvent> eventClass) {
        TimeConstrainedProcessTracker tracker = trackerRepository.trackerOfProcessId(processId, eventClass.getName());
        completeTracker(tracker);
    }

    /**
     * 删除跟踪器
     */
    public void removeTracker(String processId, Class<? extends ProcessTimedOutEvent> eventClass) {
        TimeConstrainedProcessTracker tracker = trackerRepository.trackerOfProcessId(processId, eventClass.getName());
        removeTracker(tracker);
    }

    /**
     * 删除跟踪器
     */
    public void removeTracker(TimeConstrainedProcessTracker tracker) {
        if (tracker == null) {
            return;
        }

        trackerRepository.remove(tracker);
    }

    /**
     * 检查所有处理超时的事物，并发布超时事件
     */
    public void informAllProcessTimedOut(int sizeLimit) {
        informProcessTimedOut(trackerRepository.allTimedOut(sizeLimit));
    }

    /**
     * 检查指定处理超时的事物，并发布超时事件
     */
    public void informProcessTimedOut(Class<? extends ProcessTimedOutEvent> eventClass, int sizeLimit) {
        informProcessTimedOut(trackerRepository.allTimedOutOf(eventClass.getName(), sizeLimit));
    }

    /**
     * 获取跟踪器
     *
     * @param processId  关联的处理标识
     * @param eventClass 超时后发布事件类型
     * @return 跟踪器
     */
    public TimeConstrainedProcessTracker trackerOfProcessId(String processId, Class<? extends ProcessTimedOutEvent> eventClass) {
        return repository().trackerOfProcessId(processId, eventClass.getName());
    }

    private void informProcessTimedOut(Collection<TimeConstrainedProcessTracker> trackers) {
        for (TimeConstrainedProcessTracker tracker : trackers) {
            try {
                tracker.informProcessTimedOut();
                saveTracker(tracker);
            } catch (Exception e) {
                log.error(e, LogMessage.create(this.getClass().getSimpleName(), "informProcessTimedOut")
                        .fail(e.getMessage())
                        .append("trackerId", tracker.getId())
                        .append("processId", tracker.processId())
                        .append("processTimedOutEventType", tracker.processTimedOutEventType())
                        .toString());
            }
        }
    }

    public TimeConstrainedProcessTrackerRepository repository() {
        return trackerRepository;
    }
}