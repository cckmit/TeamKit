package org.team4u.workflow.application;

import org.junit.Assert;
import org.junit.Test;
import org.team4u.ddd.infrastructure.persistence.memory.LogOnlyEventStore;
import org.team4u.workflow.TestUtil;
import org.team4u.workflow.domain.definition.ProcessAction;
import org.team4u.workflow.domain.definition.ProcessDefinition;
import org.team4u.workflow.domain.definition.ProcessDefinitionId;
import org.team4u.workflow.domain.instance.DefaultProcessPermissionService;

import java.util.List;

public class WorkflowAppServiceTest {

    @Test
    public void hasAvailableActions() {
        checkAvailableActions(TestUtil.TEST, "[save, submit, test]");
    }

    @Test
    public void noAvailableActions() {
        checkAvailableActions(TestUtil.TEST1, "[]");
    }

    private void checkAvailableActions(String operator, String expectedActions) {
        WorkflowAppService service = new WorkflowAppService(
                new LogOnlyEventStore(),
                null,
                new DefaultProcessPermissionService(),
                null,
                TestUtil.processDefinitionRepository
        );

        ProcessDefinition definition = TestUtil.definitionOf("simple.json");

        List<ProcessAction> actions = service.availableActionsOf(
                TestUtil.newInstance()
                        .setCurrentNode(definition.processNodeOf("created"))
                        .setProcessDefinitionId(ProcessDefinitionId.of("simple.json")),
                operator
        );
        Assert.assertEquals(expectedActions, actions.toString());
    }
}