package org.team4u.config.infrastructure.persistence;

import cn.hutool.core.date.DateUtil;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.team4u.config.TestUtil;
import org.team4u.config.domain.SimpleConfig;
import org.team4u.config.infrastructure.BeanConfig;
import org.team4u.test.spring.SpringDbTest;

import java.util.List;

@ContextConfiguration(classes = BeanConfig.class)
public class MybatisSimpleConfigRepositoryTest extends SpringDbTest {

    @Autowired
    private MybatisSimpleConfigRepository repository;

    @Test
    public void allConfigs() {
        List<SimpleConfig> configs = repository.allConfigs();
        Assert.assertEquals(1, configs.size());
        checkConfig(configs.get(0));
    }

    private void checkConfig(SimpleConfig config) {
        Assert.assertEquals(1, config.getSequenceNo());
        Assert.assertTrue(config.getEnabled());
        Assert.assertEquals(TestUtil.TEST_ID, config.getConfigId().getConfigKey());
        Assert.assertEquals(TestUtil.TEST_ID, config.getConfigId().getConfigType());
        Assert.assertEquals(TestUtil.TEST_ID, config.getConfigValue());
        Assert.assertEquals(TestUtil.TEST_ID, config.getCreatedBy());
        Assert.assertEquals(TestUtil.TEST_ID, config.getUpdatedBy());
        Assert.assertEquals(TestUtil.TEST_ID, config.getDescription());
        Assert.assertEquals("2020-08-21 00:00:00", DateUtil.formatDateTime(config.getCreateTime()));
        Assert.assertEquals("2020-08-21 00:00:00", DateUtil.formatDateTime(config.getUpdateTime()));
    }

    @Override
    protected String[] ddlResourcePaths() {
        return new String[]{"sql/system_config.sql", "sql/system_config_data.sql"};
    }
}