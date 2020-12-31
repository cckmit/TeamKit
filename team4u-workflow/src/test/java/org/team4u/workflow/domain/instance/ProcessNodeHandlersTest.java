package org.team4u.workflow.domain.instance;

import org.junit.Assert;
import org.junit.Test;
import org.team4u.workflow.TestUtil;
import org.team4u.workflow.domain.definition.ProcessDefinition;
import org.team4u.workflow.domain.instance.node.handler.DynamicChoiceNodeHandler;
import org.team4u.workflow.domain.instance.node.handler.ProcessNodeHandlerContext;

import static org.team4u.workflow.TestUtil.*;

public class ProcessNodeHandlersTest {

    private final ProcessDefinition definition = TestUtil.definitionOf("simple");
    private final ProcessNodeHandlers handles = new ProcessNodeHandlers(new MockProcessInstanceRepository());

    @Test
    public void save() {
        ProcessInstance instance = TestUtil.newInstance().setCurrentNode(definition.rootNode());
        handles.handle(contextBuilder()
                .withInstance(instance)
                .withAction(action("save"))
                .withDefinition(definition)
                .build());

        Assert.assertEquals(TestUtil.staticNode("created"), instance.getCurrentNode());
        Assert.assertEquals(
                "[nodeId=created,actionId=save,nextNodeId=created,remark='test',operator='test']",
                instance.events().toString()
        );
    }

    @Test
    public void dynamic() {
        ProcessInstance instance = TestUtil.newInstance().setCurrentNode(definition.rootNode());
        handles.saveIdObject(new DynamicChoiceNodeHandler(selectorAppService()));

        handles.handle(contextBuilder()
                .withInstance(instance)
                .withAction(action("test"))
                .withDefinition(definition)
                .build()
                .ext("a", 1));

        Assert.assertEquals(TestUtil.staticNode("created"), instance.getCurrentNode());
        Assert.assertEquals(
                "[nodeId=created,actionId=test,nextNodeId=created,remark='test',operator='test']",
                instance.events().toString()
        );
    }

    @Test
    public void testSubmit() {
        ProcessInstance instance = submit();

        Assert.assertEquals(
                TestUtil.assigneeNode("pending", null, null),
                instance.getCurrentNode()
        );

        Assert.assertEquals(
                "[nodeId=created,actionId=submit,nextNodeId=pending,remark='test',operator='test']",
                instance.events().toString()
        );
    }

    @Test
    public void reject() {
        checkIsCompleted("reject", "rejected");
    }

    @Test
    public void approve() {
        checkIsCompleted("approve", "completed");
    }

    private void checkIsCompleted(String actionId, String expectedNodeId) {
        ProcessInstance instance = submit();

        ProcessNodeHandlerContext context = contextBuilder()
                .withInstance(instance)
                .withAction(action(actionId))
                .withDefinition(definition)
                .build();
        handles.handle(context);

        Assert.assertEquals(expectedNodeId, instance.getCurrentNode().getNodeId());

        Assert.assertEquals(
                "nodeId=pending,actionId=" + actionId +
                        ",nextNodeId=" + expectedNodeId +
                        ",remark='test',operator='test'",
                instance.events().get(1).toString()
        );

        Assert.assertTrue(instance.isCompleted());
        Assert.assertEquals(TEST, context.ext(TEST));
    }

    private ProcessInstance submit() {
        ProcessInstance instance = TestUtil.newInstance().setCurrentNode(definition.rootNode());

        handles.handle(contextBuilder()
                .withInstance(instance)
                .withAction(action("submit"))
                .withDefinition(definition)
                .build());

        return instance;
    }
}