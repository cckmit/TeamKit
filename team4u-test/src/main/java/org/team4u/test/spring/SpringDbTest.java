package org.team4u.test.spring;

import cn.hutool.core.io.FileUtil;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

/**
 * 基于db的单元测试
 *
 * @author jay.wu
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class
})
@Transactional
@ActiveProfiles(value = "test")
public abstract class SpringDbTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Before
    public void initDdl() throws Exception {
        for (String ddlPath : ddlResourcePaths()) {
            String ddl = FileUtil.readUtf8String(ddlPath);
            executeXDdl(ddl);
        }
    }

    protected abstract String[] ddlResourcePaths();

    public JdbcTemplate jdbcTemplate() {
        return jdbcTemplate;
    }

    protected void executeXDdl(String ddl) throws  Exception {
        jdbcTemplate().execute(ddl);
    }
}