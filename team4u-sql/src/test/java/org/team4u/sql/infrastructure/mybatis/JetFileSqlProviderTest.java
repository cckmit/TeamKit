package org.team4u.sql.infrastructure.mybatis;

import cn.hutool.core.lang.Dict;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.team4u.test.spring.DbTestBeanConfig;
import org.team4u.test.spring.SpringDbTest;

@ContextConfiguration(classes = DbTestBeanConfig.class)
public class JetFileSqlProviderTest extends SpringDbTest {

    @Autowired
    private TestMapper testMapper;

    @Test
    public void eventOf() {
        StoredEventEntity entity = new StoredEventEntity().setEventId(1);
        testMapper.insert(entity);
        MybatisSqlProvider.extraParams(Dict.create().set("eventId", 1));
        Assert.assertEquals(1, testMapper.eventOf(entity.getId()).getEventId());
    }

    @Override
    protected String[] ddlResourcePaths() {
        return new String[]{"table/stored_event.sql"};
    }
}