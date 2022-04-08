package org.team4u.base.message.mq;

import org.junit.Assert;
import org.junit.Test;
import org.team4u.base.message.Message;
import org.team4u.base.message.jvm.AbstractMessageSubscriber;

public class AbstractMessageSubscriberTest {

    @Test
    public void handleWithExpectedEvent() {
        FakeEvent1Subscriber s = new FakeEvent1Subscriber();
        FakeEvent1 e = new FakeEvent1();
        s.onMessage(e);
        Assert.assertEquals(e, s.getEvent());
    }

    @Test
    public void handleWithNotExpectedEvent() {
        FakeEvent1Subscriber s = new FakeEvent1Subscriber();
        FakeEvent2 e = new FakeEvent2();
        s.onMessage(e);
        Assert.assertNull(s.getEvent());
    }


    private static class FakeEvent1Subscriber extends AbstractMessageSubscriber<Message> {

        private Message event;

        @Override
        protected void internalOnMessage(Message event) {
            this.event = event;
        }

        public Message getEvent() {
            return event;
        }

        @Override
        public boolean supports(Object event) {
            return event instanceof FakeEvent1;
        }
    }
}