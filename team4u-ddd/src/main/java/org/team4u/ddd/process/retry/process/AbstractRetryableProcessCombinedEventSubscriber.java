package org.team4u.ddd.process.retry.process;

import cn.hutool.core.thread.ExecutorBuilder;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.TypeUtil;
import org.team4u.base.error.BusinessException;
import org.team4u.base.message.jvm.AbstractMessageSubscriber;
import org.team4u.ddd.domain.model.DomainEvent;
import org.team4u.ddd.process.TimeConstrainedProcessTrackerAppService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Stream;

/**
 * 可重试组合事件订阅者
 * <p>
 * 该类同时监听次运行的领域事件及重试超时事件
 *
 * @author jay.wu
 */
public abstract class AbstractRetryableProcessCombinedEventSubscriber<
        P extends DomainEvent,
        R extends AbstractRetryableProcessTimedOutEvent
        > extends AbstractMessageSubscriber<DomainEvent> {

    private final RetryableProcessEventSubscriber retryableProcessEventSubscriber;
    private final RetryableProcessTimedOutEventSubscriber retryableProcessTimedOutEventSubscriber;

    public AbstractRetryableProcessCombinedEventSubscriber(TimeConstrainedProcessTrackerAppService trackerAppService) {
        super(null);

        retryableProcessEventSubscriber = new RetryableProcessEventSubscriber(trackerAppService);
        retryableProcessTimedOutEventSubscriber = new RetryableProcessTimedOutEventSubscriber(trackerAppService);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void internalOnMessage(DomainEvent event) {
        if (!supports(event)) {
            return;
        }

        if (event instanceof AbstractRetryableProcessTimedOutEvent) {
            retryableProcessTimedOutEventSubscriber.onMessage((R) event);
        } else {
            retryableProcessEventSubscriber.onMessage((P) event);
        }
    }

    @Override
    public boolean supports(Object event) {
        return Stream.of(TypeUtil.getTypeArguments(this.getClass()))
                .anyMatch(it -> ((Class<?>) it).isAssignableFrom(event.getClass()));
    }

    /**
     * 处理正常的领域事件
     *
     * @param event 领域事件
     */
    protected abstract void handleProcessEvent(P event);

    /**
     * 处理重试的领域事件
     *
     * @param timedOutEvent 超时事件
     * @param processEvent  原始领域事件
     */
    protected abstract void handleRetryProcessEvent(R timedOutEvent, P processEvent);

    /**
     * 获取重试策略标识
     *
     * @param event 领域事件
     * @return 重试策略标识
     */
    protected abstract String retryStrategyId(P event);

    /**
     * 定义线程池
     *
     * @return 线程池
     */
    protected ExecutorService executorService() {
        return ExecutorBuilder
                .create()
                .setCorePoolSize(3)
                .setWorkQueue(new LinkedBlockingQueue<>(2000))
                .setHandler(new ThreadPoolExecutor.DiscardPolicy())
                .build();
    }

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

    /**
     * 任务完成后是否移除跟踪器
     *
     * @return true则移除，false则保留
     */
    protected abstract boolean shouldRemoveTrackerAfterCompleted();

    @SuppressWarnings("unchecked")
    protected Class<P> processEventClass() {
        return (Class<P>) ClassUtil.getTypeArgument(getClass(), 0);
    }

    @SuppressWarnings("unchecked")
    protected Class<R> timedOutEventClass() {
        return (Class<R>) ClassUtil.getTypeArgument(getClass(), 1);
    }

    private class RetryableProcessEventSubscriber extends AbstractRetryableProcessEventSubscriber<P, R> {

        public RetryableProcessEventSubscriber(TimeConstrainedProcessTrackerAppService trackerAppService) {
            super(AbstractRetryableProcessCombinedEventSubscriber.this.executorService(), trackerAppService);
        }

        @Override
        protected void handleProcessEvent(P event) {
            AbstractRetryableProcessCombinedEventSubscriber.this.handleProcessEvent(event);
        }

        @Override
        protected String retryStrategyId(P event) {
            return AbstractRetryableProcessCombinedEventSubscriber.this.retryStrategyId(event);
        }

        @Override
        protected Class<? extends Throwable>[] retryForClass() {
            return AbstractRetryableProcessCombinedEventSubscriber.this.retryForClass();
        }

        @Override
        protected Class<? extends Throwable>[] noRetryForClass() {
            return AbstractRetryableProcessCombinedEventSubscriber.this.noRetryForClass();
        }

        @Override
        public Class<P> messageType() {
            return AbstractRetryableProcessCombinedEventSubscriber.this.processEventClass();
        }

        @Override
        protected Class<R> timedOutEventClass() {
            return AbstractRetryableProcessCombinedEventSubscriber.this.timedOutEventClass();
        }
    }

    private class RetryableProcessTimedOutEventSubscriber extends AbstractRetryableProcessTimedOutEventSubscriber<P, R> {

        public RetryableProcessTimedOutEventSubscriber(TimeConstrainedProcessTrackerAppService trackerAppService) {
            super(AbstractRetryableProcessCombinedEventSubscriber.this.executorService(), trackerAppService);
        }

        @Override
        protected boolean shouldRemoveTrackerAfterCompleted() {
            return AbstractRetryableProcessCombinedEventSubscriber.this.shouldRemoveTrackerAfterCompleted();
        }

        @Override
        protected void handleRetryProcessEvent(R timedOutEvent, P processEvent) {
            AbstractRetryableProcessCombinedEventSubscriber.this.handleRetryProcessEvent(timedOutEvent, processEvent);
        }

        @Override
        protected Class<? extends Throwable>[] retryForClass() {
            return AbstractRetryableProcessCombinedEventSubscriber.this.retryForClass();
        }

        @Override
        protected Class<? extends Throwable>[] noRetryForClass() {
            return AbstractRetryableProcessCombinedEventSubscriber.this.noRetryForClass();
        }

        @Override
        public Class<R> messageType() {
            return AbstractRetryableProcessCombinedEventSubscriber.this.timedOutEventClass();
        }

        @Override
        public Class<P> processEventClass() {
            return AbstractRetryableProcessCombinedEventSubscriber.this.processEventClass();
        }
    }
}