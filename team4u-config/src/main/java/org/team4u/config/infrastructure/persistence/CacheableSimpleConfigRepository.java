package org.team4u.config.infrastructure.persistence;

import cn.hutool.core.util.ObjectUtil;
import lombok.Getter;
import org.team4u.base.lang.lazy.LazyRefreshSupplier;
import org.team4u.config.domain.SimpleConfigComparator;
import org.team4u.config.domain.SimpleConfigRepository;
import org.team4u.config.domain.SimpleConfigs;

/**
 * 可缓存的配置项资源库
 *
 * @author jay.wu
 */
public class CacheableSimpleConfigRepository implements SimpleConfigRepository {

    @Getter
    private final Config config;

    private final SimpleConfigComparator simpleConfigComparator;
    private final SimpleConfigRepository delegateConfigRepository;

    private final LazyRefreshSupplier<SimpleConfigs> configsSupplier;


    public CacheableSimpleConfigRepository(Config config, SimpleConfigRepository delegateConfigRepository) {
        this.config = config;
        this.delegateConfigRepository = delegateConfigRepository;
        this.simpleConfigComparator = new SimpleConfigComparator();

        this.configsSupplier = buildConfigsSupplier();
    }

    private LazyRefreshSupplier<SimpleConfigs> buildConfigsSupplier() {
        return LazyRefreshSupplier.of(
                LazyRefreshSupplier.Config.builder()
                        .name(getClass().getSimpleName() + "|configSupplier")
                        .refreshIntervalMillis(config.getMaxEffectiveSec() * 1000L)
                        .build(),
                this::loadAndCompare
        );
    }

    @Override
    public SimpleConfigs allConfigs() {
        return configsSupplier.get();
    }

    private SimpleConfigs loadAndCompare() {
        SimpleConfigs newConfigs = delegateConfigRepository.allConfigs();
        SimpleConfigs oldConfigs = ObjectUtil.defaultIfNull(configsSupplier.value(), SimpleConfigs.EMPTY);

        simpleConfigComparator.compare(oldConfigs.getValue(), newConfigs.getValue());

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