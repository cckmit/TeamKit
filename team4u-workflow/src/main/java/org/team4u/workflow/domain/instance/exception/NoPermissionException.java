package org.team4u.workflow.domain.instance.exception;

import org.team4u.workflow.domain.ProcessBusinessException;

/**
 * 无权限异常
 *
 * @author jay.wu
 */
public class NoPermissionException extends ProcessBusinessException {

    public NoPermissionException(String message) {
        super(message);
    }
}
