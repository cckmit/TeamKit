package org.team4u.workflow.domain.definition;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import org.team4u.ddd.domain.model.AggregateRoot;

import java.util.List;

/**
 * 流程定义
 *
 * @author jay.wu
 */
public class ProcessDefinition extends AggregateRoot {

    private final String processDefinitionId;

    /**
     * 动作集合
     */
    private List<ProcessAction> actions;
    /**
     * 节点结合
     */
    private List<ProcessNode> nodes;

    public ProcessDefinition(String processDefinitionId,
                             List<ProcessAction> actions,
                             List<ProcessNode> nodes) {
        this.processDefinitionId = processDefinitionId;
        this.actions = actions;
        this.nodes = nodes;
    }

    /**
     * 获取根流程节点
     *
     * @return 理财节点
     */
    public ProcessNode rootProcessNode() {
        return CollUtil.getFirst(nodes);
    }

    public ProcessNode processNodeOf(String nodeId) {
        if (nodes == null) {
            return null;
        }

        return nodes.stream()
                .filter(it -> StrUtil.equals(it.getNodeId(), nodeId))
                .findFirst()
                .orElse(null);
    }

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public List<ProcessAction> getActions() {
        return actions;
    }

    public ProcessDefinition setActions(List<ProcessAction> actions) {
        this.actions = actions;
        return this;
    }

    public List<ProcessNode> getNodes() {
        return nodes;
    }

    public ProcessDefinition setNodes(List<ProcessNode> nodes) {
        this.nodes = nodes;
        return this;
    }
}