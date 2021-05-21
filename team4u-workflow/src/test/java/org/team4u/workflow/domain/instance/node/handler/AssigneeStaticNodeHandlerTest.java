package org.team4u.workflow.domain.instance.node.handler;

import org.junit.Assert;
import org.junit.Test;
import org.team4u.workflow.domain.form.process.definition.node.AssigneeStaticNode;
import org.team4u.workflow.domain.form.process.node.handler.AssigneeStaticNodeHandler;
import org.team4u.workflow.domain.instance.ProcessInstance;

import static org.team4u.workflow.TestUtil.*;

public class AssigneeStaticNodeHandlerTest {

    @Test
    public void handleUserNode() {
        AssigneeStaticNodeHandler handler = new AssigneeStaticNodeHandler();
        ProcessInstance instance = newInstance();
        handler.handle(contextBuilder()
                .withInstance(instance)
                .withAction(action(TEST))
                .withNode(new AssigneeStaticNode(
                        TEST,
                        TEST,
                        null,
                        AssigneeStaticNode.RULE_TYPE_USER,
                        " 1, 2 "))
                .build());

        Assert.assertEquals(
                "[test,1,null, test,2,null]",
                instance.getAssignees().toString()
        );
    }
}