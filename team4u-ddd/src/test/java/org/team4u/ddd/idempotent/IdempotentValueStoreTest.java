package org.team4u.ddd.idempotent;

import org.team4u.ddd.DbTest;
import org.team4u.ddd.infrastructure.persistence.mybatis.IdempotentValueMapper;
import org.team4u.ddd.infrastructure.persistence.mybatis.MybatisIdempotentValueStore;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.team4u.core.error.IdempotentException;
import org.team4u.ddd.TestUtil;

public class IdempotentValueStoreTest extends DbTest {

    @Autowired
    private IdempotentValueMapper mapper;

    @Test
    public void append() {
        store().append(new IdempotentValue(TestUtil.TEST_ID, TestUtil.TEST_ID));
        try {
            store().append(new IdempotentValue(TestUtil.TEST_ID, TestUtil.TEST_ID));
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(IdempotentException.class, e.getClass());
        }
    }

    private IdempotentValueStore store() {
        return new MybatisIdempotentValueStore(mapper);
    }

    @Override
    protected String[] ddlResourcePaths() {
        return new String[]{"sql/idempotent_value.sql"};
    }
}