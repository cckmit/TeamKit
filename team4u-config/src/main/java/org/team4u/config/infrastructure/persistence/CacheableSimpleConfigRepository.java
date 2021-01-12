package org.team4u.config.infrastructure.persistence;

import org.team4u.config.domain.SimpleConfig;
import org.team4u.config.domain.SimpleConfigComparator;
import org.team4u.config.domain.SimpleConfigRepository;

import java.util.List;

public class CacheableSimpleConfigRepository implements SimpleConfigRepository {

    private final Config config;

    private final SimpleConfigComparator simpleConfigComparator;

    private final SimpleConfigRepository delegateConfigRepository;

    private List<SimpleConfig> allConfigs;
    private long lastUpdateTimeMillis;

    public CacheableSimpleConfigRepository(Config config, SimpleConfigRepository delegateConfigRepository) {
        this.config = config;
        this.delegateConfigRepository = delegateConfigRepository;
        this.simpleConfigComparator = new SimpleConfigComparator();

        refresh();
    }

    @Override
    public List<SimpleConfig> allConfigs() {
        if (isExpired()) {
            List<SimpleConfig> oldConfigs = allConfigs;

            refresh();

            simpleConfigComparator.compare(oldConfigs, allConfigs);
        }

        return allConfigs;
    }

    private void refresh() {
        allConfigs = delegateConfigRepository.allConfigs();
        lastUpdateTimeMillis = System.currentTimeMillis();
    }

    private boolean isExpired() {
        return System.currentTimeMillis() - lastUpdateTimeMillis > config.getMaxEffectiveSec() * 1000L;
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