package org.team4u.workflow.domain.instance;

import org.junit.Assert;
import org.junit.Test;
import org.team4u.workflow.TestUtil;
import org.team4u.workflow.domain.definition.ProcessDefinition;

public class ProcessNodeHandlersTest {

    private final ProcessDefinition definition = TestUtil.definitionOf("simple_def.json");
    private final ProcessNodeHandlers handles = new ProcessNodeHandlers();

    @Test
    public void save() {
        ProcessInstance instance = TestUtil.newInstance().setCurrentNode(definition.rootNode());
        handles.handle(TestUtil.context(
                instance,
                TestUtil.action("save"),
                definition,
                null
        ));

        Assert.assertEquals(TestUtil.staticNode("created"), instance.getCurrentNode());
        Assert.assertEquals(
                "[node=created,action=save,nextNode=created,remark='test',createBy='test']",
                instance.getLogs().toString()
        );
    }

    @Test
    public void testSubmit() {
        ProcessInstance instance = submit();

        Assert.assertEquals(
                TestUtil.assigneeNode("pending", null, null),
                instance.getCurrentNode()
        );

        Assert.assertEquals(
                "[node=created,action=submit,nextNode=pending,remark='test',createBy='test']",
                instance.getLogs().toString()
        );
    }

    @Test
    public void reject() {
        ProcessInstance instance = submit();
        handles.handle(TestUtil.context(
                instance,
                TestUtil.action("reject"),
                definition,
                null
        ));

        Assert.assertEquals(TestUtil.staticNode("rejected"), instance.getCurrentNode());

        Assert.assertEquals(
                "node=pending,action=reject,nextNode=rejected,remark='test',createBy='test'",
                instance.getLogs().get(1).toString()
        );
    }

    @Test
    public void approve() {
        ProcessInstance instance = submit();
        handles.handle(TestUtil.context(
                instance,
                TestUtil.action("approve"),
                definition,
                null
        ));

        Assert.assertEquals(TestUtil.staticNode("completed"), instance.getCurrentNode());

        Assert.assertEquals(
                "node=pending,action=approve,nextNode=completed,remark='test',createBy='test'",
                instance.getLogs().get(1).toString()
        );
    }

    private ProcessInstance submit() {
        ProcessInstance instance = TestUtil.newInstance().setCurrentNode(definition.rootNode());
        handles.handle(TestUtil.context(
                instance,
                TestUtil.action("submit"),
                definition,
                null
        ));

        return instance;
    }
}