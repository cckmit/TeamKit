package org.team4u.ddd.process.retry.method.interceptor;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.alibaba.fastjson.JSON;
import org.team4u.ddd.process.TimeConstrainedProcessTracker;
import org.team4u.ddd.process.TimeConstrainedProcessTrackerAppService;
import org.team4u.ddd.process.retry.RetryService;
import org.team4u.ddd.process.retry.method.annotation.RetryableAnnotationParser;
import org.team4u.ddd.process.retry.method.annotation.RetryableAttribute;
import org.team4u.base.error.SystemException;
import org.team4u.base.log.LogMessages;

import java.util.Date;

/**
 * 重试拦截器
 * <p>
 * 该类将自动创建/处理超时跟踪器
 *
 * @author jay.wu
 */
public class RetryInterceptor {

    private final Log log = LogFactory.get();

    private final RetryableAnnotationParser retryableAnnotationParser;
    private final TimeConstrainedProcessTrackerAppService trackerAppService;

    public RetryInterceptor(TimeConstrainedProcessTrackerAppService trackerAppService) {
        this.trackerAppService = trackerAppService;
        this.retryableAnnotationParser = new RetryableAnnotationParser();
    }

    public Object invoke(RetryInterceptorContext context) throws Throwable {
        RetryableAttribute attribute = retryableAnnotationParser.parse(context.getMethod());
        // 无注解或者重试调用，不进行处理
        if (attribute == null || isRetry()) {
            return context.getInvoker().invoke();
        }

        RetryContext retryContext = createRetryContext(context, attribute);
        if (retryContext == null) {
            return null;
        }

        return createRetryService(attribute).invokeForFirstRun(
                createTracker(retryContext, attribute),
                true,
                () -> {
                    try {
                        return context.getInvoker().invoke();
                    } catch (Exception e) {
                        throw e;
                    } catch (Throwable throwable) {
                        throw new SystemException(throwable);
                    }
                }
        );
    }

    private boolean isRetry() {
        return RetryableMethodTimedOutEvent.hasCurrentEvent();
    }

    private RetryService createRetryService(RetryableAttribute attribute) {
        return new RetryService(
                attribute.getRetryForClass(),
                attribute.getNoRetryForClass(),
                trackerAppService
        );
    }

    private TimeConstrainedProcessTracker createTracker(RetryContext context,
                                                        RetryableAttribute attribute) {
        String serializedContext = serializeContext(context);
        String processId = SecureUtil.md5(serializedContext);
        return trackerAppService.createTracker(
                processId,
                new Date(),
                attribute.getRetryStrategyId(),
                RetryableMethodTimedOutEvent.class,
                serializedContext
        );
    }

    private RetryContext createRetryContext(RetryInterceptorContext context,
                                            RetryableAttribute attribute) {
        String invokerId = context.getInvoker().id();

        if (invokerId == null) {
            log.error(LogMessages.create(this.getClass().getSimpleName(), "createRetryContext")
                    .fail("invokerId is null")
                    .toString());
            return null;
        }

        return new RetryContext(
                invokerId,
                context.getMethod().getName(),
                context.getMethod().getParameterTypes(),
                context.getParameterValues(),
                attribute.isShouldRemoveTrackerAfterCompleted(),
                attribute.getRetryForClass(),
                attribute.getNoRetryForClass()
        );
    }

    private String serializeContext(RetryContext context) {
        return JSON.toJSONString(context);
    }
}