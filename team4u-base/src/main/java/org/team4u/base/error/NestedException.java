package org.team4u.base.error;

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

    public static RuntimeException wrap(Throwable throwable) {
        if (throwable instanceof RuntimeException) {
            return (RuntimeException) throwable;
        }

        return new NestedException(throwable);
    }
}