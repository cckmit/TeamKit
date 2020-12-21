package org.team4u.workflow.application.command;

import java.util.Map;

/**
 * 创建新流程实例命令
 *
 * @author jay.wu
 */
public class CreateProcessInstanceCommand extends AbstractHandleProcessInstanceCommand {
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


    public static final class Builder {
        private String operatorId;
        private String actionId;
        private String remark;
        private Map<String, Object> ext;
        private String processInstanceName;
        private String processDefinitionId;

        private Builder() {
        }

        public static Builder newBuilder() {
            return new Builder();
        }

        public Builder withOperatorId(String operatorId) {
            this.operatorId = operatorId;
            return this;
        }

        public Builder withActionId(String actionId) {
            this.actionId = actionId;
            return this;
        }

        public Builder withRemark(String remark) {
            this.remark = remark;
            return this;
        }

        public Builder withExt(Map<String, Object> ext) {
            this.ext = ext;
            return this;
        }

        public Builder withProcessInstanceName(String processInstanceName) {
            this.processInstanceName = processInstanceName;
            return this;
        }

        public Builder withProcessDefinitionId(String processDefinitionId) {
            this.processDefinitionId = processDefinitionId;
            return this;
        }

        public CreateProcessInstanceCommand build() {
            CreateProcessInstanceCommand createProcessInstanceCommand = new CreateProcessInstanceCommand();
            createProcessInstanceCommand.setOperatorId(operatorId);
            createProcessInstanceCommand.setActionId(actionId);
            createProcessInstanceCommand.setRemark(remark);
            createProcessInstanceCommand.setExt(ext);
            createProcessInstanceCommand.setProcessInstanceName(processInstanceName);
            createProcessInstanceCommand.setProcessDefinitionId(processDefinitionId);
            return createProcessInstanceCommand;
        }
    }
}