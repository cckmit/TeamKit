package org.team4u.workflow.domain.instance;

import org.team4u.ddd.domain.model.ValueObject;
import org.team4u.workflow.domain.definition.ProcessAction;
import org.team4u.workflow.domain.definition.ProcessNode;

import java.util.Date;

/**
 * 流程实例日志
 *
 * @author jay.wu
 */
public class ProcessInstanceLog extends ValueObject {

    /**
     * 流程实例标识
     */
    private String processInstanceId;
    /**
     * 当前节点
     */
    private ProcessNode node;
    /**
     * 流程动作
     */
    private ProcessAction action;
    /**
     * 下一个节点
     */
    private ProcessNode nextNode;
    /**
     * 备注
     */
    private String remark;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建时间
     */
    private Date createTime;

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public ProcessInstanceLog setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
        return this;
    }

    public ProcessNode getNode() {
        return node;
    }

    public ProcessInstanceLog setNode(ProcessNode node) {
        this.node = node;
        return this;
    }

    public ProcessAction getAction() {
        return action;
    }

    public ProcessInstanceLog setAction(ProcessAction action) {
        this.action = action;
        return this;
    }

    public ProcessNode getNextNode() {
        return nextNode;
    }

    public ProcessInstanceLog setNextNode(ProcessNode nextNode) {
        this.nextNode = nextNode;
        return this;
    }

    public String getRemark() {
        return remark;
    }

    public ProcessInstanceLog setRemark(String remark) {
        this.remark = remark;
        return this;
    }

    public String getCreateBy() {
        return createBy;
    }

    public ProcessInstanceLog setCreateBy(String createBy) {
        this.createBy = createBy;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public ProcessInstanceLog setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }
}
