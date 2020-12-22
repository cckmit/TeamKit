package org.team4u.workflow.domain.instance.node.handler;

import org.junit.Assert;
import org.junit.Test;
import org.team4u.workflow.domain.definition.exception.ProcessNodeNotExistException;
import org.team4u.workflow.domain.form.process.definition.node.AssigneeActionChoiceNode;
import org.team4u.workflow.domain.form.process.node.handler.AssigneeActionChoiceNodeHandler;
import org.team4u.workflow.domain.instance.NoPermissionException;
import org.team4u.workflow.domain.instance.ProcessInstance;

import static org.team4u.workflow.TestUtil.*;

public class AssigneeActionChoiceNodeHandlerTest {

    @Test
    public void any() {
        AssigneeActionChoiceNodeHandler handler = new AssigneeActionChoiceNodeHandler();

        ProcessInstance instance = newInstance(TEST).setCurrentNode(staticNode(TEST));

        String nextNodeId = handler.handle(context(
                instance,
                action(TEST),
                null,
                assigneeActionChoiceNode(
                        AssigneeActionChoiceNode.CHOICE_TYPE_ANY,
                        actionNode(TEST, TEST1)
                )
        ));

        Assert.assertEquals(TEST1, nextNodeId);
        Assert.assertEquals(TEST, instance.currentAssigneeOf(TEST).getAction().getActionId());
    }

    @Test
    public void all() {
        AssigneeActionChoiceNodeHandler handler = new AssigneeActionChoiceNodeHandler();
        ProcessInstance instance = newInstance(TEST, TEST1).setCurrentNode(staticNode(TEST));

        AssigneeActionChoiceNode actionChoiceNode = assigneeActionChoiceNode(
                AssigneeActionChoiceNode.CHOICE_TYPE_ALL,
                actionNode(TEST, TEST1)
        );

        String nextNodeId = handler.handle(context(
                instance,
                action(TEST),
                null,
                actionChoiceNode
        ));

        Assert.assertNull(nextNodeId);
        Assert.assertNull(instance.currentAssigneeOf(TEST1).getAction());

        nextNodeId = handler.handle(new ProcessNodeHandler.Context(
                instance,
                null,
                action(TEST),
                TEST1,
                TEST,
                null
        ).setNode(actionChoiceNode));
        Assert.assertEquals(TEST1, nextNodeId);
    }

    @Test
    public void noMatchAssignee() {
        AssigneeActionChoiceNodeHandler handler = new AssigneeActionChoiceNodeHandler();

        ProcessInstance instance = newInstance().setCurrentNode(staticNode(TEST));

        try {
            handler.handle(context(
                    instance,
                    action(TEST),
                    null,
                    assigneeActionChoiceNode(
                            AssigneeActionChoiceNode.CHOICE_TYPE_ANY,
                            actionNode(TEST, TEST1)
                    )
            ));

            Assert.fail();
        } catch (NoPermissionException e) {
            Assert.assertEquals("processInstanceId=test|operator=test", e.getMessage());
        }
    }

    @Test
    public void noMatchActonPermission() {
        AssigneeActionChoiceNodeHandler handler = new AssigneeActionChoiceNodeHandler();
        ProcessInstance instance = newInstance(TEST).setCurrentNode(staticNode(TEST));
        try {
            handler.handle(context(
                    instance,
                    action(TEST, TEST),
                    null,
                    assigneeActionChoiceNode(
                            AssigneeActionChoiceNode.CHOICE_TYPE_ANY,
                            actionNode(TEST, TEST1)
                    )
            ));
            Assert.fail();
        } catch (NoPermissionException e) {
            Assert.assertEquals(
                    "processInstanceId=test|operator=test|operatorPermissions=[]",
                    e.getMessage()
            );
        }
    }

    @Test
    public void noAction() {
        AssigneeActionChoiceNodeHandler handler = new AssigneeActionChoiceNodeHandler();

        ProcessInstance instance = newInstance(TEST).setCurrentNode(staticNode(TEST));

        try {
            handler.handle(context(
                    instance,
                    action(TEST),
                    null,
                    assigneeActionChoiceNode(
                            AssigneeActionChoiceNode.CHOICE_TYPE_ANY,
                            actionNode(TEST, TEST1)
                    )
            ));

            Assert.fail();
        } catch (ProcessNodeNotExistException e) {
            Assert.assertEquals(
                    "NextNode is null|instanceId=test|nodeId=test|action=test",
                    e.getMessage()
            );
        }
    }

}