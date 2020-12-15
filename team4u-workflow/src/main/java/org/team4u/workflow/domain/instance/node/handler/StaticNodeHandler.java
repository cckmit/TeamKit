package org.team4u.workflow.domain.instance.node.handler;

import org.team4u.workflow.domain.definition.node.StaticNode;

/**
 * 静态节点处理器
 *
 * @author jay.wu
 */
public class StaticNodeHandler extends AbstractStaticProcessNodeHandler {

    @Override
    public String id() {
        return StaticNode.class.getSimpleName();
    }

    @Override
    protected void internalHandle(Context context) {

    }
}