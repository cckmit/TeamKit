package org.team4u.workflow.infrastructure.persistence.definition;

import org.junit.Test;
import org.team4u.workflow.domain.definition.ProcessDefinition;

import static org.team4u.workflow.TestUtil.definitionOf;

public class JsonProcessDefinitionRepositoryTest {

    @Test
    public void domainOf() {
        ProcessDefinition definition = definitionOf("simple");
        ProcessDefinitionRepositoryTestUtil.checkDefinition(definition);
    }
}