package org.team4u.workflow.infrastructure.persistence.definition;

import org.team4u.ddd.infrastructure.persistence.memory.InMemoryEventStore;

public class HutoolProcessDefinitionRepositoryTest extends AbstractProcessDefinitionRepositoryTest {

    @Override
    protected AbstractProcessDefinitionRepository create() {
        return new HutoolProcessDefinitionRepository(
                new InMemoryEventStore(),
                jdbcTemplate().getDataSource()
        );
    }
}