package org.team4u.config.domain;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 配置项集合
 *
 * @author jay.wu
 */
@Data
public class SimpleConfigs {

    public static final SimpleConfigs EMPTY = new SimpleConfigs();

    private final List<SimpleConfig> value;

    public SimpleConfigs() {
        this(Collections.emptyList());
    }

    public SimpleConfigs(List<SimpleConfig> value) {
        this.value = value;
    }

    public SimpleConfig filter(String configType, String configKey) {
        return value.stream()
                .filter(it -> StrUtil.equals(it.getConfigId().getConfigType(), configType))
                .filter(it -> StrUtil.equals(it.getConfigId().getConfigKey(), configKey))
                .filter(SimpleConfig::getEnabled)
                .findFirst()
                .orElse(null);
    }

    public SimpleConfigs filter(String configType) {
        return new SimpleConfigs(
                value.stream()
                        .filter(it -> StrUtil.equals(it.getConfigId().getConfigType(), configType))
                        .filter(SimpleConfig::getEnabled)
                        .collect(Collectors.toList())
        );
    }

    public SimpleConfigs copy() {
        return new SimpleConfigs(
                value.stream()
                        .map(it -> new SimpleConfig(
                                it.getConfigId(),
                                it.getConfigValue(),
                                it.getDescription(),
                                it.getSequenceNo(),
                                it.getEnabled(),
                                it.getCreatedBy(),
                                it.getCreateTime()
                        ))
                        .collect(Collectors.toList())
        );
    }

    public Map<String, Object> toMap() {
        return value.stream().collect(Collectors.toMap(
                it -> it.getConfigId().toString(),
                SimpleConfig::getConfigValue
        ));
    }

    public SimpleConfig get(int index) {
        return value.get(index);
    }

    public int size() {
        return value.size();
    }
}