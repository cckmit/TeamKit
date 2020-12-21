package org.team4u.workflow.application.command;

import org.team4u.workflow.domain.form.ProcessForm;

import java.util.Map;

/**
 * 创建流程表单命令
 *
 * @author jay.wu
 */
public class CreateProcessFormCommand extends CreateProcessInstanceCommand {

    private ProcessForm processForm;

    public ProcessForm getProcessForm() {
        return processForm;
    }

    public void setProcessForm(ProcessForm processForm) {
        this.processForm = processForm;
    }


    public static final class Builder {
        private ProcessForm processForm;
        private String processInstanceName;
        private String processDefinitionId;
        private String operatorId;
        private String actionId;
        private String remark;
        private Map<String, Object> ext;

        private Builder() {
        }

        public static Builder newBuilder() {
            return new Builder();
        }

        public Builder withProcessForm(ProcessForm processForm) {
            this.processForm = processForm;
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

        public CreateProcessFormCommand build() {
            CreateProcessFormCommand createProcessFormCommand = new CreateProcessFormCommand();
            createProcessFormCommand.setProcessForm(processForm);
            createProcessFormCommand.setProcessInstanceName(processInstanceName);
            createProcessFormCommand.setProcessDefinitionId(processDefinitionId);
            createProcessFormCommand.setOperatorId(operatorId);
            createProcessFormCommand.setActionId(actionId);
            createProcessFormCommand.setRemark(remark);
            createProcessFormCommand.setExt(ext);
            return createProcessFormCommand;
        }
    }
}