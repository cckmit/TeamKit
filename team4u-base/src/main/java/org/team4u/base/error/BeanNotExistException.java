package org.team4u.base.error;

/**
 * bean不存在异常
 *
 * @author jay.wu
 */
public class BeanNotExistException extends BusinessException {

    public BeanNotExistException() {
    }

    public BeanNotExistException(String message) {
        super(message);
    }

    public BeanNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public BeanNotExistException(Throwable cause) {
        super(cause);
    }
}
