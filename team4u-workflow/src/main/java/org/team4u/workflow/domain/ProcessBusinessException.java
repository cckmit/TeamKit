package org.team4u.workflow.domain;

import org.team4u.base.error.BusinessException;

/**
 * 流程业务异常
 */
public class ProcessBusinessException extends BusinessException {

    public ProcessBusinessException() {
    }

    public ProcessBusinessException(String message) {
        super(message);
    }

    public ProcessBusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProcessBusinessException(Throwable cause) {
        super(cause);
    }

    public ProcessBusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
