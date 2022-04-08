package org.team4u.ddd.process;

import cn.hutool.core.date.DateUtil;
import org.junit.Assert;
import org.junit.Test;
import org.team4u.base.message.jvm.AbstractMessageSubscriber;
import org.team4u.ddd.TestUtil;
import org.team4u.ddd.domain.model.DomainEventPublisher;
import org.team4u.ddd.process.strategy.FixedRetryStrategy;

import java.util.concurrent.atomic.AtomicReference;

public class TimeConstrainedProcessTrackerTest {

    @Test
    public void retryIntervalSec() {
        Assert.assertEquals(1, tracker(1).retryIntervalSec());
    }

    @Test
    public void completed() {
        TimeConstrainedProcessTracker tracker = tracker(1);

        Assert.assertFalse(tracker.isCompleted());

        tracker.completed();

        Assert.assertTrue(tracker.isCompleted());
    }

    @Test
    public void baseInfo() {
        TimeConstrainedProcessTracker tracker = tracker(1);
        Assert.assertEquals(TestUtil.TEST_ID, tracker.description());
        Assert.assertEquals(TestUtil.TEST_ID, tracker.processId());
        Assert.assertEquals(TestUtil.TEST_ID, tracker.trackerId());
        Assert.assertEquals(2, tracker.maxRetriesPermitted());
    }

    @Test
    public void processInformedOfTimeout() {
        TimeConstrainedProcessTracker tracker = tracker(1);
        AtomicReference<ProcessTimedOutEvent> x = new AtomicReference<>();
        DomainEventPublisher.instance().subscribe(new AbstractMessageSubscriber<ProcessTimedOutEvent>() {

            @Override
            protected void internalOnMessage(ProcessTimedOutEvent message) {
                x.set(message);
            }
        });

        Assert.assertFalse(tracker.isProcessInformedOfTimeout());

        tracker.informProcessTimedOut();
        Assert.assertFalse(tracker.isProcessInformedOfTimeout());

        tracker.informProcessTimedOut();
        Assert.assertTrue(tracker.isProcessInformedOfTimeout());

        DomainEventPublisher.instance().publishAll(tracker.events());

        ProcessTimedOutEvent event = x.get();

        Assert.assertNotNull(event);
        Assert.assertEquals(2, event.getMaxRetriesPermitted());
        Assert.assertEquals(2, event.getRetryCount());
        Assert.assertEquals(TestUtil.TEST_ID, event.getDomainId());
        Assert.assertEquals(TestUtil.TEST_ID, event.getDescription());
    }

    @Test
    public void processInformedOfNotTimeout() {
        TimeConstrainedProcessTracker tracker = tracker(Integer.MAX_VALUE);
        tracker.informProcessTimedOut();
        Assert.assertFalse(tracker.isProcessInformedOfTimeout());
    }

    @Test
    public void processTimedOutEventType() {
        TimeConstrainedProcessTracker tracker = tracker(1);
        Assert.assertEquals(ProcessTimedOutEvent.class.getName(), tracker.processTimedOutEventType());
    }

    @Test
    public void hasTimedOut() {
        TimeConstrainedProcessTracker tracker = tracker(1);
        Assert.assertTrue(tracker.hasTimedOut());
    }

    @Test
    public void hasNotTimedOut() {
        TimeConstrainedProcessTracker tracker = tracker(Integer.MAX_VALUE);
        Assert.assertFalse(tracker.hasTimedOut());
    }

    @Test
    public void retryCount() {
        TimeConstrainedProcessTracker tracker = tracker(1);
        Assert.assertEquals(0, tracker.retryCount());
        tracker.informProcessTimedOut();
        Assert.assertEquals(1, tracker.retryCount());
        tracker.informProcessTimedOut();
        Assert.assertEquals(2, tracker.retryCount());
    }

    @Test
    public void timeoutOccursOn() {
        TimeConstrainedProcessTracker tracker = tracker(1);
        Assert.assertEquals(DateUtil.parse("2020-04-07 00:00:01"), tracker.timeoutOccursOn());

        tracker.informProcessTimedOut();
        Assert.assertEquals(DateUtil.parse("2020-04-07 00:00:02"), tracker.timeoutOccursOn());

    }

    private TimeConstrainedProcessTracker tracker(int intervalSec) {
        return new TimeConstrainedProcessTracker(
                TestUtil.TEST_ID,
                TestUtil.TEST_ID,
                TestUtil.TEST_ID,
                DateUtil.parse("2020-04-07"),
                new FixedRetryStrategy(new FixedRetryStrategy.Config()
                        .setIntervalSec(intervalSec)
                        .setMaxRetriesPermitted(2)
                        .setId("FIXED_" + intervalSec)),
                ProcessTimedOutEvent.class.getName());
    }
}