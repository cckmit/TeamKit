package org.team4u.workflow.application.command;

import java.util.Map;

/**
 * 开始流程实例命令
 *
 * @author jay.wu
 */
public class StartProcessInstanceCommand extends AbstractHandleProcessInstanceCommand {
    /**
     * 流程实例标识
     */
    private String processInstanceId;

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public StartProcessInstanceCommand setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
        return this;
    }

    public static final class Builder {
        private String operatorId;
        private String actionId;
        private String remark;
        private Map<String, Object> ext;
        private String processInstanceId;

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

        public StartProcessInstanceCommand build() {
            StartProcessInstanceCommand startProcessInstanceCommand = new StartProcessInstanceCommand();
            startProcessInstanceCommand.setOperatorId(operatorId);
            startProcessInstanceCommand.setActionId(actionId);
            startProcessInstanceCommand.setRemark(remark);
            startProcessInstanceCommand.setExt(ext);
            startProcessInstanceCommand.setProcessInstanceId(processInstanceId);
            return startProcessInstanceCommand;
        }
    }
}
