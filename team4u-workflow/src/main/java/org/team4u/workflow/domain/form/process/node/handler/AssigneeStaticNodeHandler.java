package org.team4u.workflow.domain.form.process.node.handler;

import cn.hutool.log.Log;
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
    private final AssigneeRepositories assigneeRepositories = new AssigneeRepositories();

    @Override
    public void internalHandle(ProcessNodeHandlerContext context) {
        AssigneeStaticNode node = node(context);

        List<String> assignees = assigneeRepositories.availableObjectOfId(node.getRuleType()).assigneesOf(
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
                .append("assignees", assignees)
                .success()
                .toString());
    }

    public void registerAssigneeRepository(AssigneeRepository repository) {
        assigneeRepositories.saveIdObject(repository);
    }
}