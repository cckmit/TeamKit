package org.team4u.workflow.domain.instance.node.handler;

import org.junit.Assert;
import org.junit.Test;
import org.team4u.workflow.TestUtil;
import org.team4u.workflow.domain.definition.node.BeanNode;

import static org.team4u.workflow.TestUtil.TEST;

public class BeanNodeHandlerTest {

    @Test
    public void handle() {
        ProcessBeanHandlers handlers = new ProcessBeanHandlers();
        handlers.saveIdObject(new TestBeanHandler());
        BeanNodeHandler handler = new BeanNodeHandler(handlers);

        ProcessNodeHandlerContext context = TestUtil.contextBuilder()
                .withInstance(TestUtil.newInstance())
                .withNode(new BeanNode(TEST, TEST, TEST, null))
                .withAction(TestUtil.action(TEST))
                .build();
        handler.handle(context);

        Assert.assertEquals(TEST, context.ext(TEST));
    }

    public static class TestBeanHandler implements ProcessBeanHandler {

        @Override
        public String id() {
            return "test";
        }

        @Override
        public void handle(ProcessNodeHandlerContext context) {
            context.putExt(TEST, TEST);
        }
    }
}