package org.team4u.workflow.infrastructure.persistence.definition;

import cn.hutool.core.io.FileUtil;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.team4u.ddd.infrastructure.persistence.memory.InMemoryEventStore;
import org.team4u.workflow.domain.definition.ProcessDefinition;
import org.team4u.workflow.domain.definition.ProcessDefinitionId;
import org.team4u.test.spring.SpringDbTest;
import org.team4u.workflow.infrastructure.BeanConfig;

import javax.annotation.PostConstruct;

@ContextConfiguration(classes = BeanConfig.class)
public class MybatisProcessDefinitionRepositoryTest extends SpringDbTest {

    @Autowired
    private ProcessDefinitionMapper definitionMapper;

    private MybatisProcessDefinitionRepository definitionRepository;

    @PostConstruct
    private void init() {
        definitionRepository = new MybatisProcessDefinitionRepository(
                new InMemoryEventStore(),
                definitionMapper
        );
    }

    @Test
    public void domainOf() {
        save();

        ProcessDefinition definition = definitionRepository.domainOf("simple");
        ProcessDefinitionRepositoryTestUtil.checkDefinition(definition);
    }

    public void save() {
        ProcessDefinition definition = ProcessDefinitionUtil.definitionOfJson(
                new ProcessDefinitionId("simple"),
                FileUtil.readUtf8String("simple.json")
        );

        definitionRepository.save(definition);

        Assert.assertNotNull(definition.getId());
    }

    @Override
    protected String[] ddlResourcePaths() {
        return new String[]{"sql/process_definition.sql"};
    }
}