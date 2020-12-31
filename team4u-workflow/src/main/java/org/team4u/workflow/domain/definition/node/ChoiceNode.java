package org.team4u.workflow.domain.definition.node;

import org.team4u.workflow.domain.definition.ProcessNode;

/**
 * 选择器节点
 *
 * @author jay.wu
 */
public class ChoiceNode extends ProcessNode {

    public ChoiceNode(String nodeId,
                      String nodeName) {
        super(nodeId, nodeName);
    }
}