package org.team4u.core.error;

public class IdempotentException extends BusinessException {

    public IdempotentException(String message) {
        super(message);
    }

    public IdempotentException() {
        this("Duplicate delivery");
    }

    public IdempotentException(String message, Throwable cause) {
        super(message, cause);
    }
}
