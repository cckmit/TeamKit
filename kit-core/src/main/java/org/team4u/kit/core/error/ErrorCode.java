package org.team4u.kit.core.error;

public interface ErrorCode {

    String getMessage();

    String getCode();

    ServiceException createException(String code, String message);
}