package org.team4u.workflow.application.command;

import org.team4u.workflow.domain.form.FormIndex;
import org.team4u.workflow.domain.instance.ProcessInstanceDetail;

/**
 * 创建表单索引命令
 *
 * @author jay.wu
 */
public class CreateProcessFormCommand extends CreateProcessInstanceCommand {

    private FormIndex formIndex;

    public FormIndex getProcessForm() {
        return formIndex;
    }

    public void setFormIndex(FormIndex formIndex) {
        this.formIndex = formIndex;
    }

    public static final class Builder {
        private String operatorId;
        private String remark;
        private FormIndex formIndex;
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

        public Builder withRemark(String remark) {
            this.remark = remark;
            return this;
        }

        public Builder withFormIndex(FormIndex formIndex) {
            this.formIndex = formIndex;
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

        public CreateProcessFormCommand build() {
            CreateProcessFormCommand createProcessFormCommand = new CreateProcessFormCommand();
            createProcessFormCommand.setOperatorId(operatorId);
            createProcessFormCommand.setRemark(remark);
            createProcessFormCommand.setFormIndex(formIndex);
            createProcessFormCommand.setProcessInstanceName(processInstanceName);
            createProcessFormCommand.setProcessDefinitionId(processDefinitionId);
            createProcessFormCommand.setProcessInstanceId(processInstanceId);
            createProcessFormCommand.setProcessInstanceDetail(processInstanceDetail);
            return createProcessFormCommand;
        }
    }
}