package org.team4u.ddd.process.retry;


 import org.team4u.core.error.ControlException;

/**
 * 需要继续重试控制异常
 *
 * @author jay.wu
 */
public class NeedRetryException extends ControlException {

    public NeedRetryException() {
        super("[Control]Need to retry");
    }

    public NeedRetryException(String message) {
        super(message);
    }
}