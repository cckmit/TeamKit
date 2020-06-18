package org.team4u.ddd.process.retry.process;

import cn.hutool.core.util.StrUtil;
import org.team4u.ddd.domain.model.DomainEvent;
import org.team4u.ddd.process.TimeConstrainedProcessTrackerAppService;

/**
 * 简单可重试组合事件订阅者
 * <p>
 * 该类同时监听次运行的领域事件及重试超时事件，并同一处理超时与普通事件
 *
 * @author jay.wu
 */
public abstract class AbstractSimpleRetryableProcessCombinedEventSubscriber<
        P extends DomainEvent,
        R extends AbstractRetryableProcessTimedOutEvent
        > extends AbstractRetryableProcessCombinedEventSubscriber<P, R> {


    public AbstractSimpleRetryableProcessCombinedEventSubscriber(TimeConstrainedProcessTrackerAppService trackerAppService) {
        super(trackerAppService);
    }

    /**
     * 忽略timedOutEvent，直接调用handleProcessEvent
     *
     * @param timedOutEvent 超时事件
     * @param processEvent  原始领域事件
     */
    @Override
    protected void handleRetryProcessEvent(R timedOutEvent, P processEvent) {
        handleProcessEvent(processEvent);
    }

    /**
     * 基于超时事件名称的策略标识
     *
     * @param event 领域事件
     * @return 策略标识
     */
    @Override
    protected String retryStrategyId(P event) {
        return StrUtil.lowerFirst(timedOutEventClass().getSimpleName());
    }

    @Override
    protected boolean shouldRemoveTrackerAfterCompleted() {
        return false;
    }
}