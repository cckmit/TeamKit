package org.team4u.ddd.process.retry.method.interceptor;

import com.alibaba.fastjson.JSON;
import org.team4u.ddd.domain.model.ThreadPoolEventSubscriber;
import org.team4u.ddd.infrastructure.util.MethodInvoker;
import org.team4u.ddd.process.TimeConstrainedProcessTracker;
import org.team4u.ddd.process.TimeConstrainedProcessTrackerAppService;
import org.team4u.ddd.process.retry.RetryService;

/**
 * 可重试方法超时事件抽象订阅者
 * <p>
 * 该类将自动处理跟踪器
 *
 * @author jay.wu
 */
public abstract class AbstractRetryableMethodTimedOutEventSubscriber extends ThreadPoolEventSubscriber<RetryableMethodTimedOutEvent> {

    private final MethodInvoker methodInvoker;
    private final TimeConstrainedProcessTrackerAppService trackerAppService;

    public AbstractRetryableMethodTimedOutEventSubscriber(TimeConstrainedProcessTrackerAppService trackerAppService) {
        this.trackerAppService = trackerAppService;
        this.methodInvoker = new MethodInvoker();
    }

    @Override
    protected void handleEvent(RetryableMethodTimedOutEvent event) throws Throwable {
        RetryContextReader contextReader = new RetryContextReader(JSON.parseObject(event.getDescription()));

        TimeConstrainedProcessTracker tracker = trackerAppService.trackerOfProcessId(
                event.getDomainId(),
                typeOfEventSubscribed()
        );

        retry(tracker, event, contextReader);
    }

    /**
     * 获取待重试的实例
     *
     * @param contextReader 重试上下文阅读器
     * @return 待重试的实例
     */
    protected abstract Object retryInvoker(RetryContextReader contextReader);

    private void retry(TimeConstrainedProcessTracker tracker,
                       RetryableMethodTimedOutEvent event,
                       RetryContextReader contextReader) throws Exception {
        retryService(contextReader).invokeAndAutoCloseTracker(
                tracker,
                contextReader.shouldRemoveTrackerAfterCompleted(),
                () -> {
                    RetryableMethodTimedOutEvent.setCurrentEvent(event);

                    try {
                        return methodInvoker.invoke(
                                retryInvoker(contextReader),
                                contextReader.methodName(),
                                contextReader.parameterTypes(),
                                contextReader.parameterValues()
                        );
                    } finally {
                        RetryableMethodTimedOutEvent.removeCurrentEvent();
                    }
                }
        );
    }

    private RetryService retryService(RetryContextReader contextReader) {
        return new RetryService(
                contextReader.retryForClass(),
                contextReader.noRetryForClass(),
                trackerAppService
        );
    }

    @Override
    protected Class<RetryableMethodTimedOutEvent> typeOfEventSubscribed() {
        return RetryableMethodTimedOutEvent.class;
    }
}