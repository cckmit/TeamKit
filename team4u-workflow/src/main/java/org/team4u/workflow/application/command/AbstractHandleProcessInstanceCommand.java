package org.team4u.workflow.application.command;

import org.team4u.workflow.domain.instance.ProcessInstanceDetail;

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
    private ProcessInstanceDetail processInstanceDetail;

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

    public ProcessInstanceDetail getProcessInstanceDetail() {
        return processInstanceDetail;
    }

    public void setProcessInstanceDetail(ProcessInstanceDetail processInstanceDetail) {
        this.processInstanceDetail = processInstanceDetail;
    }
}