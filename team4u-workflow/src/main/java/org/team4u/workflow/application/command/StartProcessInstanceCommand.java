package org.team4u.workflow.application.command;

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
    private String operatorId;
    /**
     * 触发动作标识
     */
    private String actionId;
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

    public String getOperatorId() {
        return operatorId;
    }

    public StartProcessInstanceCommand setOperatorId(String operatorId) {
        this.operatorId = operatorId;
        return this;
    }

    public String getActionId() {
        return actionId;
    }

    public StartProcessInstanceCommand setActionId(String actionId) {
        this.actionId = actionId;
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
