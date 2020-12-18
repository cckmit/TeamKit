package org.team4u.workflow.application.command;

import java.util.Map;

/**
 * 处理流程实例命令
 *
 * @author jay.wu
 */
public abstract class HandleProcessInstanceCommand {
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

    public String getOperatorId() {
        return operatorId;
    }

    public HandleProcessInstanceCommand setOperatorId(String operatorId) {
        this.operatorId = operatorId;
        return this;
    }

    public String getActionId() {
        return actionId;
    }

    public HandleProcessInstanceCommand setActionId(String actionId) {
        this.actionId = actionId;
        return this;
    }

    public String getRemark() {
        return remark;
    }

    public HandleProcessInstanceCommand setRemark(String remark) {
        this.remark = remark;
        return this;
    }

    public Map<String, Object> getExt() {
        return ext;
    }

    public HandleProcessInstanceCommand setExt(Map<String, Object> ext) {
        this.ext = ext;
        return this;
    }
}
