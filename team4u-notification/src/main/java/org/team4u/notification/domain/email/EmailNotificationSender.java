package org.team4u.notification.domain.email;

import org.team4u.notification.domain.NotificationSender;

/**
 * 电子邮件发送者
 *
 * @author jay.wu
 */
public interface EmailNotificationSender extends NotificationSender<EmailNotification> {

    @Override
    default String id() {
        return Type.EMAIL.name();
    }
}