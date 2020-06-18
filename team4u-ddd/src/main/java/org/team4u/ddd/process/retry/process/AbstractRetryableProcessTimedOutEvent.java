package org.team4u.ddd.process.retry.process;

import org.team4u.ddd.process.ProcessTimedOutEvent;

/**
 * 可重试超时事件
 *
 * @author jay.wu
 */
public abstract class AbstractRetryableProcessTimedOutEvent extends ProcessTimedOutEvent {

    public AbstractRetryableProcessTimedOutEvent(String processId, String description) {
        super(processId, description);
    }

    public AbstractRetryableProcessTimedOutEvent(String processId, int totalRetriesPermitted, int retryCount, String description) {
        super(processId, totalRetriesPermitted, retryCount, description);
    }
}