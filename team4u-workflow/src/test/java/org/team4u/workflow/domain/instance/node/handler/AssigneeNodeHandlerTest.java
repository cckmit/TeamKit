package org.team4u.workflow.domain.instance.node.handler;

import org.junit.Assert;
import org.junit.Test;
import org.team4u.workflow.domain.definition.node.AssigneeNode;
import org.team4u.workflow.domain.instance.ProcessInstance;

import static org.team4u.workflow.TestUtil.*;

public class AssigneeNodeHandlerTest {

    @Test
    public void handleUserNode() {
        AssigneeNodeHandler handler = new AssigneeNodeHandler();
        ProcessInstance instance = newInstance();
        handler.handle(context(
                instance,
                action(TEST),
                null,
                new AssigneeNode(
                        TEST,
                        TEST,
                        null,
                        AssigneeNode.RULE_TYPE_USER,
                        " 1, 2 ")
        ));

        Assert.assertEquals(
                "[test,1,null, test,2,null]",
                instance.getAssignees().toString()
        );

        Assert.assertEquals("[node=null,action=test,nextNode=test,remark='test',createBy='test']",
                instance.getLogs().toString());

        Assert.assertEquals(1, instance.events().size());
    }
}