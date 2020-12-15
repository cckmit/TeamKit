package org.team4u.workflow.domain.definition.node;

import org.team4u.workflow.domain.definition.ProcessNode;

/**
 * 静态节点
 *
 * @author jay.wu
 */
public class StaticNode extends ProcessNode {
    /**
     * 下一个流转节点
     */
    private final String nextNodeId;

    public StaticNode(String nodeId, String nodeName, String nextNodeId) {
        super(nodeId, nodeName);
        this.nextNodeId = nextNodeId;
    }

    public String getNextNodeId() {
        return nextNodeId;
    }
}