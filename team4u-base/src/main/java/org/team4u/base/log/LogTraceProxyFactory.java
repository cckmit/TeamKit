package org.team4u.base.log;

import cn.hutool.aop.aspects.Aspect;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.team4u.base.aop.SpringCglibProxyFactory;

import java.lang.reflect.Method;

/**
 * 日志跟踪代理，可打印方法的输入、输出信息
 *
 * @author jay.wu
 */
public class LogTraceProxyFactory {
    public static <T> T proxy(T target) {
        return proxy(target, new Config());
    }

    public static <T> T proxy(T target, Config config) {
        return new SpringCglibProxyFactory().proxy(target, new LogTraceAspect(config));
    }

    public interface LogX {

        void info(String format);

        void error(Throwable t, String format);
    }

    public static class LogTraceAspect implements Aspect {

        private final Config config;

        public LogTraceAspect(Config config) {
            this.config = config;
        }

        @Override
        public boolean before(Object target, Method method, Object[] args) {
            if (!config.isEnabled()) {
                return true;
            }

            LogMessage logMessage = newLogMessage(target, method);

            if (config.isInputEnabled()) {
                logMessage.append("input", formatArgs(args));
            }

            config.getLogX().info(logMessage.processing().toString());
            return true;
        }

        private Object formatArgs(Object[] args) {
            Object input = args;
            if (ArrayUtil.length(args) == 1) {
                input = args[0];
            } else if (ArrayUtil.isEmpty(args)) {
                input = null;
            }

            return input;
        }

        @Override
        public boolean after(Object target, Method method, Object[] args, Object returnVal) {
            if (!config.isEnabled()) {
                return true;
            }

            LogMessage logMessage = newLogMessage(target, method);

            if (config.isOutputEnabled()) {
                logMessage.append("output", returnVal);
            }

            config.getLogX().info(logMessage.success().toString());

            return true;
        }

        @Override
        public boolean afterException(Object target, Method method, Object[] args, Throwable e) {
            if (!config.isEnabled()) {
                return true;
            }

            config.getLogX().error(
                    e,
                    newLogMessage(target, method).fail(e.getMessage()).toString()
            );

            return true;
        }

        private LogMessage newLogMessage(Object target, Method method) {
            LogMessage logMessage = LogMessages.createWithMasker(
                    target.getClass().getSimpleName(),
                    method.getName()
            );

            if (config.getLogMessageConfig() != null) {
                logMessage.setConfig(config.getLogMessageConfig());
            }

            return logMessage;
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