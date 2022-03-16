package org.team4u.base.config;

import cn.hutool.core.collection.CollUtil;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 组合配置服务
 *
 * @author jay.wu
 */
public class CompositeConfigService implements ConfigService {

    private final List<ConfigService> configServices;

    public CompositeConfigService(List<ConfigService> configServices) {
        this.configServices = configServices;
    }

    @Override
    public String get(String key) {
        return configServices.stream()
                .map(it -> it.get(key))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    @Override
    public Map<String, Object> allConfigs() {
        return CollUtil.reverse(configServices)
                .stream()
                .map(ConfigService::allConfigs)
                .reduce(new LinkedHashMap<>(), (a, b) -> {
                    a.putAll(b);
                    return a;
                });
    }
}
