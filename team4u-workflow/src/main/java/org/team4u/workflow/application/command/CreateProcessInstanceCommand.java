package org.team4u.workflow.application.command;

import org.team4u.workflow.domain.instance.ProcessInstanceDetail;

import java.util.Map;

/**
 * 创建新流程实例命令
 *
 * @author jay.wu
 */
public class CreateProcessInstanceCommand extends AbstractHandleProcessInstanceCommand {
    /**
     * 自定义流程实例标识（可选）
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

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getProcessInstanceName() {
        return processInstanceName;
    }

    public void setProcessInstanceName(String processInstanceName) {
        this.processInstanceName = processInstanceName;
    }

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    public static final class Builder {
        private String operatorId;
        private String actionId;
        private String remark;
        private Map<String, Object> ext;
        private String processInstanceId;
        private String processInstanceName;
        private String processDefinitionId;
        private ProcessInstanceDetail processInstanceDetail;

        private Builder() {
        }

        public static Builder create() {
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

        public Builder withProcessInstanceId(String processInstanceId) {
            this.processInstanceId = processInstanceId;
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

        public Builder withProcessInstanceDetail(ProcessInstanceDetail processInstanceDetail) {
            this.processInstanceDetail = processInstanceDetail;
            return this;
        }

        public CreateProcessInstanceCommand build() {
            CreateProcessInstanceCommand createProcessInstanceCommand = new CreateProcessInstanceCommand();
            createProcessInstanceCommand.setOperatorId(operatorId);
            createProcessInstanceCommand.setActionId(actionId);
            createProcessInstanceCommand.setRemark(remark);
            createProcessInstanceCommand.setExt(ext);
            createProcessInstanceCommand.setProcessInstanceId(processInstanceId);
            createProcessInstanceCommand.setProcessInstanceName(processInstanceName);
            createProcessInstanceCommand.setProcessDefinitionId(processDefinitionId);
            createProcessInstanceCommand.setProcessInstanceDetail(processInstanceDetail);
            return createProcessInstanceCommand;
        }
    }
}