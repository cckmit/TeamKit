package org.team4u.workflow.domain.instance.node.handler;

import org.team4u.workflow.domain.instance.node.handler.bean.ProcessBeanHandlers;

public class TestBeanProcessingNodeHandler extends BeanProcessingNodeHandler {
    public TestBeanProcessingNodeHandler() {
        super(new ProcessBeanHandlers());
    }
}