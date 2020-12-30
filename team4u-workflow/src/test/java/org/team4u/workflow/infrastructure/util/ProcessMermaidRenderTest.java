package org.team4u.workflow.infrastructure.util;

import org.junit.Assert;
import org.junit.Test;
import org.team4u.workflow.TestUtil;

public class ProcessMermaidRenderTest {

    @Test
    public void simple() {
        ProcessMermaidRender render = new ProcessMermaidRender(TestUtil.definitionOf("simple"));

        Assert.assertEquals("graph TD\n" +
                "    created[已保存]-->createdActionNode{已保存动作选择器}\n" +
                "    createdActionNode-->|保存|created[已保存]\n" +
                "    createdActionNode-->|提交|pending[待审核]\n" +
                "    createdActionNode-->|表达式测试|dynamic{表达式选择器}\n" +
                "    dynamic-->|\"${ext.a == 1}\"|created[已保存]\n" +
                "    dynamic-->|\"${ext.a == 2}\"|pending[待审核]\n" +
                "    pending[待审核]-->pendingActionNode{待审核动作选择器}\n" +
                "    pendingActionNode-->|拒绝|rejected[审批拒绝]\n" +
                "    pendingActionNode-->|同意|completed[审批完成]\n", render.toFlow().toString());
    }

    @Test
    public void vms() {
        ProcessMermaidRender render = new ProcessMermaidRender(TestUtil.definitionOf("vms"));

        Assert.assertEquals("graph TD\n" +
                        "    created[创建]-->createdChoiceNode{动作选择器}\n" +
                        "    createdChoiceNode-->|动作1|testChoiceNode{预期到test节点}\n" +
                        "    createdChoiceNode-->|动作2|created[创建]\n" +
                        "    testChoiceNode-->test[完成]\n",
                render.toFlow().toString());
    }
}