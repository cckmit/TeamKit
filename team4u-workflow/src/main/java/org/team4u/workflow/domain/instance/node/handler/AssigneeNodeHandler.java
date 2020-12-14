package org.team4u.workflow.domain.instance.node.handler;

import cn.hutool.core.util.StrUtil;
import org.team4u.workflow.domain.definition.ProcessNode;
import org.team4u.workflow.domain.definition.node.AssigneeNode;
import org.team4u.workflow.domain.instance.ProcessAssignee;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 处理人节点处理器
 *
 * @author jay.wu
 */
public class AssigneeNodeHandler implements ProcessNodeHandler {

    @Override
    public ProcessNode handle(Context context) {
        AssigneeNode node = context.getNode();
        List<String> assignees = new ArrayList<>();

        switch (node.getRuleType()) {
            case AssigneeNode.RULE_TYPE_USER:
                assignees.add(node.getRuleExpression());
                break;

            case AssigneeNode.RULE_TYPE_ROLE:
                assignees = StrUtil.splitTrim(node.getRuleExpression(), ",");
                break;

            default:
                assignees = assigneesOf(node);
                break;
        }

        for (String assignee : assignees) {
            context.getInstance().getAssignees()
                    .add(new ProcessAssignee(
                            context.getInstance().getProcessInstanceId(),
                            assignee
                    ));
        }

        return null;
    }

    protected List<String> assigneesOf(AssigneeNode node) {
        return Collections.emptyList();
    }

    @Override
    public String id() {
        return AssigneeNode.class.getSimpleName();
    }
}