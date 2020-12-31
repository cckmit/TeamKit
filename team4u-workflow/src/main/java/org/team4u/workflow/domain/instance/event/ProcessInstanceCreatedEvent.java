package org.team4u.workflow.domain.instance.event;

import org.team4u.ddd.domain.model.AbstractDomainEvent;
import org.team4u.workflow.domain.definition.ProcessDefinitionId;

import java.util.Date;

/**
 * 流程已创建事件
 *
 * @author jay.wu
 */
public class ProcessInstanceCreatedEvent extends AbstractDomainEvent {

    private final String createdBy;
    private final String currentNodeId;
    private final String processInstanceName;
    private final ProcessDefinitionId processDefinitionId;

    public ProcessInstanceCreatedEvent(String domainId,
                                       String processInstanceName,
                                       Date occurredOn,
                                       String createdBy,
                                       String currentNodeId,
                                       ProcessDefinitionId processDefinitionId) {
        super(domainId, occurredOn);
        this.createdBy = createdBy;
        this.currentNodeId = currentNodeId;
        this.processInstanceName = processInstanceName;
        this.processDefinitionId = processDefinitionId;
    }

    public String getCurrentNodeId() {
        return currentNodeId;
    }

    public ProcessDefinitionId getProcessDefinitionId() {
        return processDefinitionId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getProcessInstanceName() {
        return processInstanceName;
    }
}