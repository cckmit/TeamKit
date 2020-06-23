package org.team4u.ddd.process.retry.process;

import cn.hutool.core.util.ClassUtil;
import org.team4u.ddd.domain.model.DomainEvent;
import org.team4u.ddd.infrastructure.serializer.FastJsonSerializer;
import org.team4u.ddd.process.TimeConstrainedProcessTrackerAppService;

/**
 * 可重试超时事件订阅者
 *
 * @author jay.wu
 */
public abstract class AbstractRetryableProcessTimedOutEventSubscriber<
        P extends DomainEvent,
        R extends AbstractRetryableProcessTimedOutEvent>
        extends AbstractProcessTimedOutEventSubscriber<R> {

    public AbstractRetryableProcessTimedOutEventSubscriber(TimeConstrainedProcessTrackerAppService trackerAppService) {
        super(trackerAppService);
    }

    @Override
    protected void handleTimedOutEvent(R timedOutEvent) {
        P processEvent = FastJsonSerializer.instance().deserialize(
                timedOutEvent.getDescription(),
                processEventClass()
        );

        handleRetryProcessEvent(timedOutEvent, processEvent);
    }

    /**
     * 处理事件
     *
     * @param timedOutEvent 超时事件
     * @param processEvent  原始领域事件
     */
    protected abstract void handleRetryProcessEvent(R timedOutEvent, P processEvent);

    @SuppressWarnings("unchecked")
    @Override
    protected Class<R> typeOfEventSubscribed() {
        return (Class<R>) ClassUtil.getTypeArgument(this.getClass(), 1);
    }

    @SuppressWarnings("unchecked")
    protected Class<P> processEventClass() {
        return (Class<P>) ClassUtil.getTypeArgument(this.getClass());
    }
}