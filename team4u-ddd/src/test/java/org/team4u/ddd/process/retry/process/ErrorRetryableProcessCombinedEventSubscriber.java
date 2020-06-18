package org.team4u.ddd.process.retry.process;

import org.team4u.ddd.domain.model.FakeEvent1;
import org.team4u.ddd.process.TimeConstrainedProcessTrackerAppService;

import java.util.concurrent.ExecutorService;

class ErrorRetryableProcessCombinedEventSubscriber extends
        AbstractRetryableProcessCombinedEventSubscriber<FakeEvent1, FakeRetryableProcessTimedOutEvent> {

    private final RuntimeException exception;

    public ErrorRetryableProcessCombinedEventSubscriber(TimeConstrainedProcessTrackerAppService trackerAppService,
                                                        RuntimeException exception) {
        super(trackerAppService);
        this.exception = exception;
    }

    @Override
    protected void handleProcessEvent(FakeEvent1 event) {
        throw exception;
    }

    @Override
    protected void handleRetryProcessEvent(FakeRetryableProcessTimedOutEvent timedOutEvent, FakeEvent1 processEvent) {
    }

    @Override
    protected String retryStrategyId(FakeEvent1 event) {
        return null;
    }

    @Override
    protected boolean shouldRemoveTrackerAfterCompleted() {
        return true;
    }

    @Override
    protected ExecutorService executorService() {
        return null;
    }
}