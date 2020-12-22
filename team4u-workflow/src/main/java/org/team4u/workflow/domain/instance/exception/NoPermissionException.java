package org.team4u.workflow.domain.instance.exception;

import org.team4u.base.error.BusinessException;

/**
 * 无权限异常
 *
 * @author jay.wu
 */
public class NoPermissionException extends BusinessException {

    public NoPermissionException(String message) {
        super(message);
    }
}
