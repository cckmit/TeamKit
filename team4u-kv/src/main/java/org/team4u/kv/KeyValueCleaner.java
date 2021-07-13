package org.team4u.kv;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.team4u.base.lang.LongTimeThread;
import org.team4u.base.log.LogMessageContext;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * kv定时清理器
 * <p>
 * 统一由单线程清理所有过期对象
 *
 * @author jay.wu
 */
public class KeyValueCleaner extends LongTimeThread {

    private final Log log = LogFactory.get();

    private static final String LOCK_ID = "KVC";

    private final Config config;
    private final SimpleLockService lockService;

    private final Set<KeyValueRepository> keyValueRepositories = new HashSet<>();

    public KeyValueCleaner(Config config, SimpleLockService lockService) {
        this.config = config;
        this.lockService = lockService;
    }

    @Override
    protected void onRun() {
        if (!lockService.tryLock(LOCK_ID, (int) config.getIntervalMillis() * 2)) {
            return;
        }

        try {
            // 执行清理
            int count = removeExpirationValues();

            log.info(LogMessageContext.createAndSet(this.getClass().getSimpleName(), "onRun")
                    .append("typeCount", keyValueRepositories.size())
                    .append("intervalMillis", config.getIntervalMillis())
                    .append("count", count)
                    .success().toString());
        } finally {
            lockService.unLock(LOCK_ID);
        }
    }

    @Override
    protected Number runIntervalMillis() {
        return config.getIntervalMillis();
    }

    protected int removeExpirationValues() {
        int count = 0;

        for (KeyValueRepository repository : keyValueRepositories) {
            try {
                count += repository.removeExpirationValues(config.getMaxBatchSize());
            } catch (Exception e) {
                log.error(e);
            }
        }

        return count;
    }

    /**
     * 添加需要清理的kv资源库
     */
    public void addKeyValueRepository(KeyValueRepository repository) {
        keyValueRepositories.add(repository);
    }

    public Set<KeyValueRepository> keyValueRepositories() {
        return Collections.unmodifiableSet(keyValueRepositories);
    }

    public static class Config {
        /**
         * 检查间隔(毫秒)
         */
        private long intervalMillis = TimeUnit.MINUTES.toMillis(1);
        private int maxBatchSize = 500;

        public long getIntervalMillis() {
            return intervalMillis;
        }

        public Config setIntervalMillis(long intervalMillis) {
            this.intervalMillis = intervalMillis;
            return this;
        }

        public int getMaxBatchSize() {
            return maxBatchSize;
        }

        public Config setMaxBatchSize(int maxBatchSize) {
            this.maxBatchSize = maxBatchSize;
            return this;
        }
    }
}