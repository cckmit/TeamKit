package org.team4u.base.lang.lazy;

/**
 * 空值异常
 *
 * @author jay.wu
 */
public class NullValueException extends IllegalStateException {

    public NullValueException() {
    }

    public NullValueException(String s) {
        super(s);
    }

    public NullValueException(String message, Throwable cause) {
        super(message, cause);
    }

    public NullValueException(Throwable cause) {
        super(cause);
    }
}