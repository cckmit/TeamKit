package org.team4u.ddd.process.retry.method.annotation;

import java.lang.annotation.*;

/**
 * 重试策略注解
 * <p>
 * 在需要重试的方法上使用
 *
 * @author jay.wu
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Retryable {

    /**
     * 定义重试策略标识
     *
     * @return 重试策略标识
     */
    String retryStrategyId();

    /**
     * 重试完成后是否需要清除跟踪器记录
     *
     * @return true:清除,false:不清除
     */
    boolean shouldRemoveTrackerAfterCompleted() default false;

    /**
     * 定义需要重试的异常类数组
     * <p>
     * 若不配置，默认所有异常均可重试
     *
     * @return 需重试的异常类数组
     */
    Class<? extends Throwable>[] retryFor() default {};

    /**
     * 定义无需重试的异常类数组
     * <p>
     * 若不配置，默认所有异常均可重试
     *
     * @return 无需重试的异常类数组
     */
    Class<? extends Throwable>[] noRetryFor() default {};
}