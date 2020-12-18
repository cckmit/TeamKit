package org.team4u.workflow.domain.emulator;

import org.team4u.ddd.domain.model.ValueObject;

import java.util.Map;

/**
 * 流程模拟器脚本步骤
 *
 * @author jay.wu
 */
public class ProcessEmulatorScriptStep extends ValueObject {
    /**
     * 当前操作人标识
     */
    private String operatorId;
    /**
     * 动作标识
     */
    private String actionId;
    /**
     * 附加信息
     */
    private Map<String, Object> ext;
    /**
     * 期望
     */
    private Expected expected;

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public Map<String, Object> getExt() {
        return ext;
    }

    public void setExt(Map<String, Object> ext) {
        this.ext = ext;
    }

    public Expected getExpected() {
        return expected;
    }

    public void setExpected(Expected expected) {
        this.expected = expected;
    }

    public static class Expected {

        /**
         * 期望流程节点标识
         */
        private String nodeId;

        public String getNodeId() {
            return nodeId;
        }

        public void setNodeId(String nodeId) {
            this.nodeId = nodeId;
        }
    }
}