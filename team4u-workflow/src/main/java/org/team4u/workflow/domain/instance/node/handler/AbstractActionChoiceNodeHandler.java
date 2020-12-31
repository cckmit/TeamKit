package org.team4u.workflow.domain.instance.node.handler;

import org.team4u.workflow.domain.definition.node.ActionChoiceNode;
import org.team4u.workflow.domain.instance.exception.ProcessActionNodeNotExistException;

/**
 * 抽象动作选择器节点处理器
 *
 * @author jay.wu
 */
public abstract class AbstractActionChoiceNodeHandler<N extends ActionChoiceNode>
        extends AbstractProcessNodeHandler<N> {

    @Override
    public String handle(ProcessNodeHandlerContext context) {
        return findNextNode(context);
    }

    private String findNextNode(ProcessNodeHandlerContext context) throws ProcessActionNodeNotExistException {
        ActionChoiceNode node = node(context);
        return node.nextNodeId(context.getAction());
    }
}