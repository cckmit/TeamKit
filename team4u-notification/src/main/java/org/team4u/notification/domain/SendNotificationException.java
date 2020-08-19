package org.team4u.notification.domain;


import org.team4u.base.error.BusinessException;

public class SendNotificationException extends BusinessException {

    public SendNotificationException() {
    }

    public SendNotificationException(String message) {
        super(message);
    }

    public SendNotificationException(String message, Throwable cause) {
        super(message, cause);
    }

    public SendNotificationException(Throwable cause) {
        super(cause);
    }
}