package org.team4u.workflow.domain.instance;

import org.junit.Assert;
import org.junit.Test;
import org.team4u.workflow.TestUtil;
import org.team4u.workflow.domain.definition.ProcessDefinition;
import org.team4u.workflow.domain.instance.node.handler.DynamicChoiceNodeHandler;

import static org.team4u.workflow.TestUtil.selectorAppService;

public class ProcessNodeHandlersTest {

    private final ProcessDefinition definition = TestUtil.definitionOf("simple.json");
    private final ProcessNodeHandlers handles = new ProcessNodeHandlers();

    @Test
    public void save() {
        ProcessInstance instance = TestUtil.newInstance().setCurrentNode(definition.rootNode());
        handles.handle(TestUtil.context(
                instance,
                TestUtil.action("save"),
                definition,
                null
        ));

        Assert.assertEquals(TestUtil.staticNode("created"), instance.getCurrentNode());
        Assert.assertEquals(
                "[node=created,action=save,nextNode=created,remark='test',operator='test']",
                instance.events().toString()
        );
    }

    @Test
    public void dynamic() {
        ProcessInstance instance = TestUtil.newInstance().setCurrentNode(definition.rootNode());
        handles.saveIdObject(new DynamicChoiceNodeHandler(selectorAppService()));

        handles.handle(TestUtil.context(
                instance,
                TestUtil.action("test"),
                definition,
                null
        ).putExt("a", 1));

        Assert.assertEquals(TestUtil.staticNode("created"), instance.getCurrentNode());
        Assert.assertEquals(
                "[node=created,action=test,nextNode=created,remark='test',operator='test']",
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
                "[node=created,action=submit,nextNode=pending,remark='test',operator='test']",
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

        handles.handle(TestUtil.context(
                instance,
                TestUtil.action(actionId),
                definition,
                null
        ));

        Assert.assertEquals(TestUtil.staticNode(expectedNodeId), instance.getCurrentNode());

        Assert.assertEquals(
                "node=pending,action=" + actionId +
                        ",nextNode=" + expectedNodeId +
                        ",remark='test',operator='test'",
                instance.events().get(1).toString()
        );

        Assert.assertTrue(instance.isCompleted());
    }

    private ProcessInstance submit() {
        ProcessInstance instance = TestUtil.newInstance().setCurrentNode(definition.rootNode());
        handles.handle(TestUtil.context(
                instance,
                TestUtil.action("submit"),
                definition,
                null
        ));

        return instance;
    }
}