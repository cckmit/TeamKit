package org.team4u.workflow.domain.definition;

import org.team4u.ddd.domain.model.ValueObject;

/**
 * 流程节点
 *
 * @author jay.wu
 */
public abstract class ProcessNode extends ValueObject {
    /**
     * 节点标识
     */
    private final String nodeId;
    /**
     * 节点名称
     */
    private final String nodeName;

    public ProcessNode(String nodeId, String nodeName) {
        this.nodeId = nodeId;
        this.nodeName = nodeName;
    }

    public String getNodeId() {
        return nodeId;
    }


    public String getNodeName() {
        return nodeName;
    }

    @Override
    public String toString() {
        return nodeId;
    }
}