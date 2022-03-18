package org.team4u.kv;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.team4u.kv.infrastructure.repository.file.SimpleFileKeyValueRepository;
import org.team4u.kv.model.KeyValue;
import org.team4u.kv.model.KeyValueId;

public class SimpleFileKeyValueRepositoryTest extends KeyValueServiceTest {

    @Before
    @After
    public void setUp() throws Exception {
        FileUtil.del("test_db.txt");
    }

    @Override
    protected KeyValueRepository keyValueRepository() {
        return new SimpleFileKeyValueRepository(SimpleFileKeyValueRepository.Config.builder()
                .dbPath("test_db.txt")
                .flushIntervalMillis(50)
                .build());
    }

    @Test
    public void db() {
        KeyValueRepository r1 = keyValueRepository();
        KeyValueId id = new KeyValueId("1", "1");
        r1.save(new KeyValue().setKeyValueId(id).setValue("1"));

        ThreadUtil.sleep(300);

        KeyValueRepository r2 = keyValueRepository();
        KeyValue kv = r2.byId(id);
        Assert.assertEquals("1", kv.getValue());
    }
}
