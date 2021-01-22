package org.team4u.workflow.domain.instance.node.handler;

import com.alibaba.fastjson.JSON;
import org.junit.Assert;
import org.junit.Test;
import org.team4u.selector.domain.selector.SelectorConfig;
import org.team4u.workflow.domain.definition.node.DynamicChoiceNode;
import org.team4u.workflow.domain.instance.ProcessInstance;

import static org.team4u.workflow.TestUtil.*;

public class DynamicChoiceNodeHandlerTest {

    @Test
    public void handle() {
        DynamicChoiceNodeHandler handler = new TestDynamicChoiceNodeHandler();

        ProcessInstance instance = newInstance();
        ProcessNodeHandlerContext c = contextBuilder().withInstance(instance)
                .withNode(new DynamicChoiceNode(TEST, TEST, selectorConfig()))
                .build();
        c.ext("a", 2);
        Assert.assertEquals("y", handler.handle(c));

        c.ext("a", 1);
        Assert.assertEquals("x", handler.handle(c));
    }

    private SelectorConfig selectorConfig() {
        String json = "{\"type\":\"expression\",\"body\":[{\"x\":\"${ext.a == 1}\"},{\"y\":\"${ext.a == 2}\"}]}";
        return JSON.parseObject(json, SelectorConfig.class);
    }
}