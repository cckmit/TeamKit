package org.team4u.ddd.process.retry.method.interceptor;

/**
 * 重试上下文
 *
 * @author jay.wu
 */
public class RetryContext {

    /**
     * 重试实例标识
     */
    private final String invokerId;
    /**
     * 重试方法名称
     */
    private final String methodName;
    /**
     * 方法参数类型数组
     */
    private final Class<?>[] parameterTypes;
    /**
     * 方法入参值数组
     */
    private final Object[] parameterValues;

    /**
     * 重试完成后是否需要清除跟踪器记录
     */
    private final boolean shouldRemoveTrackerAfterCompleted;
    /**
     * 需重试的异常类数组
     */
    private final Class<? extends Throwable>[] retryForClass;
    /**
     * 无需重试的异常类数组
     */
    private final Class<? extends Throwable>[] noRetryForClass;

    public RetryContext(String invokerId,
                        String methodName,
                        Class<?>[] parameterTypes,
                        Object[] parameterValues,
                        boolean shouldRemoveTrackerAfterCompleted,
                        Class<? extends Throwable>[] retryForClass,
                        Class<? extends Throwable>[] noRetryForClass) {
        this.invokerId = invokerId;
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
        this.parameterValues = parameterValues;
        this.shouldRemoveTrackerAfterCompleted = shouldRemoveTrackerAfterCompleted;
        this.retryForClass = retryForClass;
        this.noRetryForClass = noRetryForClass;
    }

    public String getInvokerId() {
        return invokerId;
    }

    public String getMethodName() {
        return methodName;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public Object[] getParameterValues() {
        return parameterValues;
    }

    public boolean isShouldRemoveTrackerAfterCompleted() {
        return shouldRemoveTrackerAfterCompleted;
    }

    public Class<? extends Throwable>[] getRetryForClass() {
        return retryForClass;
    }

    public Class<? extends Throwable>[] getNoRetryForClass() {
        return noRetryForClass;
    }
}