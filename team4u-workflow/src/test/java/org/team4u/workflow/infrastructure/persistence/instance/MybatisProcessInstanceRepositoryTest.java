package org.team4u.workflow.infrastructure.persistence.instance;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.team4u.workflow.domain.definition.ProcessDefinition;
import org.team4u.workflow.domain.instance.ProcessAssignee;
import org.team4u.workflow.domain.instance.ProcessInstance;
import org.team4u.workflow.domain.instance.ProcessInstanceRepository;
import org.team4u.workflow.infrastructure.DbTest;

import static org.team4u.workflow.TestUtil.*;

public class MybatisProcessInstanceRepositoryTest extends DbTest {

    @Autowired
    private ProcessInstanceRepository repository;

    @Test
    public void domainOf() {
        save();

        ProcessInstance instance = repository.domainOf(TEST);
        System.out.println(JSON.toJSONString(instance));

        Assert.assertFalse(instance.getAssignees().isEmpty());
        Assert.assertNotNull(instance.getCurrentNode());
    }

    @Test
    public void save() {
        ProcessInstance instance = newInstance();
        repository.save(instance);
        Assert.assertNotNull(instance.getId());
    }

    private ProcessInstance newInstance() {
        ProcessDefinition definition = definitionOf("simple");
        ProcessInstance instance = new ProcessInstance(
                TEST,
                TEST,
                definition.getProcessDefinitionId(),
                staticNode("created"),
                TEST
        );

        instance.changeCurrentNodeTo(
                action("submit"),
                staticNode("pending"),
                TEST, null
        );

        ProcessAssignee assignee = new ProcessAssignee("pending", TEST1);
        assignee.handle(definition.actionOf("reject"));
        instance.setAssignees(CollUtil.newHashSet(assignee));

        return instance;
    }

    @Override
    protected String[] ddlResourcePaths() {
        return new String[]{"sql/process_instance.sql"};
    }
}