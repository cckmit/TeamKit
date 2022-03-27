package org.team4u.ddd.process;


import org.team4u.ddd.domain.model.AbstractDomainEvent;
import org.team4u.ddd.domain.model.TransientDomainEvent;

/**
 * 超时事件
 *
 * @author jay.wu
 */
public class ProcessTimedOutEvent extends AbstractDomainEvent implements TransientDomainEvent {

    /**
     * 上下文描述
     */
    private final String description;
    /**
     * 已重试次数
     */
    private int retryCount;
    /**
     * 最大重试次数
     */
    private int maxRetriesPermitted;

    public ProcessTimedOutEvent(String processId, String description) {
        super(processId);
        this.description = description;
    }

    public ProcessTimedOutEvent(
            String processId,
            int maxRetriesPermitted,
            int retryCount,
            String description) {
        super(processId);
        this.retryCount = retryCount;
        this.maxRetriesPermitted = maxRetriesPermitted;
        this.description = description;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public int getMaxRetriesPermitted() {
        return maxRetriesPermitted;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
                "domainId='" + getDomainId() + '\'' +
                ", occurredOn=" + getOccurredOn() +
                ", version=" + getVersion() +
                ", retryCount=" + retryCount +
                ", totalRetriesPermitted=" + maxRetriesPermitted +
                '}';
    }
}