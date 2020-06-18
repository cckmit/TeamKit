package org.team4u.ddd;

import cn.hutool.core.io.FileUtil;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-test.xml"})
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class
})
@Transactional(value = "txManager")
@ActiveProfiles(value = "test")
public abstract class DbTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Before
    public void initDdl() {
        for (String ddlPath : ddlResourcePaths()) {
            String ddl = FileUtil.readUtf8String(ddlPath);
            jdbcTemplate.execute(ddl);
        }
    }

    protected abstract String[] ddlResourcePaths();

    public JdbcTemplate jdbcTemplate() {
        return jdbcTemplate;
    }
}