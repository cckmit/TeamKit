package org.team4u.workflow.domain.definition;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import org.team4u.base.error.DataNotExistException;
import org.team4u.ddd.domain.model.AggregateRoot;
import org.team4u.workflow.domain.definition.node.StaticNode;

import java.util.List;

/**
 * 流程定义
 *
 * @author jay.wu
 */
public class ProcessDefinition extends AggregateRoot {

    /**
     * 流程定义标识
     */
    private final ProcessDefinitionId processDefinitionId;
    /**
     * 动作集合
     */
    private List<ProcessAction> actions;
    /**
     * 节点结合
     */
    private List<ProcessNode> nodes;

    public ProcessDefinition(ProcessDefinitionId processDefinitionId,
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
    public StaticNode rootNode() {
        return (StaticNode) CollUtil.getFirst(nodes);
    }

    @SuppressWarnings("unchecked")
    public <T extends ProcessNode> T processNodeOf(String nodeId) {
        if (nodes == null || StrUtil.isBlank(nodeId)) {
            return null;
        }

        return (T) nodes.stream()
                .filter(it -> StrUtil.equals(it.getNodeId(), nodeId))
                .findFirst()
                .orElse(null);
    }

    /**
     * 获取有效流程动作
     *
     * @param actionId 动作标识
     * @return 流程动作，若获取失败则抛出DataNotExistException
     */
    public ProcessAction availableActionOf(String actionId) {
        if (StrUtil.isBlank(actionId)) {
            throw new DataNotExistException("action is null|actionId=" + actionId);
        }

        return actions.stream()
                .filter(it -> StrUtil.equals(it.getActionId(), actionId))
                .findFirst()
                .orElseThrow(() -> new DataNotExistException("action is null|actionId=" + actionId));
    }

    public ProcessDefinitionId getProcessDefinitionId() {
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

    @Override
    public String toString() {
        return processDefinitionId.toString();
    }
}