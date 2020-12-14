package org.team4u.workflow.domain.instance.node.handler;

import org.team4u.workflow.domain.definition.ProcessNode;
import org.team4u.workflow.domain.definition.node.ActionChoiceNode;

/**
 * 动作选择器节点处理器
 *
 * @author jay.wu
 */
public class ActionChoiceNodeHandler implements ProcessNodeHandler {

    @Override
    public ProcessNode handle(Context context) {
        ActionChoiceNode node = context.getNode();
        return node.nextNodeOfAction(context.getAction());
    }

    @Override
    public String id() {
        return ActionChoiceNode.class.getSimpleName();
    }
}