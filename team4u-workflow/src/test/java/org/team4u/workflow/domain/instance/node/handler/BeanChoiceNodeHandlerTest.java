package org.team4u.workflow.domain.instance.node.handler;

import org.junit.Assert;
import org.junit.Test;
import org.team4u.workflow.TestUtil;
import org.team4u.workflow.domain.definition.node.BeanChoiceNode;
import org.team4u.workflow.domain.instance.exception.ProcessNodeUnexpectedException;

import static org.team4u.workflow.TestUtil.TEST;
import static org.team4u.workflow.TestUtil.TEST1;

public class BeanChoiceNodeHandlerTest {

    private final BeanChoiceNodeHandler handler = new TestBeanChoiceNodeHandler();

    @Test
    public void handle() {
        ProcessNodeHandlerContext context = TestUtil.contextBuilder()
                .withInstance(TestUtil.newInstance())
                .withNode(new BeanChoiceNode(TEST, TEST, TEST, new String[]{TEST}))
                .withAction(TestUtil.action(TEST))
                .build();
        String nextNodeId = handler.handle(context);

        Assert.assertEquals(TEST, nextNodeId);
    }

    @Test
    public void unexpectedNode() {
        ProcessNodeHandlerContext context = TestUtil.contextBuilder()
                .withInstance(TestUtil.newInstance())
                .withNode(new BeanChoiceNode(TEST, TEST, TEST, new String[]{TEST1}))
                .withAction(TestUtil.action(TEST))
                .build();
        try {
            handler.handle(context);
            Assert.fail();
        } catch (ProcessNodeUnexpectedException e) {
            Assert.assertEquals(TEST, e.getMessage());
        }
    }
}