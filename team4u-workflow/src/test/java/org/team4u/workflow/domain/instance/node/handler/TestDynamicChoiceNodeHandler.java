package org.team4u.workflow.domain.instance.node.handler;

import static org.team4u.workflow.TestUtil.selectorAppService;

public class TestDynamicChoiceNodeHandler extends DynamicChoiceNodeHandler {

    public TestDynamicChoiceNodeHandler() {
        super(selectorAppService());
    }
}
