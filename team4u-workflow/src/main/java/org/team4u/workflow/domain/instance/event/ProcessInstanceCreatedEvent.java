package org.team4u.workflow.domain.instance.event;

import org.team4u.ddd.domain.model.AbstractDomainEvent;
import org.team4u.workflow.domain.instance.ProcessInstance;

import java.util.Date;

/**
 * 流程已创建事件
 *
 * @author jay.wu
 */
public class ProcessInstanceCreatedEvent extends AbstractDomainEvent {

    private final ProcessInstance processInstance;

    public ProcessInstanceCreatedEvent(String domainId,
                                       Date occurredOn,
                                       ProcessInstance processInstance) {
        super(domainId, occurredOn);
        this.processInstance = processInstance;
    }

    public ProcessInstance getProcessInstance() {
        return processInstance;
    }
}