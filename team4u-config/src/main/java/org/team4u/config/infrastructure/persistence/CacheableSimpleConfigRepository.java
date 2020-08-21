package org.team4u.config.infrastructure.persistence;

import org.team4u.config.domain.SimpleConfig;
import org.team4u.config.domain.SimpleConfigRepository;
import org.team4u.config.domain.SimpleDiffComparator;

import java.util.List;

public class CacheableSimpleConfigRepository implements SimpleConfigRepository {

    private final Config config;

    private final SimpleDiffComparator simpleDiffComparator;

    private final SimpleConfigRepository delegateConfigRepository;

    private List<SimpleConfig> allConfigs;
    private long lastUpdateTimeMillis;

    public CacheableSimpleConfigRepository(Config config, SimpleConfigRepository delegateConfigRepository) {
        this.config = config;
        this.delegateConfigRepository = delegateConfigRepository;
        this.simpleDiffComparator = new SimpleDiffComparator();

        refresh();
    }

    @Override
    public List<SimpleConfig> allConfigs() {
        if (isExpired()) {
            List<SimpleConfig> oldConfigs = allConfigs;

            refresh();

            simpleDiffComparator.compare(oldConfigs, allConfigs);
        }

        return allConfigs;
    }

    private void refresh() {
        allConfigs = delegateConfigRepository.allConfigs();
        lastUpdateTimeMillis = System.currentTimeMillis();
    }

    private boolean isExpired() {
        return System.currentTimeMillis() - lastUpdateTimeMillis > config.getMaxEffectiveSec() * 1000;
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