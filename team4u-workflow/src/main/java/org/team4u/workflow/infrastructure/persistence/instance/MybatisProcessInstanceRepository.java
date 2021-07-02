package org.team4u.workflow.infrastructure.persistence.instance;

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
import org.team4u.workflow.domain.instance.ProcessInstanceRepository;

import java.util.List;

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

        return ProcessInstanceConverter.instance().toProcessInstance(
                definition,
                instanceDo,
                loadAssignees(domainId)
        );
    }

    private List<ProcessAssigneeDo> loadAssignees(String processInstanceId) {
        return assigneeMapper.selectList(
                new LambdaQueryWrapper<ProcessAssigneeDo>()
                        .eq(ProcessAssigneeDo::getProcessInstanceId, processInstanceId)
        );
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
        ProcessDefinitionId definitionId = instance.getProcessDefinitionId();

        // 没有版本号，获取最新的流程定义标识
        if (!instance.getProcessDefinitionId().hasVersion()) {
            ProcessDefinition definition = definitionRepository.domainOf(
                    instance.getProcessDefinitionId().toString()
            );

            definitionId = definition.getProcessDefinitionId();
        }

        ProcessInstanceDo instanceDo = ProcessInstanceConverter.instance().toProcessInstanceDo(instance, definitionId);

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
            ProcessAssigneeDo assigneeDo = ProcessInstanceConverter.instance().toProcessAssigneeDo(
                    instance.getProcessInstanceId(), assignee
            );
            if (assignee.getId() == null) {
                assigneeMapper.insert(assigneeDo);
                assignee.setId(assigneeDo.getId());
            } else {
                assigneeMapper.updateById(assigneeDo);
            }
        }
    }
}