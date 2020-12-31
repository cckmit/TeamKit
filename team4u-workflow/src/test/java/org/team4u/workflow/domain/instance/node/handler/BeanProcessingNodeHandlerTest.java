package org.team4u.workflow.domain.instance.node.handler;

import org.junit.Assert;
import org.junit.Test;
import org.team4u.workflow.TestUtil;
import org.team4u.workflow.domain.definition.node.BeanProcessingNode;

import static org.team4u.workflow.TestUtil.TEST;

public class BeanProcessingNodeHandlerTest {

    private final BeanProcessingNodeHandler handler = new TestBeanProcessingNodeHandler();

    @Test
    public void handle() {
        ProcessNodeHandlerContext context = TestUtil.contextBuilder()
                .withInstance(TestUtil.newInstance())
                .withNode(new BeanProcessingNode(
                        TEST,
                        TEST,
                        TEST,
                        TEST
                ))
                .build();
        String nextNodeId = handler.handle(context);

        Assert.assertEquals(TEST, context.ext(TEST));
        Assert.assertEquals(TEST, nextNodeId);
    }
}