package org.team4u.workflow.domain.instance.node.handler;

import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import org.team4u.base.log.LogMessage;
import org.team4u.workflow.domain.form.process.definition.node.AssigneeNode;
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

    private final Log log = Log.get();

    @Override
    public void internalHandle(Context context) {
        AssigneeNode node = context.getNode();
        List<String> assignees;

        if (AssigneeNode.RULE_TYPE_USER.equals(node.getRuleType())) {
            assignees = StrUtil.splitTrim(node.getRuleExpression(), ",");
        } else {
            assignees = assigneesOf(node);
        }

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
                .append("assignees", assignees)
                .success()
                .toString());
    }

    protected List<String> assigneesOf(AssigneeNode node) {
        return Collections.emptyList();
    }

    @Override
    public String id() {
        return AssigneeNode.class.getSimpleName();
    }
}