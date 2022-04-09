package org.team4u.base.log;

import cn.hutool.core.lang.func.Func1;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.log.Log;
import cn.hutool.log.level.Level;
import org.team4u.base.error.BusinessException;
import org.team4u.base.error.ControlException;
import org.team4u.base.error.NestedException;

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
                                String module,
                                String eventName,
                                Func1<LogMessage, V> callable) {
        module = ObjectUtil.defaultIfNull(module, ClassUtil.getShortClassName(log.getName()));
        LogMessage lm = LogMessage.create(module, eventName);

        try {
            V result = callable.call(lm);

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
                                     String module,
                                     String eventName,
                                     Func1<LogMessage, V> callable) {
        return withLog(log, Level.DEBUG, module, eventName, callable);
    }

    public static <V> V withDebugLog(Log log,
                                     String eventName,
                                     Func1<LogMessage, V> callable) {
        return withLog(log, Level.DEBUG, null, eventName, callable);
    }

    public static <V> V withInfoLog(Log log,
                                    String module,
                                    String eventName,
                                    Func1<LogMessage, V> callable) {
        return withLog(log, Level.INFO, module, eventName, callable);
    }

    public static <V> V withInfoLog(Log log,
                                    String eventName,
                                    Func1<LogMessage, V> callable) {
        return withLog(log, Level.INFO, null, eventName, callable);
    }
}