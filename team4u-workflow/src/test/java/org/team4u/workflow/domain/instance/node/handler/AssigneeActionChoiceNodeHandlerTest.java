package org.team4u.workflow.domain.instance.node.handler;

import org.junit.Assert;
import org.junit.Test;
import org.team4u.workflow.TestUtil;
import org.team4u.workflow.domain.definition.exception.ProcessNodeNotExistException;
import org.team4u.workflow.domain.form.process.definition.node.AssigneeActionChoiceNode;
import org.team4u.workflow.domain.form.process.node.handler.AssigneeActionChoiceNodeHandler;
import org.team4u.workflow.domain.instance.ProcessInstance;
import org.team4u.workflow.domain.instance.exception.NoPermissionException;

import static org.team4u.workflow.TestUtil.*;

public class AssigneeActionChoiceNodeHandlerTest {

    @Test
    public void any() {
        AssigneeActionChoiceNodeHandler handler = new AssigneeActionChoiceNodeHandler();

        ProcessInstance instance = newInstance(TEST).setCurrentNode(staticNode(TEST));

        String nextNodeId = handler.handle(
                contextBuilder().withInstance(instance)
                        .withAction(action(TEST))
                        .withNode(anyAssigneeActionNode(actionNode(TEST, TEST1)))
                        .build()
        );

        Assert.assertEquals(TEST1, nextNodeId);
        Assert.assertEquals(TEST, instance.currentAssigneeOf(TEST).getAction().getActionId());
    }

    @Test
    public void all() {
        AssigneeActionChoiceNodeHandler handler = new AssigneeActionChoiceNodeHandler();
        ProcessInstance instance = newInstance(TEST, TEST1).setCurrentNode(staticNode(TEST));

        AssigneeActionChoiceNode actionChoiceNode = allAssigneeActionNode(TEST1, actionNode(TEST, TEST1));

        String nextNodeId = handler.handle(
                contextBuilder().withInstance(instance)
                        .withAction(action(TEST))
                        .withNode(actionChoiceNode)
                        .build()
        );

        Assert.assertNull(nextNodeId);
        Assert.assertNull(instance.currentAssigneeOf(TEST1).getAction());

        nextNodeId = handler.handle(
                contextBuilder().withInstance(instance)
                        .withOperatorId(TEST1)
                        .withAction(action(TEST))
                        .withNode(actionChoiceNode)
                        .build()
        );
        Assert.assertEquals(TEST1, nextNodeId);
    }

    @Test
    public void allAndReject() {
        AssigneeActionChoiceNodeHandler handler = new AssigneeActionChoiceNodeHandler();
        ProcessInstance instance = newInstance(TEST, TEST1).setCurrentNode(staticNode(TEST));

        AssigneeActionChoiceNode actionChoiceNode = TestUtil.allAssigneeActionNode(
                TEST,
                actionNode(TEST, TEST1)
        );

        String nextNodeId = handler.handle(
                contextBuilder().withInstance(instance)
                        .withAction(action(TEST))
                        .withNode(actionChoiceNode)
                        .build()
        );

        Assert.assertEquals(TEST1, nextNodeId);
    }

    @Test
    public void noMatchAssignee() {
        AssigneeActionChoiceNodeHandler handler = new AssigneeActionChoiceNodeHandler();

        ProcessInstance instance = newInstance().setCurrentNode(staticNode(TEST));

        try {
            handler.handle(
                    contextBuilder().withInstance(instance)
                            .withAction(action(TEST))
                            .withNode(anyAssigneeActionNode(actionNode(TEST, TEST1)))
                            .build()
            );

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
            handler.handle(
                    contextBuilder().withInstance(instance)
                            .withAction(action(TEST, TEST))
                            .withNode(anyAssigneeActionNode(actionNode(TEST, TEST1)))
                            .build()
            );
            Assert.fail();
        } catch (NoPermissionException e) {
            Assert.assertEquals(
                    "processInstanceId=test|operator=test|operatorPermissions=null",
                    e.getMessage()
            );
        }
    }

    @Test
    public void noAction() {
        AssigneeActionChoiceNodeHandler handler = new AssigneeActionChoiceNodeHandler();

        ProcessInstance instance = newInstance(TEST).setCurrentNode(staticNode(TEST));

        try {
            handler.handle(
                    contextBuilder().withInstance(instance)
                            .withAction(action(TEST))
                            .withNode(anyAssigneeActionNode(actionNode(TEST1, TEST1)))
                            .build()
            );

            Assert.fail();
        } catch (ProcessNodeNotExistException e) {
            Assert.assertEquals(
                    "NextNode is null|instanceId=test|nodeId=test|action=test",
                    e.getMessage()
            );
        }
    }

}