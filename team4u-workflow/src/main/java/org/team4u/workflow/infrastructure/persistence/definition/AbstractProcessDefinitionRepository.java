package org.team4u.workflow.infrastructure.persistence.definition;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.team4u.ddd.domain.model.DomainEventAwareRepository;
import org.team4u.ddd.event.EventStore;
import org.team4u.workflow.domain.definition.ProcessDefinition;
import org.team4u.workflow.domain.definition.ProcessDefinitionId;
import org.team4u.workflow.domain.definition.ProcessDefinitionRepository;

import java.util.Date;

/**
 * 抽象流程定义资源库
 *
 * @author jay.wu
 */
public abstract class AbstractProcessDefinitionRepository
        extends DomainEventAwareRepository<ProcessDefinition>
        implements ProcessDefinitionRepository {

    public AbstractProcessDefinitionRepository(EventStore eventStore) {
        super(eventStore);
    }

    @Override
    public ProcessDefinition domainOf(String domainId) {
        ProcessDefinitionId processDefinitionId = ProcessDefinitionId.of(domainId);
        ProcessDefinitionDo definitionDo = processDefinitionDoOf(processDefinitionId);
        return toProcessDefinition(processDefinitionId, definitionDo);
    }

    protected abstract ProcessDefinitionDo processDefinitionDoOf(ProcessDefinitionId processDefinitionId);

    private ProcessDefinition toProcessDefinition(ProcessDefinitionId processDefinitionId,
                                                  ProcessDefinitionDo definitionDo) {
        String json = definitionDo.getProcessDefinitionBody();
        return ProcessDefinitionUtil.definitionOfJson(processDefinitionId, json);
    }

    @Override
    protected void doSave(ProcessDefinition definition) {
        ProcessDefinitionDo definitionDo = toProcessDefinitionDo(definition);
        if (definition.getId() == null) {
            definitionDo.setCreateTime(new Date());
            insertProcessDefinition(definitionDo);
            definition.setId(definitionDo.getId());
        } else {
            definitionDo.setUpdateTime(new Date());
            updateProcessDefinition(definitionDo);
        }
    }

    protected abstract void insertProcessDefinition(ProcessDefinitionDo definitionDo);

    protected abstract void updateProcessDefinition(ProcessDefinitionDo definitionDo);

    private ProcessDefinitionDo toProcessDefinitionDo(ProcessDefinition definition) {
        ProcessDefinitionDo definitionDo = new ProcessDefinitionDo();
        definitionDo.setId(definitionDo.getId());
        definitionDo.setProcessDefinitionId(definition.getProcessDefinitionId().getId());
        definitionDo.setProcessDefinitionVersion(definition.getProcessDefinitionId().getVersion());
        definitionDo.setProcessDefinitionName(definition.getProcessDefinitionName());

        definitionDo.setProcessDefinitionBody(
                JSON.toJSONString(
                        definition,
                        SerializerFeature.WriteClassName
                )
        );

        return definitionDo;
    }
}