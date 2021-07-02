package org.team4u.workflow.application.command;

/**
 * 处理流程实例命令
 *
 * @author jay.wu
 */
public abstract class AbstractHandleProcessInstanceCommand {
    /**
     * 当前处理人
     */
    private String operatorId;
    /**
     * 备注
     */
    private String remark;
    /**
     * 流程实例明细
     */
    private Object processInstanceDetail;

    public String getOperatorId() {
        return operatorId;
    }

    public AbstractHandleProcessInstanceCommand setOperatorId(String operatorId) {
        this.operatorId = operatorId;
        return this;
    }

    public String getRemark() {
        return remark;
    }

    public AbstractHandleProcessInstanceCommand setRemark(String remark) {
        this.remark = remark;
        return this;
    }

    public Object getProcessInstanceDetail() {
        return processInstanceDetail;
    }

    public void setProcessInstanceDetail(Object processInstanceDetail) {
        this.processInstanceDetail = processInstanceDetail;
    }
}