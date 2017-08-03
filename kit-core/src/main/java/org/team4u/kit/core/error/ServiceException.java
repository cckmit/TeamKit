package org.team4u.kit.core.error;

public class ServiceException extends RuntimeException {

    protected String code;

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String code, String message) {
        super(message);
        this.code = code;
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }

    public String getCode() {
        return code;
    }

    public ServiceException setCode(String code) {
        this.code = code;
        return this;
    }
}