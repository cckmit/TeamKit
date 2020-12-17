package org.team4u.workflow.infrastructure.persistence;

import org.junit.Assert;
import org.junit.Test;
import org.team4u.workflow.domain.definition.ProcessDefinition;

import static org.team4u.workflow.TestUtil.definitionOf;

public class JsonProcessDefinitionRepositoryTest {

    @Test
    public void domainOf() {
        String path = "simple_def.json";
        ProcessDefinition definition = definitionOf(path);

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