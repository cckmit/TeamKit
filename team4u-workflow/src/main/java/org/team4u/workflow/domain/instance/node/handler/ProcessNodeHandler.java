package org.team4u.workflow.domain.instance.node.handler;

import cn.hutool.core.util.ObjectUtil;
import org.team4u.base.lang.IdObject;
import org.team4u.workflow.domain.definition.ProcessAction;
import org.team4u.workflow.domain.definition.ProcessDefinition;
import org.team4u.workflow.domain.definition.ProcessNode;
import org.team4u.workflow.domain.instance.ProcessInstance;

import java.util.HashMap;
import java.util.Map;

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
        private final String remark;
        private final Map<String, Object> ext;
        private ProcessNode node;

        public Context(ProcessInstance instance,
                       ProcessDefinition definition,
                       ProcessAction action,
                       String operatorId,
                       String remark, Map<String, Object> ext) {
            this.instance = instance;
            this.definition = definition;
            this.action = action;
            this.operatorId = operatorId;
            this.remark = remark;
            this.ext = ObjectUtil.defaultIfNull(ext, new HashMap<>());
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

        public String getRemark() {
            return remark;
        }

        public Map<String, Object> getExt() {
            return ext;
        }

        @SuppressWarnings("unchecked")
        public <T> T ext(String key) {
            return (T) getExt().get(key);
        }

        public Context putExt(String key, Object value) {
            getExt().put(key, value);
            return this;
        }
    }
}