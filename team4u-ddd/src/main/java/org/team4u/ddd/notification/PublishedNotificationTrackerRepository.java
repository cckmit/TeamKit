package org.team4u.ddd.notification;

import java.util.List;

/**
 * 已发布通知跟踪器资源库
 *
 * @author jay.wu
 */
public interface PublishedNotificationTrackerRepository {

    PublishedNotificationTracker publishedNotificationTracker();

    PublishedNotificationTracker publishedNotificationTracker(String typeName);

    void trackMostRecentPublishedNotification(
            PublishedNotificationTracker publishedNotificationTracker,
            List<Notification> notifications
    );

    String typeName();
}