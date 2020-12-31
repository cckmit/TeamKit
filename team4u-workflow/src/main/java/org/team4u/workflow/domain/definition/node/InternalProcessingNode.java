package org.team4u.workflow.domain.definition.node;

import org.team4u.workflow.domain.definition.ProcessNode;

/**
 * 内部处理节点
 *
 * @author jay.wu
 */
public class InternalProcessingNode extends ProcessNode implements TransientNode {
    /**
     * 下一个流转节点
     */
    private final String nextNodeId;

    public InternalProcessingNode(String nodeId, String nodeName, String nextNodeId) {
        super(nodeId, nodeName);
        this.nextNodeId = nextNodeId;
    }

    public String getNextNodeId() {
        return nextNodeId;
    }
}