package org.team4u.workflow.domain.definition.node;

import org.team4u.workflow.domain.definition.ProcessNode;

/**
 * 瞬时流程节点
 * <p>
 * 该节点类型不做保存、停留
 *
 * @author jay.wu
 */
public class TransientNode extends ProcessNode {
    public TransientNode(String nodeId, String nodeName) {
        super(nodeId, nodeName);
    }
}