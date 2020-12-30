package org.team4u.workflow.domain.instance.node.handler;

import org.team4u.workflow.domain.definition.node.ActionChoiceNode;
import org.team4u.workflow.domain.instance.exception.ProcessActionNodeNotExistException;

/**
 * 动作选择器节点处理器
 *
 * @author jay.wu
 */
public class ActionChoiceNodeHandler implements ProcessNodeHandler {

    @Override
    public String handle(ProcessNodeHandlerContext context) {
        return findNextNode(context);
    }

    private String findNextNode(ProcessNodeHandlerContext context) throws ProcessActionNodeNotExistException {
        ActionChoiceNode node = context.getNode();
        return node.nextNodeId(context.getAction());
    }

    @Override
    public String id() {
        return ActionChoiceNode.class.getName();
    }
}