package org.team4u.workflow.domain.instance.node.handler;

import org.team4u.base.error.SystemDataNotExistException;
import org.team4u.workflow.domain.definition.node.ActionChoiceNode;
import org.team4u.workflow.domain.instance.NoPermissionException;

/**
 * 动作选择器节点处理器
 *
 * @author jay.wu
 */
public class ActionChoiceNodeHandler implements ProcessNodeHandler {

    @Override
    public String handle(Context context) {
        checkOperatorPermissions(context);
        return findNextNode(context);
    }

    private String findNextNode(Context context) {
        ActionChoiceNode node = context.getNode();
        String nextNodeId = node.nextNodeId(context.getAction());

        if (nextNodeId == null) {
            throw new SystemDataNotExistException(
                    String.format("NextNode is null|instanceId=%s|nodeId=%s|action=%s",
                            context.getInstance().getProcessInstanceId(),
                            node.getNodeId(),
                            context.getAction()
                    )
            );
        }

        return nextNodeId;
    }

    private void checkOperatorPermissions(Context context) {
        if (context.getAction().matchPermissions(context.getOperatorPermissions())) {
            return;
        }

        throw new NoPermissionException(
                String.format("processInstanceId=%s|operator=%s|operatorPermissions=%s",
                        context.getInstance().getProcessInstanceId(),
                        context.getOperatorId(),
                        context.getOperatorPermissions()
                )
        );
    }

    @Override
    public String id() {
        return ActionChoiceNode.class.getSimpleName();
    }
}