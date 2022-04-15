package org.team4u.config.domain.repository;

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

    public void refresh() {
        configsSupplier.refresh();
    }

    private SimpleConfigs loadAndCompare() {
        SimpleConfigs newConfigs = delegateConfigRepository.allConfigs();

        // 配置项比对
        compareAndPublishEvents(newConfigs);

        // 需要复制副本，防止外部修改值导致比对失效
        return newConfigs.copy();
    }

    private void compareAndPublishEvents(SimpleConfigs newConfigs) {
        // 首次初始化不做比对
        if (configsSupplier.value() == null) {
            return;
        }

        SimpleConfigs oldConfigs = configsSupplier.value();

        simpleConfigComparator.compareAndPublishEvents(
                oldConfigs.getValue(),
                newConfigs.getValue()
        );
    }

    public static class Config {
        /**
         * 缓存有效期（秒）
         */
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