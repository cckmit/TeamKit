package org.team4u.config.infrastructure.persistence.mybatis;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.team4u.config.TestUtil;
import org.team4u.config.domain.SimpleConfigs;
import org.team4u.config.infrastructure.BeanConfig;
import org.team4u.test.spring.SpringDbTest;

import static org.team4u.config.TestUtil.checkConfigByDb;

@ContextConfiguration(classes = BeanConfig.class)
public class MybatisSimpleConfigRepositoryTest extends SpringDbTest {

    @Autowired
    private MybatisSimpleConfigRepository repository;

    @Test
    public void allConfigs() {
        SimpleConfigs configs = repository.allConfigs();
        Assert.assertEquals(1, configs.size());
        checkConfigByDb(configs.getValue().get(0));
    }

    @Override
    protected String[] ddlResourcePaths() {
        return TestUtil.ddlResourcePaths();
    }
}