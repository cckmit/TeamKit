package org.team4u.base.log;

import cn.hutool.aop.aspects.Aspect;
import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.team4u.base.aop.MethodInterceptor;
import org.team4u.base.aop.SpringCglibProxyFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * 日志跟踪代理，可打印方法的输入、输出信息
 *
 * @author jay.wu
 */
public class LogTraceProxyFactory {

    /**
     * 生成可打印日志的代理类，底层使用Spring和Cglib
     *
     * @param target 原始对象
     * @param <T>    代理类型
     * @return 代理类
     */
    public static <T> T proxy(T target) {
        return proxy(target, new Config());
    }

    /**
     * 生成可打印日志的代理类，底层使用Spring和Cglib
     *
     * @param target 原始对象
     * @param config 配置
     * @param <T>    代理类型
     * @return 代理类
     */
    public static <T> T proxy(T target, Config config) {
        return new SpringCglibProxyFactory().proxy(target, new LogTraceAspect(config, target));
    }

//    /**
//     * 生成可打印日志的代理类，底层使用bytebuddy
//     *
//     * @param target 原始对象
//     * @param <T>    代理类型
//     * @return 代理类
//     */
//    public static <T> T proxy2(T target) {
//        return proxy(target, new Config());
//    }
//
//    /**
//     * 生成可打印日志的代理类，底层使用bytebuddy
//     *
//     * @param target 原始对象
//     * @param config 配置
//     * @param <T>    代理类型
//     * @return 代理类
//     */
//    @SuppressWarnings("unchecked")
//    public static <T> T proxy2(T target, Config config) {
//        return (T) SimpleAop.proxyOf(
//                target.getClass(),
//                ElementMatchers.any(),
//                new ValueMethodInterceptor(config, target)
//        );
//    }

    private static class ValueMethodInterceptor extends AbstractLogMethodInterceptor implements MethodInterceptor {

        private ValueMethodInterceptor(Config config, Object target) {
            super(config, target);
        }

        @Override
        public Object intercept(Object instance, Object[] parameters, Method method, Callable<?> superMethod) throws Throwable {
            beforeInvoke(target, method, parameters);

            try {
                Object result = ReflectUtil.invoke(target, method, parameters);
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

    public interface LogX {

        void info(String format);

        void error(Throwable t, String format);
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

    private static class DefaultLogX implements LogX {

        private final Log log = LogFactory.get();

        @Override
        public void info(String format) {
            log.info(format);
        }

        @Override
        public void error(Throwable t, String format) {
            log.error(t, format);
        }
    }

    public static class Config {
        /**
         * 不打印输入
         * <p>
         * 用于避免打印敏感信息
         */
        private boolean inputEnabled = true;
        /**
         * 不打印输出
         * <p>
         * 用于避免打印敏感信息
         */
        private boolean outputEnabled = true;
        /**
         * 是否开启打印日志功能
         */
        private boolean enabled = true;
        /**
         * 日志接口
         */
        private LogX logX = new DefaultLogX();
        /**
         * 日志消息配置
         */
        private LogMessageConfig logMessageConfig;

        public boolean isInputEnabled() {
            return inputEnabled;
        }

        public Config setInputEnabled(boolean inputEnabled) {
            this.inputEnabled = inputEnabled;
            return this;
        }

        public boolean isOutputEnabled() {
            return outputEnabled;
        }

        public Config setOutputEnabled(boolean outputEnabled) {
            this.outputEnabled = outputEnabled;
            return this;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public Config setEnabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public LogX getLogX() {
            return logX;
        }

        public Config setLogX(LogX logX) {
            this.logX = logX;
            return this;
        }

        public LogMessageConfig getLogMessageConfig() {
            return logMessageConfig;
        }

        public Config setLogMessageConfig(LogMessageConfig logMessageConfig) {
            this.logMessageConfig = logMessageConfig;
            return this;
        }
    }
}