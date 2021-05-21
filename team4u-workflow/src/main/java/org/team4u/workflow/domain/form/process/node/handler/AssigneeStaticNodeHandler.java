package org.team4u.workflow.domain.form.process.node.handler;

import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import org.team4u.base.log.LogMessage;
import org.team4u.workflow.domain.form.process.definition.node.AssigneeStaticNode;
import org.team4u.workflow.domain.instance.ProcessAssignee;
import org.team4u.workflow.domain.instance.node.handler.AbstractStaticProcessNodeHandler;
import org.team4u.workflow.domain.instance.node.handler.ProcessNodeHandlerContext;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 处理人节点处理器
 *
 * @author jay.wu
 */
public class AssigneeStaticNodeHandler extends AbstractStaticProcessNodeHandler<AssigneeStaticNode> {

    private final Log log = Log.get();

    @Override
    public void internalHandle(ProcessNodeHandlerContext context) {
        AssigneeStaticNode node = node(context);
        List<String> assignees;

        if (AssigneeStaticNode.RULE_TYPE_USER.equals(node.getRuleType())) {
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

    protected List<String> assigneesOf(AssigneeStaticNode node) {
        return Collections.emptyList();
    }
}