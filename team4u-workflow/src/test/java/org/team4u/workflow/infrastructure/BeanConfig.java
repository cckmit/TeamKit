package org.team4u.workflow.infrastructure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.team4u.base.config.LocalJsonConfigService;
import org.team4u.ddd.event.EventStore;
import org.team4u.ddd.infrastructure.persistence.memory.LogOnlyEventStore;
import org.team4u.test.spring.DbTestBeanConfig;
import org.team4u.workflow.domain.instance.ProcessInstanceRepository;
import org.team4u.workflow.infrastructure.persistence.definition.JsonProcessDefinitionRepository;
import org.team4u.workflow.infrastructure.persistence.instance.MybatisProcessInstanceRepository;
import org.team4u.workflow.infrastructure.persistence.instance.ProcessAssigneeMapper;
import org.team4u.workflow.infrastructure.persistence.instance.ProcessInstanceMapper;

@Configuration
@Import(DbTestBeanConfig.class)
@ComponentScan("org.team4u.workflow.infrastructure.persistence")
public class BeanConfig {

    @Bean
    public EventStore eventStore() {
        return new LogOnlyEventStore();
    }

    @Bean
    public ProcessInstanceRepository processInstanceRepository(EventStore eventStore,
                                                               ProcessInstanceMapper instanceMapper,
                                                               ProcessAssigneeMapper assigneeMapper) {
        return new MybatisProcessInstanceRepository(
                eventStore,
                instanceMapper,
                assigneeMapper,
                new JsonProcessDefinitionRepository(new LocalJsonConfigService())
        );
    }
}