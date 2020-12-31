package org.team4u.workflow.infrastructure.util;

import org.junit.Assert;
import org.junit.Test;
import org.team4u.workflow.TestUtil;

public class ProcessMermaidRenderTest {

    @Test
    public void simple() {
        ProcessMermaidRender render = new ProcessMermaidRender(TestUtil.definitionOf("simple"));

        Assert.assertEquals("graph TD\n" +
                        "    created[已保存]-.->createdActionNode{已保存动作选择器}\n" +
                        "    createdActionNode-->|保存|created[已保存]\n" +
                        "    createdActionNode-->|提交|pending[待审核]\n" +
                        "    createdActionNode-->|表达式测试|dynamic{表达式选择器}\n" +
                        "    dynamic-->|\"${ext.a == 1}\"|created[已保存]\n" +
                        "    dynamic-->|\"${ext.a == 2}\"|pending[待审核]\n" +
                        "    pending[待审核]-.->pendingActionNode{待审核动作选择器}\n" +
                        "    pendingActionNode-->|拒绝|rejected[审批拒绝]\n" +
                        "    pendingActionNode-->|同意|completed[审批完成]\n",
                render.render());
    }

    @Test
    public void vms() {
        ProcessMermaidRender render = new ProcessMermaidRender(TestUtil.definitionOf("vms"));

        Assert.assertEquals("graph TD\n" +
                        "    created-->|动作1|beanProcessing(内部处理)\n" +
                        "    created-->|动作2|completed[完成]\n" +
                        "    beanProcessing(内部处理)-->beanChoiceNode{bea动作选择器1}\n" +
                        "    beanChoiceNode-->|动作1|beanChoiceNode2{bea动作选择器2}\n" +
                        "    beanChoiceNode-->|动作2|completed[完成]\n" +
                        "    beanChoiceNode2-. 动作1 .->simpleChoice{结束流程}\n" +
                        "    beanChoiceNode2-->|动作2|created{创建}\n" +
                        "    simpleChoice-->|动作1|completed[完成]\n",
                render.render());
    }
}