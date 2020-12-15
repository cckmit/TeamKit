package org.team4u.workflow.domain.definition.node;

import org.team4u.workflow.domain.definition.ProcessAction;

import java.util.List;

/**
 * 动作选择器节点
 *
 * @author jay.wu
 */
public class ActionChoiceNode extends ChoiceNode {

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

    public String nextNodeId(ProcessAction action) {
        if (actionNodes == null) {
            return null;
        }

        return actionNodes.stream()
                .filter(it -> it.actionId.equals(action.getActionId()))
                .findFirst()
                .map(ActionNode::getNodeId)
                .orElse(null);
    }

    public List<ActionNode> getActionNodes() {
        return actionNodes;
    }

    /**
     * 动作节点映射关系
     */
    public static class ActionNode {

        private final String actionId;
        private final String nodeId;

        public ActionNode(String actionId, String nodeId) {
            this.actionId = actionId;
            this.nodeId = nodeId;
        }

        public String getActionId() {
            return actionId;
        }

        public String getNodeId() {
            return nodeId;
        }

        @Override
        public String toString() {
            return actionId + "->" + nodeId;
        }
    }
}
