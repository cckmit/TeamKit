package org.team4u.workflow.domain.definition.node;

import org.team4u.workflow.domain.definition.ProcessAction;
import org.team4u.workflow.domain.definition.ProcessNode;

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
    private List<ActionNode> actionNodes;

    public ProcessNode nextNodeOfAction(ProcessAction action) {
        if (actionNodes == null) {
            return null;
        }

        return actionNodes.stream()
                .filter(it -> it.action.equals(action))
                .findFirst()
                .map(ActionNode::getNode)
                .orElse(null);
    }

    public List<ActionNode> getActionNodes() {
        return actionNodes;
    }

    public ActionChoiceNode setActionNodes(List<ActionNode> actionNodes) {
        this.actionNodes = actionNodes;
        return this;
    }

    /**
     * 动作节点映射关系
     */
    public static class ActionNode {

        private final ProcessAction action;
        private final ProcessNode node;

        public ActionNode(ProcessAction action, ProcessNode node) {
            this.action = action;
            this.node = node;
        }

        public ProcessAction getAction() {
            return action;
        }

        public ProcessNode getNode() {
            return node;
        }
    }
}
