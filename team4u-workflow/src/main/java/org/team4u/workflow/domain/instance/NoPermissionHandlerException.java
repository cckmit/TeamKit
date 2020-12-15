package org.team4u.workflow.domain.instance;

import org.team4u.base.error.BusinessException;

/**
 * 无权限操作异常
 *
 * @author jay.wu
 */
public class NoPermissionHandlerException extends BusinessException {

    public NoPermissionHandlerException() {
    }

    public NoPermissionHandlerException(String message) {
        super(message);
    }

    public NoPermissionHandlerException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoPermissionHandlerException(Throwable cause) {
        super(cause);
    }

    public NoPermissionHandlerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
