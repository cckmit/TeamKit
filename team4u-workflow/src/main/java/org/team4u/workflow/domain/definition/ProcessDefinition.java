package org.team4u.workflow.domain.definition;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import org.team4u.base.error.DataNotExistException;
import org.team4u.ddd.domain.model.AggregateRoot;
import org.team4u.workflow.domain.definition.node.StaticNode;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 流程定义
 *
 * @author jay.wu
 */
public class ProcessDefinition extends AggregateRoot {

    /**
     * 流程定义标识
     */
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

    public ProcessAction actionOf(String actionId) {
        return actions.stream()
                .filter(it -> StrUtil.equals(it.getActionId(), actionId))
                .findFirst()
                .orElseThrow(() -> new DataNotExistException("action is null|actionId=" + actionId));
    }

    public List<ProcessAction> actionsOf(List<String> permissions) {
        return actions.stream()
                .filter(it -> CollUtil.contains(permissions, it.getRequiredPermissions()))
                .collect(Collectors.toList());
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

    @Override
    public String toString() {
        return processDefinitionId;
    }
}