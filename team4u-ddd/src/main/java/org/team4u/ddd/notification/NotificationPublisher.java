package org.team4u.ddd.notification;

/**
 * 通知发布者
 *
 * @author jay.wu
 */
public interface NotificationPublisher {

    /**
     * 发布通知
     */
    void publishNotifications();
}