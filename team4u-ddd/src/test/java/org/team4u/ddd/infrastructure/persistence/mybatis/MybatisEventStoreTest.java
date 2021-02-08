package org.team4u.ddd.infrastructure.persistence.mybatis;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.team4u.ddd.FakeLongIdentityFactory;
import org.team4u.ddd.TestUtil;
import org.team4u.ddd.domain.model.AbstractDomainEvent;
import org.team4u.ddd.event.EventStore;
import org.team4u.ddd.event.StoredEvent;
import org.team4u.test.spring.SpringDbTest;
import org.team4u.test.spring.DbTestBeanConfig;

import javax.annotation.PostConstruct;
import java.util.List;

@ContextConfiguration(classes = DbTestBeanConfig.class)
public class MybatisEventStoreTest extends SpringDbTest {

    @Autowired
    private EventStoreMapper mapper;

    private EventStore eventStore;

    @PostConstruct
    public void beforeClass() {
        eventStore = new MybatisEventStore(mapper, FakeLongIdentityFactory.getInstance());
    }

    @Before
    public void setUp() {
        eventStore.removeStoredEventsBefore(Long.MAX_VALUE);
    }

    @Test
    public void allStoredEventsOf() {
        FakeLongIdentityFactory.getInstance().setId(1L);
        appendEvent();

        List<StoredEvent> events = eventStore.allStoredEventsOf(TestUtil.TEST_ID);
        Assert.assertEquals(1, events.size());
        checkEvent(events.get(0));
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
        Assert.assertEquals(TestUtil.TEST_ID, storedEvent.domainId());
        Assert.assertEquals(1L, storedEvent.eventId());
        Assert.assertEquals(TestEvent.class.getName(), storedEvent.typeName());
        Assert.assertEquals(TestUtil.TEST_ID, storedEvent.toDomainEvent().getDomainId());
    }

    private StoredEvent appendEvent() {
        return eventStore.append(new TestEvent());
    }

    public static class TestEvent extends AbstractDomainEvent {

        public TestEvent() {
            super(TestUtil.TEST_ID);
        }
    }
}