package org.team4u.base.error;

/**
 * 控制类异常
 * <p>
 * 该类用于控制流程，并非真正的异常
 *
 * @author jay.wu
 */
public class ControlException extends BusinessException {

    public ControlException() {
    }

    public ControlException(String message) {
        super(message);
    }

    public ControlException(String message, Throwable cause) {
        super(message, cause);
    }
}