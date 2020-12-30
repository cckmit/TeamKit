package org.team4u.workflow.infrastructure.persistence.instance;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import com.alibaba.fastjson.JSON;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.team4u.workflow.domain.definition.ProcessDefinition;
import org.team4u.workflow.domain.instance.ProcessAssignee;
import org.team4u.workflow.domain.instance.ProcessInstance;
import org.team4u.workflow.domain.instance.ProcessInstanceDetail;
import org.team4u.workflow.domain.instance.ProcessInstanceRepository;
import org.team4u.workflow.infrastructure.DbTest;

import static org.team4u.workflow.TestUtil.*;

public class MybatisProcessInstanceRepositoryTest extends DbTest {

    @Autowired
    private ProcessInstanceRepository repository;

    @Test
    public void domainOf() {
        insert();

        ProcessInstance instance = repository.domainOf(TEST);
        System.out.println(JSON.toJSONString(instance));

        Assert.assertFalse(instance.getAssignees().isEmpty());
        Assert.assertNotNull(instance.getCurrentNode());
        Assert.assertEquals(1, instance.concurrencyVersion());
        Assert.assertEquals(
                Dict.create().set("x", 1),
                instance.getProcessInstanceDetail().toDetailObject()
        );
    }

    @Test
    public void update() {
        insert();
        ProcessInstance instance = repository.domainOf(TEST);

        repository.save(instance);
        Assert.assertEquals(1, instance.concurrencyVersion());

        instance = repository.domainOf(TEST);
        Assert.assertEquals(1, instance.concurrencyVersion());
    }

    @Test
    public void insert() {
        ProcessInstance instance = newInstance();

        repository.save(instance);

        Assert.assertNotNull(instance.getId());
    }

    private ProcessInstance newInstance() {
        ProcessDefinition definition = definitionOf("simple");

        ProcessInstanceDetail item = new ProcessInstanceDetail(Dict.create().set("x", 1));
        ProcessInstance instance = new ProcessInstance(
                TEST,
                TEST,
                definition.getProcessDefinitionId(),
                staticNode("created"),
                TEST,
                item
        );

        instance.changeCurrentNodeTo(
                action("submit"),
                staticNode("pending"),
                TEST, null
        );

        ProcessAssignee assignee = new ProcessAssignee("pending", TEST1);
        assignee.handle(definition.availableActionOf("reject"));
        instance.setAssignees(CollUtil.newHashSet(assignee));

        return instance;
    }

    @Override
    protected String[] ddlResourcePaths() {
        return new String[]{"sql/process_instance.sql"};
    }
}