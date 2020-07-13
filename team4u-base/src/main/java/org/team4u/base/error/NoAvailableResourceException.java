package org.team4u.base.error;

public class NoAvailableResourceException extends SystemException {

    public NoAvailableResourceException() {
    }

    public NoAvailableResourceException(String message) {
        super(message);
    }

    public NoAvailableResourceException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoAvailableResourceException(Throwable cause) {
        super(cause);
    }
}
