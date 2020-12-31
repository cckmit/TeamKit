package org.team4u.workflow.domain.definition.node;

import java.util.List;

/**
 * 简单动作选择器节点
 *
 * @author jay.wu
 */
public class SimpleActionChoiceNode extends ActionChoiceNode implements TransientNode {

    public SimpleActionChoiceNode(String nodeId, String nodeName, List<ActionNode> actionNodes) {
        super(nodeId, nodeName, actionNodes);
    }
}