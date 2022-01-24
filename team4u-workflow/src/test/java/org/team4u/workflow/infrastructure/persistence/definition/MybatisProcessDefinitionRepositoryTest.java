package org.team4u.workflow.infrastructure.persistence.definition;

import org.springframework.beans.factory.annotation.Autowired;
import org.team4u.ddd.infrastructure.persistence.memory.InMemoryEventStore;

public class MybatisProcessDefinitionRepositoryTest extends AbstractProcessDefinitionRepositoryTest {

    @Autowired
    private ProcessDefinitionMapper definitionMapper;

    @Override
    protected AbstractProcessDefinitionRepository create() {
        return new MybatisProcessDefinitionRepository(
                new InMemoryEventStore(),
                definitionMapper
        );
    }
}