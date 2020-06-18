package org.team4u.ddd.process.retry.method.annotation;

import java.lang.reflect.Method;

/**
 * 重试注解解析器
 *
 * @author jay.wu
 */
public class RetryableAnnotationParser {

    /**
     * 解析注解
     *
     * @param method 方法
     * @return 重试属性
     */
    public RetryableAttribute parse(Method method) {
        if (method == null) {
            return null;
        }

        Retryable retryable = method.getAnnotation(Retryable.class);
        if (retryable == null) {
            return null;
        }

        return new RetryableAttribute()
                .setRetryStrategyId(retryable.retryStrategyId())
                .setShouldRemoveTrackerAfterCompleted(retryable.shouldRemoveTrackerAfterCompleted())
                .setNoRetryForClass(retryable.noRetryFor())
                .setRetryForClass(retryable.retryFor());
    }
}