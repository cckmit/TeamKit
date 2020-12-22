package org.team4u.workflow.domain.instance.node.handler;

import org.junit.Assert;
import org.junit.Test;
import org.team4u.workflow.TestUtil;
import org.team4u.workflow.domain.definition.node.BeanNode;
import org.team4u.workflow.domain.instance.exception.ProcessBeanHandlerNotExistException;

import static org.team4u.workflow.TestUtil.TEST;
import static org.team4u.workflow.TestUtil.TEST1;

public class BeanNodeHandlerTest {

    private final BeanNodeHandler handler = new TestBeanNodeHandler();

    @Test
    public void handle() {
        ProcessNodeHandlerContext context = TestUtil.contextBuilder()
                .withInstance(TestUtil.newInstance())
                .withNode(new BeanNode(TEST, TEST, TEST, null))
                .withAction(TestUtil.action(TEST))
                .build();
        handler.handle(context);

        Assert.assertEquals(TEST, context.ext(TEST));
    }

    @Test
    public void notExist() {
        ProcessNodeHandlerContext context = TestUtil.contextBuilder()
                .withInstance(TestUtil.newInstance())
                .withNode(new BeanNode(TEST, TEST, TEST1, null))
                .withAction(TestUtil.action(TEST))
                .build();

        try {
            handler.handle(context);
            Assert.fail();
        } catch (ProcessBeanHandlerNotExistException e) {
            Assert.assertEquals(TEST1, e.getMessage());
        }
    }
}