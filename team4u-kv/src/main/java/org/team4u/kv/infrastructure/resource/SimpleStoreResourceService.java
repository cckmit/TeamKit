package org.team4u.kv.infrastructure.resource;

import cn.hutool.core.lang.ConsistentHash;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.team4u.kv.resource.StoreResource;
import org.team4u.kv.resource.StoreResourceService;

import java.util.ArrayList;
import java.util.List;

/**
 * 简单存储资源服务
 * <p>
 * 采用一致性哈希算法进行资源分配
 *
 * @author jay.wu
 */
public class SimpleStoreResourceService implements StoreResourceService {

    private final Config config;

    private List<StoreResource> resources;
    private ConsistentHash<StoreResource> resourceSelector;

    public SimpleStoreResourceService(Config config) {
        this.config = config;

        reload();
    }

    @Override
    public StoreResource select(String key) {
        return resourceSelector.get(key);
    }

    @Override
    public List<StoreResource> resources() {
        return resources;
    }

    @Override
    public synchronized void reload() {
        List<StoreResource> result = new ArrayList<>();
        for (int i = 0; i < config.getResourceCount(); i++) {
            StoreResource resource = new StoreResource(i + "", config.getResourceType(), config.getResourceName());
            result.add(resource);
        }

        resources = result;
        resourceSelector = new ConsistentHash<>(config.getMaxResourceCount(), resources);
    }

    public Config config() {
        return config;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Config {
        /**
         * 资源类型
         */
        private String resourceType;
        /**
         * 资源名称
         */
        private String resourceName;
        /**
         * 实际资源数量
         */
        private int resourceCount = 1;
        /**
         * 最大资源数量
         */
        private int maxResourceCount = 1;
    }
}