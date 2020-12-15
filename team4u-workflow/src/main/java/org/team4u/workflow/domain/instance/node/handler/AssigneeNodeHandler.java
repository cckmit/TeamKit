package org.team4u.workflow.domain.instance.node.handler;

import cn.hutool.core.util.StrUtil;
import org.team4u.workflow.domain.definition.node.AssigneeNode;
import org.team4u.workflow.domain.instance.ProcessAssignee;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 处理人节点处理器
 *
 * @author jay.wu
 */
public class AssigneeNodeHandler extends AbstractStaticProcessNodeHandler {

    @Override
    public void internalHandle(Context context) {
        AssigneeNode node = context.getNode();
        List<String> assignees;

        if (AssigneeNode.RULE_TYPE_USER.equals(node.getRuleType())) {
            assignees = StrUtil.splitTrim(node.getRuleExpression(), ",");
        } else {
            assignees = assigneesOf(node);
        }

        context.getInstance().setAssignees(
                assignees.stream()
                        .map(it -> new ProcessAssignee(
                                context.getInstance().getProcessInstanceId(),
                                it
                        ))
                        .collect(Collectors.toSet())
        );
    }

    protected List<String> assigneesOf(AssigneeNode node) {
        return Collections.emptyList();
    }

    @Override
    public String id() {
        return AssigneeNode.class.getSimpleName();
    }
}