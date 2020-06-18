package org.team4u.ddd.process.retry.method.interceptor;

import java.lang.reflect.Method;

/**
 * 重试拦截器上下文
 *
 * @author jay.wu
 */
public class RetryInterceptorContext {

    /**
     * 可重试方法关联的执行者
     */
    private final Invoker invoker;
    /**
     * 可重试方法
     */
    private final Method method;
    /**
     * 方法入参值
     */
    private final Object[] parameterValues;

    public RetryInterceptorContext(Invoker invoker, Method method, Object[] parameterValues) {
        this.invoker = invoker;
        this.method = method;
        this.parameterValues = parameterValues;
    }

    public Invoker getInvoker() {
        return invoker;
    }

    public Method getMethod() {
        return method;
    }

    public Object[] getParameterValues() {
        return parameterValues;
    }


    public interface Invoker {

        /**
         * 获取执行者标识
         *
         * @return 执行者标识
         */
        String id();

        /**
         * 执行方法
         *
         * @return 执行后返回结果
         * @throws Throwable 执行异常
         */
        Object invoke() throws Throwable;
    }
}