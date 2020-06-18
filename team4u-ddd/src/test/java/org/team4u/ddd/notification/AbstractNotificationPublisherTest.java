package org.team4u.ddd.notification;

import cn.hutool.core.collection.CollUtil;
import org.team4u.ddd.event.EventSerializer;
import org.team4u.ddd.event.EventStore;
import org.team4u.ddd.event.StoredEvent;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.team4u.ddd.TestUtil;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(MockitoJUnitRunner.class)
public class AbstractNotificationPublisherTest {

    @Mock
    private EventStore eventStore;
    @Mock
    private PublishedNotificationTrackerRepository repository;

    @Test
    public void publish() {
        List<StoredEvent> storedEvents = CollUtil.newArrayList(
                new StoredEvent(
                        FakeDomainEvent.class.getName(), new Date(),
                        EventSerializer.instance().serialize(new FakeDomainEvent(TestUtil.TEST_ID)), 10
                )
        );
        Mockito.when(eventStore.allStoredEventsSince(0)).thenReturn(storedEvents);

        PublishedNotificationTracker tracker = new PublishedNotificationTracker(TestUtil.TEST_ID);
        Mockito.when(repository.publishedNotificationTracker()).thenReturn(tracker);

        TestablePublisher publisher = publisher();
        publisher.publishNotifications();

        Assert.assertEquals(10, publisher.getNotification().getNotificationId());
        Mockito.verify(repository, Mockito.times(1))
                .trackMostRecentPublishedNotification(
                        tracker,
                        storedEvents.stream()
                                .map(it -> new Notification(it.eventId(),
                                        it.toDomainEvent()))
                                .collect(Collectors.toList())
                );

        Mockito.verify(repository, Mockito.times(1))
                .trackMostRecentPublishedNotification(
                        tracker,
                        storedEvents.stream()
                                .map(it -> new Notification(it.eventId(),
                                        it.toDomainEvent()))
                                .collect(Collectors.toList())
                );

        Mockito.verify(eventStore, Mockito.times(1))
                .removeStoredEventsBefore(0);
    }

    private TestablePublisher publisher() {
        return new TestablePublisher(eventStore, repository);
    }

    public static class TestablePublisher extends AbstractNotificationPublisher {

        private Notification notification;

        public TestablePublisher(EventStore eventStore,
                                 PublishedNotificationTrackerRepository publishedNotificationTrackerRepository) {
            super(eventStore, publishedNotificationTrackerRepository);
        }

        @Override
        protected void publish(Notification notification) {
            this.notification = notification;
        }

        public Notification getNotification() {
            return notification;
        }
    }
}