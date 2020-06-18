package org.team4u.ddd.process.retry.process;

import org.team4u.ddd.domain.model.ThreadPoolEventSubscriber;
import org.team4u.ddd.process.ProcessTimedOutEvent;
import org.team4u.ddd.process.TimeConstrainedProcessTracker;
import org.team4u.ddd.process.TimeConstrainedProcessTrackerAppService;
import org.team4u.ddd.process.retry.RetryService;
import org.team4u.core.error.BusinessException;

/**
 * 超时事件订阅者
 *
 * @author jay.wu
 */
public abstract class AbstractProcessTimedOutEventSubscriber<E extends ProcessTimedOutEvent>
        extends ThreadPoolEventSubscriber<E> {

    private final RetryService retryService;
    private final TimeConstrainedProcessTrackerAppService trackerAppService;

    public AbstractProcessTimedOutEventSubscriber(TimeConstrainedProcessTrackerAppService trackerAppService) {
        this.trackerAppService = trackerAppService;

        this.retryService = new RetryService(
                retryForClass(),
                noRetryForClass(),
                trackerAppService
        );
    }

    @Override
    protected void handleEvent(E event) throws Exception {
        TimeConstrainedProcessTracker tracker = trackerAppService.trackerOfProcessId(
                event.getDomainId(),
                typeOfEventSubscribed()
        );

        retryService.invokeAndAutoCloseTracker(tracker, shouldRemoveTrackerAfterCompleted(), () -> {
            handleTimedOutEvent(event);
            return null;
        });
    }

    /**
     * 任务完成后是否移除跟踪器
     *
     * @return true则移除，false则保留
     */
    protected abstract boolean shouldRemoveTrackerAfterCompleted();

    /**
     * 处理事件
     *
     * @param timedOutEvent 超时事件
     */
    protected abstract void handleTimedOutEvent(E timedOutEvent);

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
}