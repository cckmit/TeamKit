package org.team4u.base.log.aop;

import cn.hutool.core.exceptions.UtilException;
import net.bytebuddy.matcher.ElementMatchers;
import org.team4u.base.aop.MethodInterceptor;
import org.team4u.base.aop.SimpleAop;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * 基于ByteBuddy的日志跟踪代理，可打印方法的输入、输出信息
 *
 * <p>
 * 当Config禁用日志时，性能与原始对象调用接近
 *
 * @author jay.wu
 */
public class ByteBuddyLogAop implements LogAop {

    @SuppressWarnings("unchecked")
    @Override
    public <T> T proxy(T target, Config config) {
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