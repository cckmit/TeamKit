package org.team4u.workflow.infrastructure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.team4u.ddd.event.EventStore;
import org.team4u.workflow.application.ProcessAppService;
import org.team4u.workflow.domain.definition.ProcessDefinitionRepository;
import org.team4u.workflow.domain.instance.ProcessInstanceRepository;
import org.team4u.workflow.domain.instance.ProcessNodeHandlers;
import org.team4u.workflow.infrastructure.persistence.definition.MybatisProcessDefinitionRepository;
import org.team4u.workflow.infrastructure.persistence.definition.ProcessDefinitionMapper;
import org.team4u.workflow.infrastructure.persistence.instance.MybatisProcessInstanceRepository;
import org.team4u.workflow.infrastructure.persistence.instance.ProcessAssigneeMapper;
import org.team4u.workflow.infrastructure.persistence.instance.ProcessInstanceMapper;

/**
 * 流程bean定义
 *
 * @author jay.wu
 */
@Configuration
public class ProcessBeanConfig {

    @Bean
    public ProcessAppService processAppService(
            ProcessInstanceRepository processInstanceRepository,
            ProcessDefinitionRepository processDefinitionRepository) {
        return new ProcessAppService(
                new ProcessNodeHandlers(),
                processInstanceRepository,
                processDefinitionRepository
        );
    }

    @Bean
    public ProcessInstanceRepository processInstanceRepository(
            EventStore eventStore,
            ProcessInstanceMapper instanceMapper,
            ProcessAssigneeMapper assigneeMapper,
            ProcessDefinitionRepository processDefinitionRepository) {
        return new MybatisProcessInstanceRepository(
                eventStore,
                instanceMapper,
                assigneeMapper,
                processDefinitionRepository
        );
    }

    @Bean
    public ProcessDefinitionRepository processDefinitionRepository(
            EventStore eventStore,
            ProcessDefinitionMapper definitionMapper) {
        return new MybatisProcessDefinitionRepository(
                eventStore,
                definitionMapper
        );
    }
}