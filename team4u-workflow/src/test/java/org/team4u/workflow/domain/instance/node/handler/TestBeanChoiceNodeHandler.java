package org.team4u.workflow.domain.instance.node.handler;

import org.team4u.workflow.domain.instance.node.handler.bean.ProcessBeanHandlers;

public class TestBeanChoiceNodeHandler extends BeanChoiceNodeHandler {
    public TestBeanChoiceNodeHandler() {
        super(new ProcessBeanHandlers());
    }
}