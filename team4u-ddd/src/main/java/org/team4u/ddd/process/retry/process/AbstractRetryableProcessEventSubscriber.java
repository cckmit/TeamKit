package org.team4u.ddd.process.retry.process;

import cn.hutool.core.util.ClassUtil;
import org.team4u.base.error.BusinessException;
import org.team4u.base.serializer.FastJsonSerializer;
import org.team4u.ddd.domain.model.DomainEvent;
import org.team4u.ddd.message.AbstractMessageConsumer;
import org.team4u.ddd.process.TimeConstrainedProcessTracker;
import org.team4u.ddd.process.TimeConstrainedProcessTrackerAppService;
import org.team4u.ddd.process.retry.RetryService;

import java.util.concurrent.ExecutorService;

/**
 * 可重试的本地线程池事件订阅者
 *
 * @author jay.wu
 */
public abstract class AbstractRetryableProcessEventSubscriber<E extends DomainEvent, R extends AbstractRetryableProcessTimedOutEvent>
        extends AbstractMessageConsumer<E> {

    private final RetryService retryService;
    private final TimeConstrainedProcessTrackerAppService trackerAppService;


    public AbstractRetryableProcessEventSubscriber(ExecutorService executorService,
                                                   TimeConstrainedProcessTrackerAppService trackerAppService) {
        super(executorService);
        this.trackerAppService = trackerAppService;

        this.retryService = new RetryService(
                retryForClass(),
                noRetryForClass(),
                trackerAppService
        );
    }

    public AbstractRetryableProcessEventSubscriber(TimeConstrainedProcessTrackerAppService trackerAppService) {
        this(null, trackerAppService);
    }

    @Override
    public void processMessage(E event) {
        if (supports(event)) {
            createAndSaveTracker(event);
        }

        super.processMessage(event);
    }

    @Override
    protected void internalProcessMessage(E event) throws Exception {
        TimeConstrainedProcessTracker tracker = trackerAppService.trackerOfProcessId(
                event.getDomainId(),
                timedOutEventClass()
        );

        retryService.invokeForFirstRun(tracker, false, () -> {
            handleProcessEvent(event);
            return null;
        });
    }

    /**
     * 处理事件
     */
    protected abstract void handleProcessEvent(E event) throws Exception;

    /**
     * 重试策略标识
     */
    protected abstract String retryStrategyId(E event);

    /**
     * 定义需要重试的异常类数组
     * <p>
     * 若不配置，默认所有异常均可重试
     *
     * @return 需重试的异常类数组
     */
    protected Class<? extends Throwable>[] retryForClass() {
        return null;
    }

    /**
     * 定义无需重试的异常类数组
     * <p>
     * 若不配置，默认所有异常均可重试
     *
     * @return 无需重试的异常类数组
     */
    @SuppressWarnings("unchecked")
    protected Class<? extends Throwable>[] noRetryForClass() {
        return new Class[]{BusinessException.class};
    }

    @SuppressWarnings("unchecked")
    protected Class<R> timedOutEventClass() {
        return (Class<R>) ClassUtil.getTypeArgument(this.getClass(), 1);
    }

    private void createAndSaveTracker(E event) {
        trackerAppService.createAndSaveTracker(
                event.getDomainId(),
                event.getOccurredOn(),
                retryStrategyId(event),
                timedOutEventClass(),
                FastJsonSerializer.instance().serialize(event)
        );
    }
}