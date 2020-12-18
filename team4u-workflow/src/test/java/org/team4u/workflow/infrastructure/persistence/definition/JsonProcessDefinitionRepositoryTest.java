package org.team4u.workflow.infrastructure.persistence.definition;

import org.junit.Assert;
import org.junit.Test;
import org.team4u.workflow.domain.definition.ProcessDefinition;

import static org.team4u.workflow.TestUtil.definitionOf;

public class JsonProcessDefinitionRepositoryTest {

    @Test
    public void domainOf() {
        ProcessDefinition definition = definitionOf("simple");

        Assert.assertEquals(
                "[save, submit, test, approve, reject]",
                definition.getActions().toString()
        );

        Assert.assertEquals(
                "[created, createdActionNode, dynamic, pending, pendingActionNode, rejected, completed]",
                definition.getNodes().toString()
        );
    }
}