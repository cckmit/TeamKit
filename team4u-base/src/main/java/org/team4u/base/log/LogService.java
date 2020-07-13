package org.team4u.base.log;

import cn.hutool.log.Log;
import org.team4u.base.error.BusinessException;
import org.team4u.base.error.ControlException;

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
            log.info(lm.result(e.getMessage()).toString());
            return;
        }

        if (e instanceof BusinessException) {
            log.warn(lm.fail(e.getMessage()).toString());
            return;
        }

        log.error(e, lm.fail(e.getMessage()).toString());
    }
}