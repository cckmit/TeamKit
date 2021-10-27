package org.team4u.base.log;

import cn.hutool.core.util.ArrayUtil;

import java.lang.reflect.Method;

public abstract class AbstractLogMethodInterceptor {

    protected final Object target;
    protected final LogTraceProxyFactory.Config config;

    public AbstractLogMethodInterceptor(LogTraceProxyFactory.Config config, Object target) {
        this.config = config;
        this.target = target;
    }

    protected void beforeInvoke(Object target, Method method, Object[] args) {
        if (!config.isEnabled()) {
            return;
        }

        LogMessage logMessage = newLogMessage(target, method);

        if (config.isInputEnabled()) {
            logMessage.append("input", formatArgs(args));
        }

        config.getLogX().info(logMessage.processing().toString());
    }

    protected void afterInvoke(Object target, Method method, Object[] args, Object returnVal) {
        if (!config.isEnabled()) {
            return;
        }

        LogMessage logMessage = newLogMessage(target, method);

        if (config.isOutputEnabled()) {
            logMessage.append("output", returnVal);
        }

        config.getLogX().info(logMessage.success().toString());
    }

    protected void afterInvokeException(Object target, Method method, Object[] args, Throwable e) {
        if (!config.isEnabled()) {
            return;
        }

        config.getLogX().error(
                e,
                newLogMessage(target, method).fail(e.getMessage()).toString()
        );
    }

    protected Object formatArgs(Object[] args) {
        Object input = args;
        if (ArrayUtil.length(args) == 1) {
            input = args[0];
        } else if (ArrayUtil.isEmpty(args)) {
            input = null;
        }

        return input;
    }

    protected LogMessage newLogMessage(Object target, Method method) {
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
