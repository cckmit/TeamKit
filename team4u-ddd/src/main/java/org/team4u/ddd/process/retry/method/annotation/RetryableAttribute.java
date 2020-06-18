package org.team4u.ddd.process.retry.method.annotation;

/**
 * 重试属性
 *
 * @author jay.wu
 */
public class RetryableAttribute {

    /**
     * 重试策略标识
     */
    private String retryStrategyId;

    /**
     * 重试完成后是否需要清除跟踪器记录
     */
    private boolean shouldRemoveTrackerAfterCompleted;

    /**
     * 定义需要重试的异常类数组
     */
    private Class<? extends Throwable>[] retryForClass;

    /**
     * 定义无需重试的异常类数组
     */
    private Class<? extends Throwable>[] noRetryForClass;

    public String getRetryStrategyId() {
        return retryStrategyId;
    }

    public RetryableAttribute setRetryStrategyId(String retryStrategyId) {
        this.retryStrategyId = retryStrategyId;
        return this;
    }

    public boolean isShouldRemoveTrackerAfterCompleted() {
        return shouldRemoveTrackerAfterCompleted;
    }

    public RetryableAttribute setShouldRemoveTrackerAfterCompleted(boolean shouldRemoveTrackerAfterCompleted) {
        this.shouldRemoveTrackerAfterCompleted = shouldRemoveTrackerAfterCompleted;
        return this;
    }

    public Class<? extends Throwable>[] getRetryForClass() {
        return retryForClass;
    }

    public RetryableAttribute setRetryForClass(Class<? extends Throwable>[] retryForClass) {
        this.retryForClass = retryForClass;
        return this;
    }

    public Class<? extends Throwable>[] getNoRetryForClass() {
        return noRetryForClass;
    }

    public RetryableAttribute setNoRetryForClass(Class<? extends Throwable>[] noRetryForClass) {
        this.noRetryForClass = noRetryForClass;
        return this;
    }
}
