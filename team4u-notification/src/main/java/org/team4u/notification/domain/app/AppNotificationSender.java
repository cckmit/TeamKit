package org.team4u.notification.domain.app;

import org.team4u.notification.domain.NotificationSender;

/**
 * 应用发送者
 *
 * @author jay.wu
 */
public interface AppNotificationSender extends NotificationSender<AppNotification> {

    @Override
    default String id() {
        return NotificationSender.Type.APP.name();
    }
}
