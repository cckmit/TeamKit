package org.team4u.workflow.infrastructure.persistence.definition;

import org.junit.Assert;
import org.team4u.workflow.domain.definition.ProcessDefinition;
import org.team4u.workflow.domain.definition.ProcessDefinitionId;

public class ProcessDefinitionRepositoryTestUtil {

    public static void checkDefinition(ProcessDefinition definition) {
        Assert.assertEquals(
                "简单流程",
                definition.getProcessDefinitionName()
        );

        Assert.assertEquals(
                new ProcessDefinitionId("simple").toString(),
                definition.getProcessDefinitionId().toString()
        );

        Assert.assertEquals(
                "[save, submit, test, approve, reject]",
                definition.getActions().toString()
        );

        Assert.assertEquals(
                "[init, created, createdActionNode, dynamic, pending, pendingActionNode, rejected, completed]",
                definition.getNodes().toString()
        );
    }
}
