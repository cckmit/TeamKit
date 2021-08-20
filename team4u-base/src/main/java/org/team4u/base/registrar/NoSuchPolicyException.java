package org.team4u.base.registrar;

import org.team4u.base.error.SystemDataNotExistException;

/**
 * 无法找到匹配的策略异常
 *
 * @author jay.wu
 */
public class NoSuchPolicyException extends SystemDataNotExistException {

    public NoSuchPolicyException() {
    }

    public NoSuchPolicyException(String message) {
        super(message);
    }

    public NoSuchPolicyException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchPolicyException(Throwable cause) {
        super(cause);
    }
}