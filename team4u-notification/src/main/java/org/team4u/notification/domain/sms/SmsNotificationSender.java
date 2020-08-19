package org.team4u.notification.domain.sms;

import org.team4u.notification.domain.NotificationSender;

/**
 * 短信发送者
 *
 * @author jay.wu
 */
public interface SmsNotificationSender extends NotificationSender<SmsNotification> {

    @Override
    default String id() {
        return NotificationSender.Type.SMS.name();
    }
}