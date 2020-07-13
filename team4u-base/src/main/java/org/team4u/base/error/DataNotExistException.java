package org.team4u.base.error;

public class DataNotExistException extends BusinessException {

    public DataNotExistException() {
    }

    public DataNotExistException(String message) {
        super(message);
    }

    public DataNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataNotExistException(Throwable cause) {
        super(cause);
    }
}
