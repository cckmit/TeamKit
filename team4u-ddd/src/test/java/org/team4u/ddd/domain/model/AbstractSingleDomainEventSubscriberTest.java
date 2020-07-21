package org.team4u.ddd.domain.model;

import org.junit.Assert;
import org.junit.Test;

public class AbstractSingleDomainEventSubscriberTest {

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


    private static class FakeEvent1Subscriber extends AbstractSingleDomainEventSubscriber<FakeEvent1> {

        private FakeEvent1 event;

        @Override
        protected void handle(FakeEvent1 event) {
            this.event = event;
        }

        public FakeEvent1 getEvent() {
            return event;
        }
    }
}