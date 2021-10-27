package org.team4u.base.log;

import cn.hutool.core.exceptions.UtilException;
import net.bytebuddy.matcher.ElementMatchers;
import org.team4u.base.aop.MethodInterceptor;
import org.team4u.base.aop.SimpleAop;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * 日志跟踪代理，可打印方法的输入、输出信息
 *
 * @author jay.wu
 */
public class LogTraceProxyFactory2 extends LogTraceProxyFactory {

    /**
     * 生成可打印日志的代理类，底层使用bytebuddy
     *
     * @param target 原始对象
     * @param <T>    代理类型
     * @return 代理类
     */
    public static <T> T proxy(T target) {
        return proxy(target, new Config());
    }

    /**
     * 生成可打印日志的代理类，底层使用bytebuddy
     *
     * @param target 原始对象
     * @param config 配置
     * @param <T>    代理类型
     * @return 代理类
     */
    @SuppressWarnings("unchecked")
    public static <T> T proxy(T target, Config config) {
        return (T) SimpleAop.proxyOf(
                target.getClass(),
                ElementMatchers.any(),
                new ValueMethodInterceptor(config, target)
        );
    }

    private static class ValueMethodInterceptor extends AbstractLogMethodInterceptor implements MethodInterceptor {

        private ValueMethodInterceptor(Config config, Object target) {
            super(config, target);
        }

        @Override
        public Object intercept(Object instance, Object[] parameters, Method method, Callable<?> superMethod) throws Throwable {
            beforeInvoke(target, method, parameters);

            try {
                Object result = superMethod.call();
                afterInvoke(target, method, parameters, result);
                return result;
            } catch (Throwable e) {
                Throwable real = e;

                if (real instanceof UtilException) {
                    real = real.getCause();
                    if (real instanceof InvocationTargetException) {
                        real = real.getCause();
                    }
                }

                afterInvokeException(target, method, parameters, real);
                throw real;
            }
        }
    }
}