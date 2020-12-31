package org.team4u.workflow.domain.instance.node.handler;

import org.team4u.workflow.domain.instance.node.handler.bean.ProcessBeanHandlers;

public class TestBeanActionChoiceNodeHandler extends BeanActionChoiceNodeHandler {
    public TestBeanActionChoiceNodeHandler() {
        super(new ProcessBeanHandlers());
    }
}