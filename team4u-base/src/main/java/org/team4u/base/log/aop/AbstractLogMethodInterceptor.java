package org.team4u.base.log.aop;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.log.Log;
import org.team4u.base.log.LogMessage;
import org.team4u.base.log.LogMessages;

import java.lang.reflect.Method;

public abstract class AbstractLogMethodInterceptor {

    private final Log log;

    protected final Object target;
    protected final LogAop.Config config;

    public AbstractLogMethodInterceptor(LogAop.Config config, Object target) {
        this.config = config;
        this.target = target;
        this.log = config.getLog();
    }

    protected void beforeInvoke(Object target, Method method, Object[] args) {
        if (!isMatchMethod(method.getName())) {
            return;
        }

        LogMessage logMessage = newLogMessage(target, method);

        if (config.isInputEnabled()) {
            logMessage.append("input", formatArgs(args));
        }

        if (log.isInfoEnabled()) {
            log.info(logMessage.processing().toString());
        }
    }

    protected void afterInvoke(Object target, Method method, Object[] args, Object returnVal) {
        if (!isMatchMethod(method.getName())) {
            return;
        }

        LogMessage logMessage = newLogMessage(target, method);

        if (config.isOutputEnabled()) {
            logMessage.append("output", returnVal);
        }

        if (log.isInfoEnabled()) {
            log.info(logMessage.success().toString());
        }
    }

    protected void afterInvokeException(Object target, Method method, Object[] args, Throwable e) {
        if (!isMatchMethod(method.getName())) {
            return;
        }

        if (log.isErrorEnabled()) {
            log.error(
                    e,
                    newLogMessage(target, method).fail(e.getMessage()).toString()
            );
        }
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

    private boolean isMatchMethod(String method) {
        // 开关关闭
        if (!config.isEnabled()) {
            return false;
        }

        // 存在白名单
        if (!config.getIncludedMethods().isEmpty()) {
            return config.getIncludedMethods().contains(method);
        }

        // 存在黑名单
        if (!config.getExcludedMethods().isEmpty()) {
            return !config.getExcludedMethods().contains(method);
        }

        // 默认匹配
        return true;
    }
}