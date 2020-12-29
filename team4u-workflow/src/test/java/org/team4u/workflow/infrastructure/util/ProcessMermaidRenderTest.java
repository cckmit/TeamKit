package org.team4u.workflow.infrastructure.util;

import org.junit.Test;
import org.team4u.workflow.TestUtil;

public class ProcessMermaidRenderTest {

    @Test
    public void toFlow() {
        ProcessMermaidRender render = new ProcessMermaidRender(TestUtil.definitionOf("simple"));
        System.out.println(render.toFlow().toString());
    }
}