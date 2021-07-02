package org.team4u.workflow.infrastructure.persistence.instance;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import org.team4u.workflow.domain.definition.ProcessDefinition;
import org.team4u.workflow.domain.definition.ProcessDefinitionId;
import org.team4u.workflow.domain.instance.ProcessAssignee;
import org.team4u.workflow.domain.instance.ProcessInstance;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 流程实例转换器
 *
 * @author jay.wu
 */
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
                instanceDo.getProcessInstanceDetail()
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


    public ProcessAssigneeDo toProcessAssigneeDo(String instanceId, ProcessAssignee assignee) {
        ProcessAssigneeDo assigneeDo = new ProcessAssigneeDo();
        assigneeDo.setProcessInstanceId(instanceId);

        BeanUtil.copyProperties(assignee, assigneeDo);

        if (assignee.getAction() != null) {
            assigneeDo.setActionId(assignee.getAction().getActionId());
        }

        return assigneeDo;
    }

    public ProcessInstanceDo toProcessInstanceDo(ProcessInstance instance, ProcessDefinitionId processDefinitionId) {
        ProcessInstanceDo instanceDo = new ProcessInstanceDo();

        BeanUtil.copyProperties(instance, instanceDo, "processDefinitionId");

        // 当前节点
        instanceDo.setCurrentNodeId(instance.getCurrentNode().getNodeId());
        instanceDo.setCurrentNodeName(instance.getCurrentNode().getNodeName());

        if (instance.getProcessInstanceDetail() == null ||
                instance.getProcessInstanceDetail().getBody() == null) {
            instanceDo.setProcessInstanceDetail("{}");
        } else {
            instanceDo.setProcessInstanceDetail(instance.getProcessInstanceDetail().getBody());
        }
        instanceDo.setProcessDefinitionId(processDefinitionId.getId());
        instanceDo.setProcessDefinitionVersion(processDefinitionId.getVersion());
        instanceDo.setConcurrencyVersion(instance.concurrencyVersion());
        return instanceDo;
    }
}
