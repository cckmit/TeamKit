package org.team4u.workflow.domain.instance;

import org.team4u.ddd.domain.model.AbstractDomainEvent;

import java.util.Date;

/**
 * 流程节点变更事件
 *
 * @author jay.wu
 */
public class ProcessNodeChangedEvent extends AbstractDomainEvent {

    private final ProcessInstance processInstance;
    private final ProcessInstanceLog processInstanceLog;

    public ProcessNodeChangedEvent(String domainId,
                                   Date occurredOn,
                                   ProcessInstance processInstance,
                                   ProcessInstanceLog processInstanceLog) {
        super(domainId, occurredOn);
        this.processInstance = processInstance;
        this.processInstanceLog = processInstanceLog;
    }

    public ProcessInstance getProcessInstance() {
        return processInstance;
    }

    public ProcessInstanceLog getProcessInstanceLog() {
        return processInstanceLog;
    }
}