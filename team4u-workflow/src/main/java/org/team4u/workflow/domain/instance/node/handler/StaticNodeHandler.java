package org.team4u.workflow.domain.instance.node.handler;

import org.team4u.workflow.domain.definition.ProcessNode;
import org.team4u.workflow.domain.definition.node.StaticNode;

/**
 * 静态节点处理器
 *
 * @author jay.wu
 */
public class StaticNodeHandler implements ProcessNodeHandler {

    @Override
    public ProcessNode handle(Context context) {
        StaticNode node = context.getNode();

        context.getInstance().changeCurrentNodeTo(
                context.getAction(),
                context.getNode(),
                context.getOperator(),
                context.getRemark()
        );

        return context.getDefinition().processNodeOf(node.getNextNodeId());
    }

    @Override
    public String id() {
        return StaticNode.class.getSimpleName();
    }
}