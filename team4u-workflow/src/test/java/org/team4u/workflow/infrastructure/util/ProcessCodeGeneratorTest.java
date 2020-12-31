package org.team4u.workflow.infrastructure.util;

import cn.hutool.core.io.FileUtil;
import org.junit.Assert;
import org.junit.Test;
import org.team4u.template.TemplateFunctionService;
import org.team4u.template.infrastructure.BeetlTemplateEngine;
import org.team4u.workflow.TestUtil;

public class ProcessCodeGeneratorTest {

    private final ProcessCodeGenerator codeGenerator = new ProcessCodeGenerator(
            new ProcessCodeGenerator.Config(),
            new BeetlTemplateEngine(new TemplateFunctionService())
    );

    @Test
    public void enumCodeForNodes() {
        String code = codeGenerator.enumCodeForNodes(TestUtil.definitionOf("vms"));
        Assert.assertEquals(FileUtil.readUtf8String("code/vmsProcessNode.txt"), code);

    }

    @Test
    public void enumCodeForActions() {
        String code = codeGenerator.enumCodeForActions(TestUtil.definitionOf("vms"));
        Assert.assertEquals(FileUtil.readUtf8String("code/vmsProcessAction.txt"), code);
    }
}