package org.team4u.workflow.domain.instance;

import org.team4u.ddd.domain.model.IdentifiedValueObject;
import org.team4u.workflow.domain.definition.ProcessAction;

import java.util.Date;
import java.util.Objects;

/**
 * 流程处理人
 *
 * @author jay.wu
 */
public class ProcessAssignee extends IdentifiedValueObject {

    /**
     * 节点标识
     */
    private String nodeId;
    /**
     * 处理人
     */
    private String assignee;
    /**
     * 流程动作
     */
    private ProcessAction action;

    private Date createTime;

    private Date updateTime;

    public ProcessAssignee(String nodeId, String assignee) {
        setNodeId(nodeId);
        setAssignee(assignee);
        setCreateTime(new Date());
        setUpdateTime(getCreateTime());
    }

    /**
     * 进行审批
     */
    public void handle(ProcessAction action) {
        refreshUpdateTime();
        setAction(action);
    }

    private void refreshUpdateTime() {
        setUpdateTime(new Date());
    }

    public String getNodeId() {
        return nodeId;
    }

    private void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getAssignee() {
        return assignee;
    }

    private void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public ProcessAction getAction() {
        return action;
    }

    private void setAction(ProcessAction action) {
        this.action = action;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public ProcessAssignee setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    private void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProcessAssignee that = (ProcessAssignee) o;
        return nodeId.equals(that.nodeId) && assignee.equals(that.assignee);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodeId, assignee);
    }

    @Override
    public String toString() {
        return nodeId + ',' +
                assignee + ',' +
                action;
    }
}