package org.team4u.workflow.domain.instance.node.handler;

import org.junit.Assert;
import org.junit.Test;
import org.team4u.base.error.SystemDataNotExistException;
import org.team4u.workflow.domain.instance.NoPermissionException;
import org.team4u.workflow.domain.instance.ProcessInstance;

import static org.team4u.workflow.TestUtil.*;

public class AssigneeActionChoiceNodeHandlerTest {

    @Test
    public void any() {
        AssigneeActionChoiceNodeHandler handler = new AssigneeActionChoiceNodeHandler();

        ProcessInstance instance = newInstance(TEST);

        String nextNodeId = handler.handle(context(
                instance,
                action(TEST),
                null,
                assigneeActionChoiceNode(actionNode(TEST, TEST1))
        ));

        Assert.assertEquals(TEST1, nextNodeId);
        Assert.assertEquals(TEST, instance.assigneeOf(TEST).getAction().getActionId());
    }

    @Test
    public void noMatchAssignee() {
        AssigneeActionChoiceNodeHandler handler = new AssigneeActionChoiceNodeHandler();

        ProcessInstance instance = newInstance();

        try {
            handler.handle(context(
                    instance,
                    action(TEST),
                    null,
                    assigneeActionChoiceNode(actionNode(TEST, TEST1))
            ));

            Assert.fail();
        } catch (NoPermissionException e) {
            Assert.assertEquals("processInstanceId=test|operator=test", e.getMessage());
        }
    }

    @Test
    public void noMatchActonPermission() {
        AssigneeActionChoiceNodeHandler handler = new AssigneeActionChoiceNodeHandler();
        ProcessInstance instance = newInstance(TEST);
        try {
            handler.handle(context(
                    instance,
                    action(TEST, TEST),
                    null,
                    assigneeActionChoiceNode(actionNode(TEST, TEST1))
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

        ProcessInstance instance = newInstance(TEST);

        try {
            handler.handle(context(
                    instance,
                    action(TEST),
                    null,
                    assigneeActionChoiceNode(actionNode(TEST1, TEST1))
            ));

            Assert.fail();
        } catch (SystemDataNotExistException e) {
            Assert.assertEquals(
                    "NextNode is null|instanceId=test|nodeId=test|action=test",
                    e.getMessage()
            );
        }
    }

}