package org.team4u.ddd.message;

import org.junit.Assert;
import org.junit.Test;
import org.team4u.ddd.domain.model.DomainEvent;

public class AbstractMessageConsumerTest {

    @Test
    public void handleWithExpectedEvent() {
        FakeEvent1Subscriber s = new FakeEvent1Subscriber();
        FakeEvent1 e = new FakeEvent1("");
        s.onMessage(e);
        Assert.assertEquals(e, s.getEvent());
    }

    @Test
    public void handleWithNotExpectedEvent() {
        FakeEvent1Subscriber s = new FakeEvent1Subscriber();
        FakeEvent2 e = new FakeEvent2("");
        s.onMessage(e);
        Assert.assertNull(s.getEvent());
    }


    private static class FakeEvent1Subscriber extends AbstractMessageConsumer<DomainEvent> {

        private DomainEvent event;

        @Override
        protected void internalOnMessage(DomainEvent event) {
            this.event = event;
        }

        public DomainEvent getEvent() {
            return event;
        }

        @Override
        protected boolean supports(Object event) {
            return event instanceof FakeEvent1;
        }
    }
}