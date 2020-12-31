package org.team4u.workflow.domain.instance.node.handler;

import cn.hutool.core.collection.CollUtil;
import org.junit.Assert;
import org.junit.Test;
import org.team4u.workflow.TestUtil;
import org.team4u.workflow.domain.definition.node.BeanActionChoiceNode;

import static org.team4u.workflow.TestUtil.*;

public class BeanActionChoiceNodeHandlerTest {

    private final BeanActionChoiceNodeHandler handler = new TestBeanActionChoiceNodeHandler();

    @Test
    public void handle() {
        ProcessNodeHandlerContext context = TestUtil.contextBuilder()
                .withInstance(TestUtil.newInstance())
                .withNode(new BeanActionChoiceNode(
                        TEST,
                        TEST,
                        CollUtil.newArrayList(actionNode(TEST, TEST)),
                        TEST
                ))
                .withDefinition(TestUtil.definitionOf("simple"))
                .withAction(action(TEST))
                .build();
        handler.handle(context);

        Assert.assertEquals(TEST, context.getAction().getActionId());
    }
}