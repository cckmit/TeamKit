package org.team4u.core.error;

public class NestedException extends RuntimeException {

    public NestedException() {
    }

    public NestedException(String message) {
        super(message);
    }

    public NestedException(String message, Throwable cause) {
        super(message, cause);
    }

    public NestedException(Throwable cause) {
        super(cause);
    }
}