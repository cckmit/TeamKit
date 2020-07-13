package org.team4u.base.error;

public class ThreadExhaustedException extends SystemException {

    public ThreadExhaustedException(String message) {
        super(message);
    }

    public ThreadExhaustedException(String message, Throwable cause) {
        super(message, cause);
    }
}