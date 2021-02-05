package org.team4u.kv;

import cn.hutool.core.thread.ThreadUtil;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;
import org.team4u.kv.infrastructure.repository.memory.InMemoryKeyValueRepository;

public abstract class KeyValueCleanerTest {

    @Test
    @Transactional(value = "txManager")
    public void testExpirationKv() {
        KeyValueService keyValueService = newKeyValueService();
        keyValueService.put("T", "x", "1", 500);
        keyValueService.put("T", "y", "1", 500);

        Assert.assertEquals("1", keyValueService.get("T", "x", String.class));
        Assert.assertEquals("1", keyValueService.get("T", "y", String.class));

        ThreadUtil.sleep(500);

        ExtKeyValueCleaner cleaner = newKeyValueCleaner(keyValueService, 1, 1);

        cleaner.onRun();
        Assert.assertEquals(1, cleaner.getRemoveCount());

        cleaner.onRun();
        Assert.assertEquals(1, cleaner.getRemoveCount());

        cleaner.onRun();
        Assert.assertEquals(0, cleaner.getRemoveCount());
    }

    private ExtKeyValueCleaner newKeyValueCleaner(
            KeyValueService keyValueService,
            int maxBatchSize,
            int intervalMillis
    ) {
        ExtKeyValueCleaner cleaner = new ExtKeyValueCleaner(
                new KeyValueCleaner.Config()
                        .setMaxBatchSize(maxBatchSize)
                        .setIntervalMillis(intervalMillis),
                // 隔离锁过期的清理，防止影响正常业务清理测试
                newLockService()
        );

        cleaner.addKeyValueRepository(keyValueService.keyValueRepository());
        return cleaner;
    }

    private KeyValueService newKeyValueService() {
        return new KeyValueService(keyValueRepository(), null);
    }

    private SimpleLockService newLockService() {
        return new SimpleLockService("LOCK",
                new KeyValueService(new InMemoryKeyValueRepository(), null)
        );
    }

    protected abstract KeyValueRepository keyValueRepository();
}