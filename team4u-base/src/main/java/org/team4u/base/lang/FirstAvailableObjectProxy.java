package org.team4u.base.lang;

import cn.hutool.aop.ProxyUtil;
import cn.hutool.core.collection.CollUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 第一可用对象代理
 *
 * @author jay.wu
 */
public class FirstAvailableObjectProxy {

    /**
     * 创建目标代理
     * <p>
     * 代理负责在候选对象中，按集合顺序，选取第一个可用的对象，代理执行
     *
     * @param type        需要代理的接口类型
     * @param candidates  接口实现类候选人集合
     * @param ignoreError 是否忽略异常，不忽略则遇到异常直接外抛；忽略则会继续尝试剩余候选人
     * @param <V>         代理类型
     * @return 代理对象
     */
    public static <V> V newProxyInstance(Class<V> type, List<V> candidates, boolean ignoreError) {
        if (CollUtil.isEmpty(candidates)) {
            throw new IllegalArgumentException("candidates should not be empty");
        }

        return ProxyUtil.newProxyInstance(new ObjectInvocationHandler(candidates, ignoreError), type);
    }

    private static class ObjectInvocationHandler implements InvocationHandler {

        private final List<?> candidates;

        private final boolean ignoreError;

        private ObjectInvocationHandler(List<?> candidates, boolean ignoreError) {
            this.candidates = candidates;
            this.ignoreError = ignoreError;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Exception lastException = null;

            for (Object candidate : candidates) {
                try {
                    Object returnValue = method.invoke(candidate, args);
                    if (returnValue != null) {
                        return returnValue;
                    }
                } catch (Exception e) {
                    if (!ignoreError) {
                        throw e;
                    }

                    lastException = e;
                }
            }

            if (lastException != null) {
                throw lastException;
            }

            return null;
        }
    }
}