package org.team4u.workflow.domain.instance.node.handler;

import cn.hutool.core.util.StrUtil;
import org.team4u.workflow.domain.definition.node.AssigneeActionChoiceNode;
import org.team4u.workflow.domain.instance.NoPermissionException;
import org.team4u.workflow.domain.instance.ProcessAssignee;

import static org.team4u.workflow.domain.definition.node.AssigneeActionChoiceNode.CHOICE_TYPE_ANY;

/**
 * 处理人动作选择器节点处理器
 *
 * @author jay.wu
 */
public class AssigneeActionChoiceNodeHandler extends ActionChoiceNodeHandler {

    @Override
    public String handle(Context context) {
        ProcessAssignee assignee = context.getInstance().assigneeOf(context.getOperatorId());

        if (assignee == null) {
            throw new NoPermissionException(
                    String.format("processInstanceId=%s|operator=%s",
                            context.getInstance().getProcessInstanceId(),
                            context.getOperatorId()
                    )
            );
        }

        assignee.setAction(context.getAction());

        if (!canJumpToNextNode(context)) {
            return null;
        }

        return super.handle(context);
    }

    protected boolean canJumpToNextNode(Context context) {
        AssigneeActionChoiceNode node = context.getNode();

        if (StrUtil.equals(node.getChoiceType(), CHOICE_TYPE_ANY)) {
            return context.getInstance().getAssignees().stream()
                    .anyMatch(it -> it.getAction() != null);
        }

        return false;
    }

    @Override
    public String id() {
        return AssigneeActionChoiceNode.class.getSimpleName();
    }
}