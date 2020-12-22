package org.team4u.workflow.domain.instance.node.handler;

import static org.team4u.workflow.TestUtil.TEST;

public class TestBeanHandler implements ProcessBeanHandler {

    @Override
    public String id() {
        return "test";
    }

    @Override
    public void handle(ProcessNodeHandlerContext context) {
        context.ext(TEST, TEST);
        context.ext(ProcessBeanHandler.EXT_NEXT_NODE_ID, TEST);
    }
}