package org.team4u.workflow.domain.instance.node.handler;

import org.team4u.base.lang.IdObject;
import org.team4u.workflow.domain.definition.ProcessAction;
import org.team4u.workflow.domain.definition.ProcessDefinition;
import org.team4u.workflow.domain.definition.ProcessNode;
import org.team4u.workflow.domain.instance.ProcessInstance;

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
     * @return 下一个流程节点
     */
    ProcessNode handle(Context context);

    class Context {
        private final ProcessInstance instance;
        private final ProcessDefinition definition;
        private final ProcessAction action;
        private final String operator;
        private final String remark;
        private ProcessNode node;

        public Context(ProcessInstance instance,
                       ProcessDefinition definition,
                       ProcessAction action,
                       String operator,
                       String remark) {
            this.instance = instance;
            this.definition = definition;
            this.action = action;
            this.operator = operator;
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

        public String getOperator() {
            return operator;
        }

        public String getRemark() {
            return remark;
        }
    }
}