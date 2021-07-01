package org.team4u.workflow.infrastructure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.team4u.ddd.event.EventStore;
import org.team4u.workflow.application.ProcessAppQueryService;
import org.team4u.workflow.application.ProcessAppService;
import org.team4u.workflow.domain.definition.ProcessDefinitionRepository;
import org.team4u.workflow.domain.instance.ProcessInstanceRepository;
import org.team4u.workflow.domain.instance.node.handler.bean.LogBeanHandler;
import org.team4u.workflow.domain.instance.node.handler.bean.ProcessBeanHandler;
import org.team4u.workflow.infrastructure.persistence.definition.MybatisProcessDefinitionRepository;
import org.team4u.workflow.infrastructure.persistence.definition.ProcessDefinitionMapper;
import org.team4u.workflow.infrastructure.persistence.instance.MybatisProcessInstanceRepository;
import org.team4u.workflow.infrastructure.persistence.instance.ProcessAssigneeMapper;
import org.team4u.workflow.infrastructure.persistence.instance.ProcessInstanceMapper;

import java.util.List;

/**
 * 流程bean定义
 *
 * @author jay.wu
 */
@Configuration
public class ProcessBeanConfig {

    @Bean
    public ProcessAppService processAppService(
            List<ProcessBeanHandler> processBeanHandlers,
            ProcessInstanceRepository processInstanceRepository,
            ProcessDefinitionRepository processDefinitionRepository) {
        ProcessAppService processAppService = new ProcessAppService(
                processInstanceRepository,
                processDefinitionRepository
        );

        if (processBeanHandlers != null) {
            processAppService.registerBeanHandlers(processBeanHandlers);
        }

        return processAppService;
    }

    @Bean
    public ProcessAppQueryService processAppQueryService(ProcessInstanceMapper instanceMapper,
                                                         ProcessDefinitionRepository definitionRepository) {
        return new ProcessAppQueryService(instanceMapper, definitionRepository);
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

    @Bean
    public ProcessBeanHandler logBeanHandler() {
        return new LogBeanHandler();
    }
}