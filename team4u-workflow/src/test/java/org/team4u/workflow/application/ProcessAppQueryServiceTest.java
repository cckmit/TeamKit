package org.team4u.workflow.application;

import cn.hutool.db.PageResult;
import com.alibaba.fastjson.JSON;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.team4u.test.spring.SpringDbTest;
import org.team4u.workflow.application.command.InstancesQuery;
import org.team4u.workflow.domain.definition.ProcessDefinition;
import org.team4u.workflow.domain.instance.ProcessAssignee;
import org.team4u.workflow.domain.instance.ProcessInstance;
import org.team4u.workflow.domain.instance.ProcessInstanceRepository;
import org.team4u.workflow.infrastructure.BeanConfig;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.team4u.workflow.TestUtil.*;

@ContextConfiguration(classes = BeanConfig.class)
public class ProcessAppQueryServiceTest extends SpringDbTest {

    @Autowired
    private ProcessInstanceRepository repository;
    @Autowired
    private ProcessAppQueryService queryService;

    public static ProcessInstance newInstance(String id, String... assignees) {
        ProcessDefinition definition = definitionOf("simple");

        return new ProcessInstance(
                id,
                TEST,
                TEST,
                definition.getProcessDefinitionId(),
                staticNode("created"),
                TEST,
                null
        ).setAssignees(
                Optional.ofNullable(assignees)
                        .map(it -> Arrays.stream(assignees)
                                .map(assignee -> new ProcessAssignee("created", assignee))
                                .collect(Collectors.toSet()))
                        .orElse(Collections.emptySet())
        );
    }

    @Test
    public void instancesOfPending() {
        org.apache.ibatis.logging.LogFactory.useStdOutLogging();

        ProcessInstance instance = insert(TEST);
        List<ProcessInstance> instances = queryService.instancesOfPending(
                new InstancesQuery().setOperator(instance.getCreateBy())
        );

        Assert.assertEquals(1, instances.size());
        System.out.println(JSON.toJSONString(instances));
    }

    @Test
    public void instancesOfHistory() {
    }

    @Test
    public void instancesOfApply() {
        org.apache.ibatis.logging.LogFactory.useStdOutLogging();

        insert(TEST + 1);
        ProcessInstance instance = insert(TEST + 2);
        PageResult<ProcessInstance> instances = queryService.instancesOfApply(
                new InstancesQuery().setOperator(instance.getCreateBy())
                        .setProcessInstanceName("t")
                        .setPageSize(1)
                        .setPageNumber(2)
        );

        System.out.println(JSON.toJSONString(instances));
        Assert.assertEquals(2, instances.getTotal());
        Assert.assertEquals(TEST + 1, instances.get(0).getProcessInstanceId());
    }

    private ProcessInstance insert(String id) {
        ProcessInstance instance = newInstance(id, TEST);
        repository.save(instance);
        return instance;
    }

    @Override
    protected String[] ddlResourcePaths() {
        return new String[]{"sql/process_instance.sql"};
    }
}