package org.team4u.workflow.domain.instance.node.handler;

import org.junit.Assert;
import org.junit.Test;
import org.team4u.workflow.TestUtil;
import org.team4u.workflow.domain.definition.node.BeanNode;

import static org.team4u.workflow.TestUtil.TEST;

public class BeanChoiceNodeHandlerTest {

    private final BeanChoiceNodeHandler handler = new TestBeanChoiceNodeHandler();

    @Test
    public void handle() {
        ProcessNodeHandlerContext context = TestUtil.contextBuilder()
                .withInstance(TestUtil.newInstance())
                .withNode(new BeanNode(TEST, TEST, TEST, null))
                .withAction(TestUtil.action(TEST))
                .build();
        String nextNodeId = handler.handle(context);

        Assert.assertEquals(TEST, nextNodeId);
    }
}