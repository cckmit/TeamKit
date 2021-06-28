package org.team4u.workflow.infrastructure.persistence.instance;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.dao.DuplicateKeyException;
import org.team4u.base.error.IdempotentException;
import org.team4u.ddd.domain.model.DomainEventAwareRepository;
import org.team4u.ddd.event.EventStore;
import org.team4u.workflow.domain.definition.ProcessDefinition;
import org.team4u.workflow.domain.definition.ProcessDefinitionId;
import org.team4u.workflow.domain.definition.ProcessDefinitionRepository;
import org.team4u.workflow.domain.instance.ProcessAssignee;
import org.team4u.workflow.domain.instance.ProcessInstance;
import org.team4u.workflow.domain.instance.ProcessInstanceDetail;
import org.team4u.workflow.domain.instance.ProcessInstanceRepository;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * 基于数据库的流程实例资源库
 *
 * @author jay.wu
 */
public class MybatisProcessInstanceRepository
        extends DomainEventAwareRepository<ProcessInstance>
        implements ProcessInstanceRepository {

    private final ProcessInstanceMapper instanceMapper;
    private final ProcessAssigneeMapper assigneeMapper;

    private final ProcessDefinitionRepository definitionRepository;

    public MybatisProcessInstanceRepository(EventStore eventStore,
                                            ProcessInstanceMapper instanceMapper,
                                            ProcessAssigneeMapper assigneeMapper,
                                            ProcessDefinitionRepository definitionRepository) {
        super(eventStore);

        this.instanceMapper = instanceMapper;
        this.assigneeMapper = assigneeMapper;
        this.definitionRepository = definitionRepository;
    }

    @Override
    public ProcessInstance domainOf(String domainId) {
        ProcessInstanceDo instanceDo = instanceMapper.selectOne(
                new LambdaQueryWrapper<ProcessInstanceDo>()
                        .eq(ProcessInstanceDo::getProcessInstanceId, domainId)
        );

        ProcessDefinition definition = definitionRepository.domainOf(new ProcessDefinitionId(
                instanceDo.getProcessDefinitionId(),
                instanceDo.getProcessDefinitionVersion()
        ).toString());

        return toProcessInstance(definition, instanceDo)
                .setAssignees(toProcessAssignees(definition, domainId));
    }

    private ProcessInstance toProcessInstance(ProcessDefinition definition,
                                              ProcessInstanceDo instanceDo) {
        ProcessInstance instance = new ProcessInstance(
                instanceDo.getProcessInstanceId(),
                instanceDo.getProcessInstanceType(),
                instanceDo.getProcessInstanceName(),
                definition.getProcessDefinitionId(),
                definition.processNodeOf(instanceDo.getCurrentNodeId()),
                instanceDo.getCreateBy(),
                new ProcessInstanceDetail(instanceDo.getProcessInstanceDetail())
        );

        BeanUtil.copyProperties(
                instanceDo,
                instance,
                "processDefinitionId", "processInstanceDetail"
        );
        return instance;
    }

    private Set<ProcessAssignee> toProcessAssignees(ProcessDefinition definition, String processInstanceId) {
        return assigneeMapper.selectList(
                new LambdaQueryWrapper<ProcessAssigneeDo>()
                        .eq(ProcessAssigneeDo::getProcessInstanceId, processInstanceId)
        )
                .stream()
                .map(it -> toProcessAssignee(definition, it))
                .collect(Collectors.toSet());
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

    @Override
    protected void doSave(ProcessInstance instance) {
        try {
            saveProcessInstance(instance);
            saveAssignee(instance);
        } catch (DuplicateKeyException e) {
            throw new IdempotentException(String.format(
                    "processInstanceId=%s",
                    instance.getProcessInstanceId()
            ), e);
        }
    }

    private void saveProcessInstance(ProcessInstance instance) {
        ProcessInstanceDo instanceDo = toProcessInstanceDo(instance);
        if (instance.getId() == null) {
            instanceMapper.insert(instanceDo);
            instance.setId(instanceDo.getId());
        } else {
            if (instanceMapper.updateById(instanceDo) == 0) {
                instance.failWhenConcurrencyViolation();
            }

            instance.setConcurrencyVersion(instanceDo.getConcurrencyVersion());
        }
    }

    private void saveAssignee(ProcessInstance instance) {
        for (ProcessAssignee assignee : instance.getAssignees()) {
            ProcessAssigneeDo assigneeDo = toProcessAssigneeDo(instance.getProcessInstanceId(), assignee);
            if (assignee.getId() == null) {
                assigneeMapper.insert(assigneeDo);
                assignee.setId(assigneeDo.getId());
            } else {
                assigneeMapper.updateById(assigneeDo);
            }
        }
    }

    private ProcessAssigneeDo toProcessAssigneeDo(String instanceId, ProcessAssignee assignee) {
        ProcessAssigneeDo assigneeDo = new ProcessAssigneeDo();
        assigneeDo.setProcessInstanceId(instanceId);

        BeanUtil.copyProperties(assignee, assigneeDo);

        if (assignee.getAction() != null) {
            assigneeDo.setActionId(assignee.getAction().getActionId());
        }

        return assigneeDo;
    }

    private ProcessInstanceDo toProcessInstanceDo(ProcessInstance instance) {
        ProcessInstanceDo instanceDo = new ProcessInstanceDo();

        BeanUtil.copyProperties(instance, instanceDo, "processDefinitionId");

        // 当前节点
        instanceDo.setCurrentNodeId(instance.getCurrentNode().getNodeId());
        instanceDo.setCurrentNodeName(instance.getCurrentNode().getNodeName());

        // 流程定义
        ProcessDefinitionId processDefinitionId = instance.getProcessDefinitionId();

        // 没有版本号，获取最新的流程定义标识
        if (!instance.getProcessDefinitionId().hasVersion()) {
            ProcessDefinition definition = definitionRepository.domainOf(
                    instance.getProcessDefinitionId().toString()
            );

            instanceDo.setProcessInstanceName(definition.getProcessDefinitionName());

            processDefinitionId = definition.getProcessDefinitionId();
        }

        instanceDo.setProcessInstanceDetail(instance.getProcessInstanceDetail().getBody());
        instanceDo.setProcessDefinitionId(processDefinitionId.getId());
        instanceDo.setProcessDefinitionVersion(processDefinitionId.getVersion());
        instanceDo.setConcurrencyVersion(instance.concurrencyVersion());
        return instanceDo;
    }
}