package org.team4u.workflow.domain.form.process.node.handler;

import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import org.team4u.base.lang.IdObject;
import org.team4u.base.lang.IdObjectService;
import org.team4u.base.log.LogMessage;
import org.team4u.workflow.domain.form.process.definition.node.AssigneeStaticNode;
import org.team4u.workflow.domain.instance.ProcessAssignee;
import org.team4u.workflow.domain.instance.node.handler.AbstractStaticProcessNodeHandler;
import org.team4u.workflow.domain.instance.node.handler.ProcessNodeHandlerContext;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 处理人节点处理器
 *
 * @author jay.wu
 */
public class AssigneeStaticNodeHandler extends AbstractStaticProcessNodeHandler<AssigneeStaticNode> {

    private final Log log = Log.get();

    private final PolicyService policyService = new PolicyService();

    @Override
    public void internalHandle(ProcessNodeHandlerContext context) {
        AssigneeStaticNode node = node(context);

        List<String> assignees = policyService.availableObjectOfId(node.getRuleType()).assigneesOf(
                context,
                node
        );

        context.getInstance().getAssignees().addAll(
                assignees.stream()
                        .map(it -> new ProcessAssignee(
                                node.getNodeId(),
                                it
                        ))
                        .collect(Collectors.toSet())
        );

        log.info(LogMessage.create(this.getClass().getSimpleName(), "internalHandle")
                .append("processInstanceId", context.getInstance().getProcessInstanceId())
                .append("nodeId", node.getNodeId())
                .append("ruleType", node.getRuleType())
                .append("ruleExpression", node.getRuleExpression())
                .append("assignees", assignees)
                .success()
                .toString());
    }

    /**
     * 注册策略
     *
     * @param policy 策略
     */
    public void registerPolicy(Policy policy) {
        policyService.saveIdObject(policy);
    }

    /**
     * 处理人策略
     *
     * @author jay.wu
     */
    public interface Policy extends IdObject<String> {

        /**
         * 获取处理人标识集合
         *
         * @param context 流程上下文
         * @param node    处理人节点
         * @return 处理人标识集合
         */
        List<String> assigneesOf(ProcessNodeHandlerContext context, AssigneeStaticNode node);
    }

    public static class PolicyService extends IdObjectService<String, Policy> {
        public PolicyService() {
            super(Policy.class);

            saveObjectByBeanInitializedEvent();
        }
    }

    /**
     * 指定用户处理人策略
     *
     * @author jay.wu
     */
    public static class UserPolicy implements Policy {

        @Override
        public List<String> assigneesOf(ProcessNodeHandlerContext context, AssigneeStaticNode node) {
            return StrUtil.splitTrim(node.getRuleExpression(), ",");
        }

        @Override
        public String id() {
            return AssigneeStaticNode.RULE_TYPE_USER;
        }
    }
}