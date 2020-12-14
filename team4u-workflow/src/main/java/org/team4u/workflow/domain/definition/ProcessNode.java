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
    private String nodeId;
    /**
     * 节点名称
     */
    private String nodeName;


    public String getNodeId() {
        return nodeId;
    }

    public ProcessNode setNodeId(String nodeId) {
        this.nodeId = nodeId;
        return this;
    }

    public String getNodeName() {
        return nodeName;
    }

    public ProcessNode setNodeName(String nodeName) {
        this.nodeName = nodeName;
        return this;
    }
}