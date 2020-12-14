package org.team4u.workflow.domain.instance;

import org.team4u.ddd.domain.model.ValueObject;
import org.team4u.workflow.domain.definition.ProcessAction;

import java.util.Date;

/**
 * 流程处理人
 *
 * @author jay.wu
 */
public class ProcessAssignee extends ValueObject {

    /**
     * 流程实例标识
     */
    private String processInstanceId;
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

    public ProcessAssignee(String processInstanceId, String assignee) {
        setProcessInstanceId(processInstanceId);
        setAssignee(assignee);
        setCreateTime(new Date());
        setUpdateTime(getCreateTime());
    }

    public void handle(ProcessAction action) {
        refreshUpdateTime();
        setAction(action);
    }

    private void refreshUpdateTime() {
        setUpdateTime(new Date());
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public ProcessAssignee setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
        return this;
    }

    public String getAssignee() {
        return assignee;
    }

    public ProcessAssignee setAssignee(String assignee) {
        this.assignee = assignee;
        return this;
    }

    public ProcessAction getAction() {
        return action;
    }

    public ProcessAssignee setAction(ProcessAction action) {
        this.action = action;
        return this;
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

    public ProcessAssignee setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }
}