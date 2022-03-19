package org.team4u.config.infrastructure.persistence.jdbc;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.team4u.config.TestUtil;
import org.team4u.config.domain.SimpleConfigs;
import org.team4u.test.spring.DbTestBeanConfig;
import org.team4u.test.spring.SpringDbTest;

import static org.team4u.config.TestUtil.checkConfigByDb;

@ContextConfiguration(classes = DbTestBeanConfig.class)
public class JdbcSimpleConfigRepositoryTest extends SpringDbTest {

    @Test
    public void allConfigs() {
        SimpleConfigs configs = repository().allConfigs();
        Assert.assertEquals(1, configs.size());
        checkConfigByDb(configs.getValue().get(0));
    }

    private JdbcSimpleConfigRepository repository() {
        return new JdbcSimpleConfigRepository(jdbcTemplate().getDataSource());
    }

    @Override
    protected void executeXDdl(String ddl) throws Exception {
        repository().getDb().execute(ddl);
    }

    @Override
    protected String[] ddlResourcePaths() {
        return TestUtil.ddlResourcePaths();
    }
}