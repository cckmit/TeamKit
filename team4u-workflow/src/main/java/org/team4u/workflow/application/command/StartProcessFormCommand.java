package org.team4u.workflow.application.command;

import org.team4u.workflow.domain.form.ProcessForm;

import java.util.Map;

/**
 * 开始流程表单命令
 *
 * @author jay.wu
 */
public class StartProcessFormCommand extends AbstractHandleProcessInstanceCommand {

    private ProcessForm processForm;

    public ProcessForm getProcessForm() {
        return processForm;
    }

    public void setProcessForm(ProcessForm processForm) {
        this.processForm = processForm;
    }


    public static final class Builder {
        private String operatorId;
        private String actionId;
        private String remark;
        private Map<String, Object> ext;
        private ProcessForm processForm;
        private String processInstanceId;

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

        public Builder withProcessForm(ProcessForm processForm) {
            this.processForm = processForm;
            return this;
        }

        public StartProcessFormCommand build() {
            StartProcessFormCommand startProcessFormCommand = new StartProcessFormCommand();
            startProcessFormCommand.setOperatorId(operatorId);
            startProcessFormCommand.setActionId(actionId);
            startProcessFormCommand.setRemark(remark);
            startProcessFormCommand.setExt(ext);
            startProcessFormCommand.setProcessForm(processForm);
            return startProcessFormCommand;
        }
    }
}