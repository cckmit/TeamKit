package org.team4u.config.infrastructure.persistence;

import cn.hutool.core.util.ObjectUtil;
import lombok.Getter;
import org.team4u.base.lang.lazy.LazyRefreshSupplier;
import org.team4u.config.domain.SimpleConfig;
import org.team4u.config.domain.SimpleConfigComparator;
import org.team4u.config.domain.SimpleConfigRepository;

import java.util.Collections;
import java.util.List;

public class CacheableSimpleConfigRepository implements SimpleConfigRepository {

    @Getter
    private final Config config;

    private final SimpleConfigComparator simpleConfigComparator;
    private final SimpleConfigRepository delegateConfigRepository;

    private final LazyRefreshSupplier<List<SimpleConfig>> refreshSupplier;


    public CacheableSimpleConfigRepository(Config config, SimpleConfigRepository delegateConfigRepository) {
        this.config = config;
        this.delegateConfigRepository = delegateConfigRepository;
        this.simpleConfigComparator = new SimpleConfigComparator();

        refreshSupplier = LazyRefreshSupplier.of(config.getMaxEffectiveSec(), this::loadAndCompare);
    }

    @Override
    public List<SimpleConfig> allConfigs() {
        return refreshSupplier.get();
    }

    private List<SimpleConfig> loadAndCompare() {
        List<SimpleConfig> newConfigs = delegateConfigRepository.allConfigs();
        List<SimpleConfig> oldConfigs = ObjectUtil.defaultIfNull(refreshSupplier.value(), Collections.emptyList());

        simpleConfigComparator.compare(oldConfigs, newConfigs);

        return newConfigs;
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