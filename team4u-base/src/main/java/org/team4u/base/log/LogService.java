package org.team4u.base.log;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.log.Log;
import cn.hutool.log.level.Level;
import org.team4u.base.error.BusinessException;
import org.team4u.base.error.ControlException;
import org.team4u.base.error.NestedException;

import java.util.concurrent.Callable;

/**
 * 日志服务
 *
 * @author jay.wu
 */
public class LogService {

    /**
     * 打印包含异常的日志
     */
    public static void logForError(Log log, LogMessage lm, Throwable e) {
        if (e == null) {
            return;
        }

        if (e instanceof ControlException) {
            if (log.isInfoEnabled()) {
                log.info(lm.result(e.getMessage()).toString());
            }
            return;
        }

        if (e instanceof BusinessException) {
            if (log.isWarnEnabled()) {
                log.warn(lm.fail(e.getMessage()).toString());
            }
            return;
        }

        if (log.isErrorEnabled()) {
            log.error(e, lm.fail(e.getMessage()).toString());
        }
    }

    public static <V> V withLog(Log log,
                                Level level,
                                String eventName,
                                Callable<V> callable) {
        LogMessage lm = LogMessageContext.createAndSet(ClassUtil.getShortClassName(log.getName()), eventName);

        try {
            V result = callable.call();

            if (log.isEnabled(level)) {
                log.log(level, lm.success().toString());
            }

            return result;
        } catch (Exception e) {
            logForError(log, lm, e);
            throw NestedException.wrap(e);
        }
    }

    public static <V> V withDebugLog(Log log,
                                     String eventName,
                                     Callable<V> callable) {
        return withLog(log, Level.DEBUG, eventName, callable);
    }

    public static <V> V withInfoLog(Log log,
                                    String eventName,
                                    Callable<V> callable) {
        return withLog(log, Level.INFO, eventName, callable);
    }
}