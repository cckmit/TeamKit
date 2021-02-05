package org.team4u.kv;

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
import org.team4u.kv.infrastructure.resource.SimpleStoreResourceService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = BeanConfig.class)
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class
})
@ActiveProfiles(value = "test")
public class DbKeyValueServiceTest extends KeyValueServiceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SimpleStoreResourceService storeResourceService;

    @Autowired
    private KeyValueRepository dbKeyValueRepository;

    @Before
    public void initDdl() {
        String ddl = FileUtil.readUtf8String("sql/kv.sql");

        for (int i = 0; i < storeResourceService.config().getResourceCount(); i++) {
            jdbcTemplate.execute(ddl.replace("key_value", "key_value_" + i)
                    .replace("idx_type_name", "idx_type_name_" + i));
        }
    }

    @Override
    protected KeyValueRepository keyValueRepository() {
        return dbKeyValueRepository;
    }
}