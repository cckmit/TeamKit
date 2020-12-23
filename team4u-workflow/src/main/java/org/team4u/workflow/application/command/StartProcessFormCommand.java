package org.team4u.workflow.application.command;

import org.team4u.workflow.domain.form.FormIndex;
import org.team4u.workflow.domain.instance.ProcessInstanceDetail;

import java.util.Map;

/**
 * 开始表单索引命令
 *
 * @author jay.wu
 */
public class StartProcessFormCommand extends AbstractHandleProcessInstanceCommand {

    private FormIndex formIndex;

    public FormIndex getProcessForm() {
        return formIndex;
    }

    public void setProcessForm(FormIndex formIndex) {
        this.formIndex = formIndex;
    }


    public static final class Builder {
        private String operatorId;
        private String actionId;
        private String remark;
        private Map<String, Object> ext;
        private ProcessInstanceDetail processInstanceDetail;
        private FormIndex formIndex;

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

        public Builder withProcessInstanceDetail(ProcessInstanceDetail processInstanceDetail) {
            this.processInstanceDetail = processInstanceDetail;
            return this;
        }

        public Builder withProcessForm(FormIndex formIndex) {
            this.formIndex = formIndex;
            return this;
        }

        public StartProcessFormCommand build() {
            StartProcessFormCommand startProcessFormCommand = new StartProcessFormCommand();
            startProcessFormCommand.setOperatorId(operatorId);
            startProcessFormCommand.setActionId(actionId);
            startProcessFormCommand.setRemark(remark);
            startProcessFormCommand.setExt(ext);
            startProcessFormCommand.setProcessInstanceDetail(processInstanceDetail);
            startProcessFormCommand.setProcessForm(formIndex);
            return startProcessFormCommand;
        }
    }
}