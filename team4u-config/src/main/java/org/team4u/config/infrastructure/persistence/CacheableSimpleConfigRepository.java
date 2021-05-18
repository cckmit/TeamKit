package org.team4u.config.infrastructure.persistence;

import org.team4u.base.lang.LongTimeThread;
import org.team4u.config.domain.SimpleConfig;
import org.team4u.config.domain.SimpleConfigComparator;
import org.team4u.config.domain.SimpleConfigRepository;

import java.util.List;

public class CacheableSimpleConfigRepository extends LongTimeThread implements SimpleConfigRepository {

    private final SimpleConfigComparator simpleConfigComparator;
    private final SimpleConfigRepository delegateConfigRepository;

    private final Config config;
    private List<SimpleConfig> allConfigs;
    private long lastUpdateTimeMillis;

    public CacheableSimpleConfigRepository(Config config, SimpleConfigRepository delegateConfigRepository) {
        this.config = config;
        this.delegateConfigRepository = delegateConfigRepository;
        this.simpleConfigComparator = new SimpleConfigComparator();

        refresh();

        start();
    }

    @Override
    public List<SimpleConfig> allConfigs() {
        return allConfigs;
    }

    private void refreshAndCompare() {
        List<SimpleConfig> oldConfigs = allConfigs;

        refresh();

        simpleConfigComparator.compare(oldConfigs, allConfigs);
    }

    private void refresh() {
        allConfigs = delegateConfigRepository.allConfigs();
        lastUpdateTimeMillis = System.currentTimeMillis();
    }

    private boolean isExpired() {
        return System.currentTimeMillis() - lastUpdateTimeMillis > config.getMaxEffectiveSec() * 1000L;
    }

    @Override
    protected void onRun() {
        // 异步刷新，不阻塞业务逻辑
        if (isExpired()) {
            refreshAndCompare();
        }
    }

    @Override
    protected Number runIntervalMillis() {
        return config.getMaxEffectiveSec();
    }

    public static class Config {
        private int maxEffectiveSec = 60;

        public int getMaxEffectiveSec() {
            return maxEffectiveSec;
        }

        public Config setMaxEffectiveSec(int maxEffectiveSec) {
            this.maxEffectiveSec = maxEffectiveSec;
            return this;
        }
    }
}