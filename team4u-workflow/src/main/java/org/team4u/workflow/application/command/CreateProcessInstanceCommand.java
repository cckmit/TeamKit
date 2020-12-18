package org.team4u.workflow.application.command;

/**
 * 创建新流程实例命令
 *
 * @author jay.wu
 */
public class CreateProcessInstanceCommand extends HandleProcessInstanceCommand {
    /**
     * 流程实例名称
     */
    private String processInstanceName;
    /**
     * 流程定义标识
     */
    private String processDefinitionId;

    public String getProcessInstanceName() {
        return processInstanceName;
    }

    public CreateProcessInstanceCommand setProcessInstanceName(String processInstanceName) {
        this.processInstanceName = processInstanceName;
        return this;
    }

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public CreateProcessInstanceCommand setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
        return this;
    }
}
