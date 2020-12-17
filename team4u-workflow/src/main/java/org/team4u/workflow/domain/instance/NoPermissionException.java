package org.team4u.workflow.domain.instance;

import org.team4u.base.error.BusinessException;

/**
 * 无权限异常
 *
 * @author jay.wu
 */
public class NoPermissionException extends BusinessException {

    public NoPermissionException() {
    }

    public NoPermissionException(String message) {
        super(message);
    }

    public NoPermissionException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoPermissionException(Throwable cause) {
        super(cause);
    }

    public NoPermissionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
