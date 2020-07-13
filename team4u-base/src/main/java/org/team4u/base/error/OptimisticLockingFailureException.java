package org.team4u.base.error;

public class OptimisticLockingFailureException extends SystemException {

    public OptimisticLockingFailureException() {
    }

    public OptimisticLockingFailureException(String message) {
        super(message);
    }

    public OptimisticLockingFailureException(String message, Throwable cause) {
        super(message, cause);
    }

    public OptimisticLockingFailureException(Throwable cause) {
        super(cause);
    }

    public OptimisticLockingFailureException(String message,
                                             Throwable cause,
                                             boolean enableSuppression,
                                             boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
