package org.team4u.workflow.domain.instance.node.handler;

import cn.hutool.core.util.ObjectUtil;
import org.team4u.workflow.domain.definition.ProcessAction;
import org.team4u.workflow.domain.definition.ProcessDefinition;
import org.team4u.workflow.domain.definition.ProcessNode;
import org.team4u.workflow.domain.instance.ProcessInstance;
import org.team4u.workflow.domain.instance.exception.ProcessInstanceNotExistException;

import java.util.HashMap;
import java.util.Map;

/**
 * 流程节点处理器上下文
 *
 * @author jay.wu
 */
public class ProcessNodeHandlerContext {

    private ProcessNode node;
    private ProcessInstance instance;
    private ProcessDefinition definition;

    private ProcessAction action;
    private String operatorId;
    private String remark;

    private Map<String, Object> ext;

    public ProcessInstance getInstance() {
        return instance;
    }

    public void setInstance(ProcessInstance instance) {
        if (instance == null) {
            throw new ProcessInstanceNotExistException("context instance is null");
        }
        this.instance = instance;
    }

    public ProcessDefinition getDefinition() {
        return definition;
    }

    public void setDefinition(ProcessDefinition definition) {
        this.definition = definition;
    }

    @SuppressWarnings("unchecked")
    public <T extends ProcessNode> T getNode() {
        return (T) node;
    }

    public ProcessNodeHandlerContext setNode(ProcessNode node) {
        this.node = node;
        return this;
    }

    public ProcessAction getAction() {
        return action;
    }

    public void setAction(ProcessAction action) {
        this.action = action;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Map<String, Object> getExt() {
        return ext;
    }

    public void setExt(Map<String, Object> ext) {
        this.ext = ext;
    }

    @SuppressWarnings("unchecked")
    public <T> T ext(String key) {
        return (T) getExt().get(key);
    }

    public ProcessNodeHandlerContext ext(String key, Object value) {
        getExt().put(key, value);
        return this;
    }

    public static final class Builder {
        private ProcessInstance instance;
        private ProcessDefinition definition;
        private ProcessAction action;
        private String operatorId;
        private String remark;
        private Map<String, Object> ext;
        private ProcessNode node;

        private Builder() {
        }

        public static Builder create() {
            return new Builder();
        }

        public Builder withInstance(ProcessInstance instance) {
            this.instance = instance;
            return this;
        }

        public Builder withDefinition(ProcessDefinition definition) {
            this.definition = definition;
            return this;
        }

        public Builder withAction(ProcessAction action) {
            this.action = action;
            return this;
        }

        public Builder withOperatorId(String operatorId) {
            this.operatorId = operatorId;
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

        public Builder withNode(ProcessNode node) {
            this.node = node;
            return this;
        }

        public ProcessNodeHandlerContext build() {
            ProcessNodeHandlerContext processNodeHandlerContext = new ProcessNodeHandlerContext();
            processNodeHandlerContext.setInstance(instance);
            processNodeHandlerContext.setDefinition(definition);
            processNodeHandlerContext.setAction(action);
            processNodeHandlerContext.setOperatorId(operatorId);
            processNodeHandlerContext.setRemark(remark);
            processNodeHandlerContext.setExt(ObjectUtil.defaultIfNull(ext, new HashMap<>()));
            processNodeHandlerContext.setNode(node);
            return processNodeHandlerContext;
        }
    }
}
