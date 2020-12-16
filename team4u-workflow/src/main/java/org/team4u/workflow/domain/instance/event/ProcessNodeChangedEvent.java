package org.team4u.workflow.domain.instance.event;

import org.team4u.ddd.domain.model.AbstractDomainEvent;

import java.util.Date;

/**
 * 流程节点变更事件
 *
 * @author jay.wu
 */
public class ProcessNodeChangedEvent extends AbstractDomainEvent {

    /**
     * 当前节点标识
     */
    private final String nodeId;
    /**
     * 当前节点名称
     */
    private final String nodeName;
    /**
     * 流程动作标识
     */
    private final String actionId;
    /**
     * 流程动作名称
     */
    private final String actionName;
    /**
     * 下一个节点标识
     */
    private final String nextNodeId;
    /**
     * 下一个节点名称
     */
    private final String nextNodeName;
    /**
     * 备注
     */
    private final String remark;
    /**
     * 操作人
     */
    private final String operator;

    public ProcessNodeChangedEvent(String domainId,
                                   Date occurredOn,
                                   String nodeId,
                                   String nodeName,
                                   String actionId,
                                   String actionName,
                                   String nextNodeId,
                                   String nextNodeName,
                                   String remark,
                                   String operator) {
        super(domainId, occurredOn);
        this.nodeId = nodeId;
        this.nodeName = nodeName;
        this.actionId = actionId;
        this.actionName = actionName;
        this.nextNodeId = nextNodeId;
        this.nextNodeName = nextNodeName;
        this.remark = remark;
        this.operator = operator;
    }

    public String getNodeId() {
        return nodeId;
    }

    public String getNodeName() {
        return nodeName;
    }

    public String getActionId() {
        return actionId;
    }

    public String getActionName() {
        return actionName;
    }

    public String getNextNodeId() {
        return nextNodeId;
    }

    public String getNextNodeName() {
        return nextNodeName;
    }

    public String getRemark() {
        return remark;
    }

    public String getOperator() {
        return operator;
    }

    @Override
    public String toString() {
        return "node=" + getNodeId() +
                ",action=" + getActionId() +
                ",nextNode=" + getNextNodeId() +
                ",remark='" + getRemark() + '\'' +
                ",operator='" + getOperator() + '\'';
    }
}