package org.team4u.workflow.infrastructure.persistence.instance;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import org.team4u.workflow.domain.definition.ProcessDefinition;
import org.team4u.workflow.domain.instance.ProcessAssignee;
import org.team4u.workflow.domain.instance.ProcessInstance;
import org.team4u.workflow.domain.instance.ProcessInstanceDetail;

import java.util.List;
import java.util.stream.Collectors;

public class ProcessInstanceConverter {

    private static final ProcessInstanceConverter INSTANCE = new ProcessInstanceConverter();

    public static ProcessInstanceConverter instance() {
        return INSTANCE;
    }

    public ProcessInstance toProcessInstance(ProcessDefinition definition,
                                             ProcessInstanceDo instanceDo,
                                             List<ProcessAssigneeDo> assigneeDoList) {
        ProcessInstance instance = new ProcessInstance(
                instanceDo.getProcessInstanceId(),
                instanceDo.getProcessInstanceType(),
                instanceDo.getProcessInstanceName(),
                definition.getProcessDefinitionId(),
                definition.processNodeOf(instanceDo.getCurrentNodeId()),
                instanceDo.getCreateBy(),
                new ProcessInstanceDetail(instanceDo.getProcessInstanceDetail())
        );

        if (assigneeDoList != null) {
            instance.setAssignees(
                    assigneeDoList.stream()
                            .map(it -> toProcessAssignee(definition, it))
                            .collect(Collectors.toSet())
            );
        }

        BeanUtil.copyProperties(
                instanceDo,
                instance,
                "processDefinitionId", "processInstanceDetail"
        );
        return instance;
    }

    private ProcessAssignee toProcessAssignee(ProcessDefinition definition, ProcessAssigneeDo assigneeDo) {
        ProcessAssignee assignee = new ProcessAssignee(
                assigneeDo.getNodeId(),
                assigneeDo.getAssignee()
        );

        if (StrUtil.isNotBlank(assigneeDo.getActionId())) {
            assignee.handle(definition.availableActionOf(assigneeDo.getActionId()));
        }

        BeanUtil.copyProperties(assigneeDo, assignee);
        return assignee;
    }
}
