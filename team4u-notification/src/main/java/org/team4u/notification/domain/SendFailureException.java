package org.team4u.notification.domain;

import org.team4u.base.error.BusinessException;

public class SendFailureException extends BusinessException {

    public SendFailureException() {
    }

    public SendFailureException(String message) {
        super(message);
    }

    public SendFailureException(String message, Throwable cause) {
        super(message, cause);
    }

    public SendFailureException(Throwable cause) {
        super(cause);
    }

    public SendFailureException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
