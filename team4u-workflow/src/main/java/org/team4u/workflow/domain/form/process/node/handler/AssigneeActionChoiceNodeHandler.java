package org.team4u.workflow.domain.form.process.node.handler;

import cn.hutool.core.util.StrUtil;
import org.team4u.base.registrar.PolicyRegistrar;
import org.team4u.base.registrar.StringIdPolicy;
import org.team4u.workflow.domain.form.FormContextKeys;
import org.team4u.workflow.domain.form.process.definition.ProcessFormAction;
import org.team4u.workflow.domain.form.process.definition.node.AssigneeActionChoiceNode;
import org.team4u.workflow.domain.instance.ProcessAssignee;
import org.team4u.workflow.domain.instance.exception.NoPermissionException;
import org.team4u.workflow.domain.instance.node.handler.AbstractActionChoiceNodeHandler;
import org.team4u.workflow.domain.instance.node.handler.ProcessNodeHandlerContext;

import static org.team4u.workflow.domain.form.process.definition.node.AssigneeActionChoiceNode.CHOICE_TYPE_ALL;
import static org.team4u.workflow.domain.form.process.definition.node.AssigneeActionChoiceNode.CHOICE_TYPE_ANY;

/**
 * 处理人动作选择器节点处理器
 *
 * @author jay.wu
 */
public class AssigneeActionChoiceNodeHandler extends AbstractActionChoiceNodeHandler<AssigneeActionChoiceNode> {

    private final PolicyService policyService = new PolicyService();

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
        return policyService.availablePolicyOf(node.getChoiceType()).canJumpToNextNode(context, node);
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

    /**
     * 注册策略
     *
     * @param policy 策略
     */
    public void registerPolicy(Policy policy) {
        policyService.registerPolicy(policy);
    }

    /**
     * 跳转策略
     *
     * @author jay.wu
     */
    public interface Policy extends StringIdPolicy {

        /**
         * 是否可以跳转到下一个节点
         *
         * @param context 流程上下文
         * @param node    节点对象
         * @return 是否可以跳转到下一个节点
         */
        boolean canJumpToNextNode(ProcessNodeHandlerContext context, AssigneeActionChoiceNode node);
    }

    public static class PolicyService extends PolicyRegistrar<String, Policy> {

        public PolicyService() {
            registerPoliciesByBeanProvidersAndEvent();
        }
    }

    public static class AnyPolicy implements Policy {

        @Override
        public String id() {
            return CHOICE_TYPE_ANY;
        }

        @Override
        public boolean canJumpToNextNode(ProcessNodeHandlerContext context, AssigneeActionChoiceNode node) {
            return context.getInstance()
                    .getAssignees()
                    .stream()
                    .anyMatch(it -> it.getAction() != null);
        }
    }

    public static class AllPolicy implements Policy {

        @Override
        public String id() {
            return CHOICE_TYPE_ALL;
        }

        @Override
        public boolean canJumpToNextNode(ProcessNodeHandlerContext context, AssigneeActionChoiceNode node) {
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
    }
}