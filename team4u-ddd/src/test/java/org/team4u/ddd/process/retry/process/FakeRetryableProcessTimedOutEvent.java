package org.team4u.ddd.process.retry.process;

class FakeRetryableProcessTimedOutEvent extends AbstractRetryableProcessTimedOutEvent {

    public FakeRetryableProcessTimedOutEvent(String processId, String description) {
        super(processId, description);
    }

    public FakeRetryableProcessTimedOutEvent(String processId,
                                             int totalRetriesPermitted,
                                             int retryCount, String description) {
        super(processId, totalRetriesPermitted, retryCount, description);
    }
}