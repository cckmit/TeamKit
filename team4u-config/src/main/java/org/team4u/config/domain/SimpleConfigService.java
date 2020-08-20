package org.team4u.config.domain;

import cn.hutool.core.util.StrUtil;

import java.util.List;
import java.util.stream.Collectors;

public class SimpleConfigService {

    public <T> T configsTo(List<SimpleConfig> simpleConfigs, Class<T> toType, String configType) {
        List<SimpleConfig> filteredConfigs = availableConfigsOf(simpleConfigs, configType);

        return null;
    }

    public List<SimpleConfig> availableConfigsOf(List<SimpleConfig> simpleConfigs, String configType) {
        return simpleConfigs.stream()
                .filter(it -> StrUtil.equals(it.getConfigId().getConfigType(), configType))
                .filter(SimpleConfig::getEnabled)
                .collect(Collectors.toList());
    }
}