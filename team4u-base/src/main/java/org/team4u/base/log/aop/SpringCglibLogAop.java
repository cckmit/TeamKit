package org.team4u.base.log.aop;

import cn.hutool.aop.aspects.Aspect;
import org.team4u.base.aop.SpringCglibProxyFactory;

import java.lang.reflect.Method;

/**
 * 基于Spring Cglib的日志跟踪代理，可打印方法的输入、输出信息
 *
 * <p>
 * 当Config禁用日志时，性能比原始对象调用有较大差距，整体性能较低
 * <p>
 * 建议优先使用ByteBuddyLogAop
 *
 * @author jay.wu
 */
public class SpringCglibLogAop implements LogAop {

    @Override
    public <T> T proxy(T target, Config config) {
        return new SpringCglibProxyFactory().proxy(target, new LogTraceAspect(config, target));
    }

    public static class LogTraceAspect extends AbstractLogMethodInterceptor implements Aspect {

        private LogTraceAspect(Config config, Object target) {
            super(config, target);
        }

        @Override
        public boolean before(Object target, Method method, Object[] args) {
            beforeInvoke(target, method, args);
            return true;
        }

        @Override
        public boolean after(Object target, Method method, Object[] args, Object returnVal) {
            afterInvoke(target, method, args, returnVal);
            return true;
        }

        @Override
        public boolean afterException(Object target, Method method, Object[] args, Throwable e) {
            afterInvokeException(target, method, args, e);
            return true;
        }
    }
}