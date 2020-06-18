package org.team4u.ddd.notification;

import org.team4u.ddd.domain.model.DomainEvent;
import org.team4u.ddd.event.EventStore;
import org.team4u.ddd.event.StoredEvent;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 抽象通知发布者
 * <p>
 * - 跟踪已发布记录标识
 * - 清理已发布记录
 *
 * @author jay.wu
 */
public abstract class AbstractNotificationPublisher implements NotificationPublisher {

    private EventStore eventStore;
    private PublishedNotificationTrackerRepository publishedNotificationTrackerRepository;

    public AbstractNotificationPublisher(EventStore eventStore,
                                         PublishedNotificationTrackerRepository publishedNotificationTrackerRepository) {
        this.eventStore = eventStore;
        this.publishedNotificationTrackerRepository = publishedNotificationTrackerRepository;
    }

    @Override
    public void publishNotifications() {
        PublishedNotificationTracker tracker = publishedNotificationTrackerStore().publishedNotificationTracker();

        List<Notification> notifications = listUnpublishedNotifications(tracker.mostRecentPublishedNotificationId());

        for (Notification notification : notifications) {
            publish(notification);
        }

        publishedNotificationTrackerStore().trackMostRecentPublishedNotification(tracker, notifications);

        cleanupPublishedNotifications(tracker.mostRecentPublishedNotificationId());
    }

    protected abstract void publish(Notification notification);

    private List<Notification> listUnpublishedNotifications(long mostRecentPublishedMessageId) {
        List<StoredEvent> storedEvents = eventStore().allStoredEventsSince(mostRecentPublishedMessageId);
        return notificationsFrom(storedEvents);
    }

    private List<Notification> notificationsFrom(List<StoredEvent> storedEvents) {
        return storedEvents
                .stream()
                .map(storedEvent -> {
                    DomainEvent domainEvent = storedEvent.toDomainEvent();
                    return new Notification(storedEvent.eventId(), domainEvent);
                })
                .collect(Collectors.toList());
    }

    /**
     * 删除已发布的事件
     */
    private void cleanupPublishedNotifications(long mostRecentPublishedMessageId) {
        eventStore().removeStoredEventsBefore(mostRecentPublishedMessageId);
    }

    private PublishedNotificationTrackerRepository publishedNotificationTrackerStore() {
        return publishedNotificationTrackerRepository;
    }

    public EventStore eventStore() {
        return eventStore;
    }
}