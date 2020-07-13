package org.team4u.base.error;

public class SystemDataNotExistException extends SystemException {

    public SystemDataNotExistException() {
    }

    public SystemDataNotExistException(String message) {
        super(message);
    }

    public SystemDataNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public SystemDataNotExistException(Throwable cause) {
        super(cause);
    }
}
