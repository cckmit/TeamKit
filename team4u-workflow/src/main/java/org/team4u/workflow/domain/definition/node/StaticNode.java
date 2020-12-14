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
    private String nextNodeId;

    public String getNextNodeId() {
        return nextNodeId;
    }

    public StaticNode setNextNodeId(String nextNodeId) {
        this.nextNodeId = nextNodeId;
        return this;
    }
}