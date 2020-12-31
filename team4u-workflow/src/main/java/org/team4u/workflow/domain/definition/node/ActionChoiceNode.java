package org.team4u.workflow.domain.definition.node;

import org.team4u.workflow.domain.definition.ProcessAction;
import org.team4u.workflow.domain.instance.exception.ProcessActionNodeNotExistException;

import java.util.List;

/**
 * 抽象动作选择器节点
 *
 * @author jay.wu
 */
public abstract class ActionChoiceNode extends ChoiceNode {

    /**
     * 动作节点映射集合
     */
    private final List<ActionNode> actionNodes;

    public ActionChoiceNode(String nodeId,
                            String nodeName,
                            List<ActionNode> actionNodes) {
        super(nodeId, nodeName);
        this.actionNodes = actionNodes;
    }

    /**
     * 获取下一个节点
     *
     * @param action 当前动作
     * @return 下一个节点，若不存在则抛出异常
     */
    public String nextNodeId(ProcessAction action) throws ProcessActionNodeNotExistException {
        if (actionNodes == null) {
            throw new ProcessActionNodeNotExistException(action.getActionId());
        }

        return actionNodes.stream()
                .filter(it -> it.actionId.equals(action.getActionId()))
                .findFirst()
                .map(ActionNode::getNextNodeId)
                .orElseThrow(() -> new ProcessActionNodeNotExistException(action.getActionId()));
    }

    public List<ActionNode> getActionNodes() {
        return actionNodes;
    }

    /**
     * 动作节点映射关系
     */
    public static class ActionNode {

        private final String actionId;
        private final String nextNodeId;

        public ActionNode(String actionId, String nextNodeId) {
            this.actionId = actionId;
            this.nextNodeId = nextNodeId;
        }

        public String getActionId() {
            return actionId;
        }

        public String getNextNodeId() {
            return nextNodeId;
        }

        @Override
        public String toString() {
            return actionId + "->" + nextNodeId;
        }
    }
}
