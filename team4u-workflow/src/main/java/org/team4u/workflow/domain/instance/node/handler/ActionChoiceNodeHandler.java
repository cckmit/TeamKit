package org.team4u.workflow.domain.instance.node.handler;

import org.team4u.base.error.SystemDataNotExistException;
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

        ProcessNode nextNode = node.nextNodeOfAction(context.getAction());
        if (nextNode == null) {
            throw new SystemDataNotExistException(
                    String.format("NextNode is null|instanceId=%s|nodeId=%s|action=%s",
                            context.getInstance().getProcessInstanceId(),
                            node.getNodeId(),
                            context.getAction()
                    )
            );
        }

        return nextNode;
    }

    @Override
    public String id() {
        return ActionChoiceNode.class.getSimpleName();
    }
}