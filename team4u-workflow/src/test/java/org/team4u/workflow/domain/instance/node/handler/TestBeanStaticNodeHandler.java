package org.team4u.workflow.domain.instance.node.handler;

import org.team4u.workflow.domain.instance.node.handler.bean.ProcessBeanHandlers;

public class TestBeanStaticNodeHandler extends BeanStaticNodeHandler {
    public TestBeanStaticNodeHandler() {
        super(new ProcessBeanHandlers());
    }
}