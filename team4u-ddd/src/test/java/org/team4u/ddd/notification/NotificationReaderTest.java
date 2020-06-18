package org.team4u.ddd.notification;

import org.team4u.ddd.domain.model.DomainEvent;
import org.team4u.ddd.infrastructure.serializer.FastJsonSerializer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NotificationReaderTest {

    @Test
    public void testReadBasicProperties() {
        DomainEvent domainEvent = new FakeDomainEvent("testing");
        Notification notification = new Notification(1, domainEvent);
        String serializedNotification = FastJsonSerializer.instance().serialize(notification);

        NotificationReader reader = new NotificationReader(serializedNotification);

        assertEquals(1L, reader.notificationId());
        assertEquals("1", reader.notificationIdAsString());
        assertEquals(domainEvent.getOccurredOn(), reader.occurredOn());
        assertEquals(notification.getTypeName(), reader.typeName());
        assertEquals(notification.getVersion(), reader.version());
        assertEquals(domainEvent.getVersion(), reader.version());
        assertEquals("testing", reader.eventValue("name"));
    }
}