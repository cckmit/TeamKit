package org.team4u.workflow.application.command;

import java.util.Map;

/**
 * 开始流程实例命令
 *
 * @author jay.wu
 */
public class StartProcessInstanceCommand {
    /**
     * 流程实例标识（非新建时必填）
     */
    private String processInstanceId;
    /**
     * 流程实例名称（新建时必填）
     */
    private String processInstanceName;
    /**
     * 流程定义标识（新建时必填）
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
    /**
     * 附加信息
     */
    private Map<String, Object> ext;

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

    public Map<String, Object> getExt() {
        return ext;
    }

    public StartProcessInstanceCommand setExt(Map<String, Object> ext) {
        this.ext = ext;
        return this;
    }
}
