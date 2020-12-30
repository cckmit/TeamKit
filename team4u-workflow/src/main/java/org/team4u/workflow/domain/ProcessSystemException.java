package org.team4u.workflow.domain;

import org.team4u.base.error.SystemException;

/**
 * 流程系统异常
 */
public class ProcessSystemException extends SystemException {

    public ProcessSystemException() {
    }

    public ProcessSystemException(String message) {
        super(message);
    }

    public ProcessSystemException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProcessSystemException(Throwable cause) {
        super(cause);
    }

    public ProcessSystemException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}