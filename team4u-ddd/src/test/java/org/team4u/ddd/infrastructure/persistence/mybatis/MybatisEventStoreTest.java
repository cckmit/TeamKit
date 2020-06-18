package org.team4u.ddd.infrastructure.persistence.mybatis;

import org.team4u.ddd.DbTest;
import org.team4u.ddd.FakeLongIdentityFactory;
import org.team4u.ddd.domain.model.AbstractDomainEvent;
import org.team4u.ddd.event.EventStore;
import org.team4u.ddd.event.StoredEvent;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class MybatisEventStoreTest extends DbTest {

    @Autowired
    private EventStore eventStore;

    @Before
    public void setUp() throws Exception {
        eventStore.removeStoredEventsBefore(Long.MAX_VALUE);
    }

    @Test
    public void allStoredEventsBetween() {
        FakeLongIdentityFactory.getInstance().setId(1L);
        appendEvent();

        List<StoredEvent> events = eventStore.allStoredEventsBetween(1, 1);
        Assert.assertEquals(1, events.size());
        checkEvent(events.get(0));

        events = eventStore.allStoredEventsBetween(2, 2);
        Assert.assertEquals(0, events.size());
    }

    @Test
    public void allStoredEventsSince() {
        FakeLongIdentityFactory.getInstance().setId(1L);
        appendEvent();

        List<StoredEvent> events = eventStore.allStoredEventsSince(0);
        Assert.assertEquals(1, events.size());
        checkEvent(events.get(0));

        events = eventStore.allStoredEventsSince(1);
        Assert.assertEquals(0, events.size());
    }

    @Test
    public void append() {
        FakeLongIdentityFactory.getInstance().setId(1L);
        StoredEvent storedEvent = appendEvent();
        checkEvent(storedEvent);
    }

    @Test
    public void countStoredEvents() {
        Assert.assertEquals(0, eventStore.countStoredEvents());

        appendEvent();

        Assert.assertEquals(1, eventStore.countStoredEvents());
    }

    @Test
    public void removeStoredEventsBefore() {
        appendEvent();
        eventStore.removeStoredEventsBefore(2);
        Assert.assertEquals(0, eventStore.countStoredEvents());
    }

    @Override
    protected String[] ddlResourcePaths() {
        return new String[]{"sql/stored_event.sql"};
    }

    private void checkEvent(StoredEvent storedEvent) {
        Assert.assertEquals(1L, storedEvent.eventId());
        Assert.assertEquals(TestEvent.class.getName(), storedEvent.typeName());
        Assert.assertEquals("TEST_ID", storedEvent.toDomainEvent().getDomainId());
    }

    private StoredEvent appendEvent() {
        return eventStore.append(new TestEvent());
    }

    public static class TestEvent extends AbstractDomainEvent {

        public TestEvent() {
            super("TEST_ID");
        }
    }
}