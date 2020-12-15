package org.team4u.workflow.domain.instance.node.handler;

import org.team4u.base.lang.IdObject;
import org.team4u.workflow.domain.definition.ProcessAction;
import org.team4u.workflow.domain.definition.ProcessDefinition;
import org.team4u.workflow.domain.definition.ProcessNode;
import org.team4u.workflow.domain.instance.ProcessInstance;

import java.util.List;

/**
 * 流程节点处理器
 *
 * @author jay.wu
 */
public interface ProcessNodeHandler extends IdObject<String> {

    /**
     * 处理节点
     *
     * @param context 上下文
     * @return 下一个流程节点标识
     */
    String handle(Context context);

    class Context {
        private final ProcessInstance instance;
        private final ProcessDefinition definition;
        private final ProcessAction action;
        private final String operatorId;
        private final List<String> operatorPermissions;
        private final String remark;
        private ProcessNode node;

        public Context(ProcessInstance instance,
                       ProcessDefinition definition,
                       ProcessAction action,
                       String operatorId,
                       List<String> operatorPermissions, String remark) {
            this.instance = instance;
            this.definition = definition;
            this.action = action;
            this.operatorId = operatorId;
            this.operatorPermissions = operatorPermissions;
            this.remark = remark;
        }

        public ProcessInstance getInstance() {
            return instance;
        }

        public ProcessDefinition getDefinition() {
            return definition;
        }

        @SuppressWarnings("unchecked")
        public <T extends ProcessNode> T getNode() {
            return (T) node;
        }

        public Context setNode(ProcessNode node) {
            this.node = node;
            return this;
        }

        public ProcessAction getAction() {
            return action;
        }

        public String getOperatorId() {
            return operatorId;
        }

        public List<String> getOperatorPermissions() {
            return operatorPermissions;
        }

        public String getRemark() {
            return remark;
        }
    }
}