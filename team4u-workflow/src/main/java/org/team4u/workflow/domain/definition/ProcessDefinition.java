package org.team4u.workflow.domain.definition;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import org.team4u.ddd.domain.model.AggregateRoot;
import org.team4u.workflow.domain.definition.exception.ProcessActionNotExistException;

import java.util.Collections;
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
     * 流程定义名称
     */
    private final String processDefinitionName;
    /**
     * 动作集合
     */
    private List<ProcessAction> actions;
    /**
     * 节点结合
     */
    private List<ProcessNode> nodes;

    public ProcessDefinition(ProcessDefinitionId processDefinitionId,
                             String processDefinitionName,
                             List<ProcessAction> actions,
                             List<ProcessNode> nodes) {
        this.processDefinitionId = processDefinitionId;
        this.processDefinitionName = processDefinitionName;
        this.actions = ObjectUtil.defaultIfNull(actions, Collections.emptyList());
        this.nodes = ObjectUtil.defaultIfNull(nodes, Collections.emptyList());
    }

    /**
     * 获取根流程节点
     *
     * @return 理财节点
     */
    public ProcessNode rootNode() {
        return CollUtil.getFirst(nodes);
    }

    /**
     * 获取流程节点
     *
     * @param nodeId 节点标识
     * @return 流程节点
     */
    @SuppressWarnings("unchecked")
    public <T extends ProcessNode> T processNodeOf(String nodeId) {
        if (nodeId == null) {
            return null;
        }

        return (T) nodes.stream()
                .filter(it -> StrUtil.equals(it.getNodeId(), nodeId))
                .findFirst()
                .orElse(null);
    }

    /**
     * 获取有效的流程动作
     *
     * @param actionId 动作标识
     * @return 流程动作，获取失败则抛出ProcessActionNotExistException
     */
    public ProcessAction availableActionOf(String actionId) {
        return actions.stream()
                .filter(it -> StrUtil.equals(it.getActionId(), actionId))
                .findFirst()
                .orElseThrow(() -> new ProcessActionNotExistException(actionId));
    }

    public ProcessDefinitionId getProcessDefinitionId() {
        return processDefinitionId;
    }

    public String getProcessDefinitionName() {
        return processDefinitionName;
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