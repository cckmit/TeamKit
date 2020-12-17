package org.team4u.workflow.domain.instance.node.handler;

import com.alibaba.fastjson.JSON;
import org.junit.Assert;
import org.junit.Test;
import org.team4u.selector.application.SelectorAppService;
import org.team4u.selector.domain.config.entity.SelectorConfig;
import org.team4u.selector.domain.selector.entity.expression.ExpressionSelectorFactory;
import org.team4u.template.TemplateFunctionService;
import org.team4u.template.infrastructure.BeetlTemplateEngine;
import org.team4u.workflow.domain.definition.node.DynamicChoiceNode;
import org.team4u.workflow.domain.instance.ProcessInstance;

import static org.team4u.workflow.TestUtil.*;

public class DynamicChoiceNodeHandlerTest {

    @Test
    public void handle() {
        DynamicChoiceNodeHandler handler = new DynamicChoiceNodeHandler(
                new SelectorAppService().registerSelectorFactory(
                        new ExpressionSelectorFactory(
                                new BeetlTemplateEngine(new TemplateFunctionService())
                        )
                )
        );

        ProcessInstance instance = newInstance();
        ProcessNodeHandler.Context c = context(
                instance,
                null,
                null,
                new DynamicChoiceNode(TEST, TEST, selectorConfig())
        );
        c.putExt("a", 2);
        Assert.assertEquals("y", handler.handle(c));

        c.putExt("a", 1);
        Assert.assertEquals("x", handler.handle(c));
    }

    private SelectorConfig selectorConfig() {
        String json = "{\"type\":\"expression\",\"body\":[{\"x\":\"${ext.a == 1}\"},{\"y\":\"${ext.a == 2}\"}]}";
        return JSON.parseObject(json, SelectorConfig.class);
    }
}