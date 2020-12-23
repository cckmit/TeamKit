package org.team4u.workflow.domain.form.process.node.handler;

import cn.hutool.core.util.StrUtil;
import org.team4u.workflow.domain.form.FormContextKeys;
import org.team4u.workflow.domain.form.process.definition.ProcessFormAction;
import org.team4u.workflow.domain.form.process.definition.node.AssigneeActionChoiceNode;
import org.team4u.workflow.domain.instance.ProcessAssignee;
import org.team4u.workflow.domain.instance.exception.NoPermissionException;
import org.team4u.workflow.domain.instance.node.handler.ActionChoiceNodeHandler;
import org.team4u.workflow.domain.instance.node.handler.ProcessNodeHandlerContext;

import static org.team4u.workflow.domain.form.process.definition.node.AssigneeActionChoiceNode.CHOICE_TYPE_ALL;
import static org.team4u.workflow.domain.form.process.definition.node.AssigneeActionChoiceNode.CHOICE_TYPE_ANY;

/**
 * 处理人动作选择器节点处理器
 *
 * @author jay.wu
 */
public class AssigneeActionChoiceNodeHandler extends ActionChoiceNodeHandler {

    @Override
    public String handle(ProcessNodeHandlerContext context) {
        ProcessAssignee assignee = context.getInstance().currentAssigneeOf(context.getOperatorId());

        checkOperatorPermissions(assignee, context);

        assignee.handle(context.getAction());

        if (!canJumpToNextNode(context)) {
            return null;
        }

        return super.handle(context);
    }

    protected boolean canJumpToNextNode(ProcessNodeHandlerContext context) {
        AssigneeActionChoiceNode node = context.getNode();

        if (StrUtil.equals(node.getChoiceType(), CHOICE_TYPE_ANY)) {
            return context.getInstance()
                    .getAssignees()
                    .stream()
                    .anyMatch(it -> it.getAction() != null);
        }

        if (StrUtil.equals(node.getChoiceType(), CHOICE_TYPE_ALL)) {
            // 遇到指定动作则直接跳转
            if (StrUtil.equals(context.getAction().getActionId(), node.getChoiceActionId())) {
                return true;
            }

            // 所有人触发动作后跳转
            return context.getInstance()
                    .getAssignees()
                    .stream()
                    .allMatch(it -> it.getAction() != null);
        }

        return false;
    }

    private void checkOperatorPermissions(ProcessAssignee assignee, ProcessNodeHandlerContext context) {
        if (assignee == null) {
            throw new NoPermissionException(
                    String.format("processInstanceId=%s|operator=%s",
                            context.getInstance().getProcessInstanceId(),
                            context.getOperatorId()
                    )
            );
        }

        ProcessFormAction action = (ProcessFormAction) context.getAction();
        if (action.matchPermissions(context.ext(FormContextKeys.OPERATOR_ACTION_PERMISSIONS))) {
            return;
        }

        throw new NoPermissionException(
                String.format("processInstanceId=%s|operator=%s|operatorPermissions=%s",
                        context.getInstance().getProcessInstanceId(),
                        context.getOperatorId(),
                        context.ext(FormContextKeys.OPERATOR_ACTION_PERMISSIONS))
        );
    }

    @Override
    public String id() {
        return AssigneeActionChoiceNode.class.getName();
    }
}