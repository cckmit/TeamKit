package org.team4u.ddd.process.retry.process;

import org.team4u.ddd.message.FakeEvent1;
import org.team4u.ddd.process.TimeConstrainedProcessTrackerAppService;

import java.util.concurrent.ExecutorService;

class SimpleRetryableProcessCombinedEventSubscriber extends
        AbstractRetryableProcessCombinedEventSubscriber<FakeEvent1, FakeRetryableProcessTimedOutEvent> {

    private FakeRetryableProcessTimedOutEvent timedOutEvent;
    private FakeEvent1 fakeEvent1;

    public SimpleRetryableProcessCombinedEventSubscriber(TimeConstrainedProcessTrackerAppService trackerAppService) {
        super(trackerAppService);
    }

    @Override
    protected void handleProcessEvent(FakeEvent1 event) {
        this.fakeEvent1 = event;
    }

    @Override
    protected void handleRetryProcessEvent(FakeRetryableProcessTimedOutEvent timedOutEvent, FakeEvent1 processEvent) {
        this.timedOutEvent = timedOutEvent;
        this.fakeEvent1 = processEvent;
    }

    @Override
    protected String retryStrategyId(FakeEvent1 event) {
        return "";
    }

    @Override
    protected boolean shouldRemoveTrackerAfterCompleted() {
        return true;
    }

    @Override
    protected ExecutorService executorService() {
        return null;
    }

    public FakeRetryableProcessTimedOutEvent getTimedOutEvent() {
        return timedOutEvent;
    }

    public FakeEvent1 getFakeEvent1() {
        return fakeEvent1;
    }
}