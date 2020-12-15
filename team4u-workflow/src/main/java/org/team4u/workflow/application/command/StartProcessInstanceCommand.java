package org.team4u.workflow.application.command;

import org.team4u.workflow.domain.definition.ProcessAction;

/**
 * 开始流程实例命令
 *
 * @author jay.wu
 */
public class StartProcessInstanceCommand {
    /**
     * 流程实例标识
     */
    private String processInstanceId;
    /**
     * 流程实例名称
     */
    private String processInstanceName;
    /**
     * 流程定义标识
     */
    private String processDefinitionId;
    /**
     * 当前处理人
     */
    private String operator;
    /**
     * 触发动作
     */
    private ProcessAction action;
    /**
     * 备注
     */
    private String remark;

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public StartProcessInstanceCommand setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
        return this;
    }

    public String getProcessInstanceName() {
        return processInstanceName;
    }

    public StartProcessInstanceCommand setProcessInstanceName(String processInstanceName) {
        this.processInstanceName = processInstanceName;
        return this;
    }

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public StartProcessInstanceCommand setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
        return this;
    }

    public String getOperator() {
        return operator;
    }

    public StartProcessInstanceCommand setOperator(String operator) {
        this.operator = operator;
        return this;
    }

    public ProcessAction getAction() {
        return action;
    }

    public StartProcessInstanceCommand setAction(ProcessAction action) {
        this.action = action;
        return this;
    }

    public String getRemark() {
        return remark;
    }

    public StartProcessInstanceCommand setRemark(String remark) {
        this.remark = remark;
        return this;
    }
}
