package org.team4u.workflow.application.command;

import org.team4u.workflow.domain.form.FormIndex;

import java.util.Map;

/**
 * 开始表单索引命令
 *
 * @author jay.wu
 */
public class StartProcessFormCommand extends AbstractHandleProcessInstanceCommand {
    /**
     * 流程动作标识
     */
    private String actionId;
    /**
     * 表单索引
     */
    private FormIndex formIndex;
    /**
     * 附加信息
     */
    private Map<String, Object> ext;

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public FormIndex getFormIndex() {
        return formIndex;
    }

    public void setFormIndex(FormIndex formIndex) {
        this.formIndex = formIndex;
    }

    public Map<String, Object> getExt() {
        return ext;
    }

    public void setExt(Map<String, Object> ext) {
        this.ext = ext;
    }

    public static final class Builder {
        private String operatorId;
        private String actionId;
        private String remark;
        private Map<String, Object> ext;
        private String processInstanceId;
        private Object processInstanceDetail;
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

        public Builder withProcessInstanceId(String processInstanceId) {
            this.processInstanceId = processInstanceId;
            return this;
        }

        public Builder withProcessInstanceDetail(Object processInstanceDetail) {
            this.processInstanceDetail = processInstanceDetail;
            return this;
        }

        public Builder withFormIndex(FormIndex formIndex) {
            this.formIndex = formIndex;
            return this;
        }

        public StartProcessFormCommand build() {
            StartProcessFormCommand startProcessFormCommand = new StartProcessFormCommand();
            startProcessFormCommand.setOperatorId(operatorId);
            startProcessFormCommand.setActionId(actionId);
            startProcessFormCommand.setRemark(remark);
            startProcessFormCommand.setExt(ext);
            startProcessFormCommand.setProcessInstanceId(processInstanceId);
            startProcessFormCommand.setProcessInstanceDetail(processInstanceDetail);
            startProcessFormCommand.setFormIndex(formIndex);
            return startProcessFormCommand;
        }
    }
}