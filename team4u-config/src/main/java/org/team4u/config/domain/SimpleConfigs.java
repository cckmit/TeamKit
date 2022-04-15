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

    /**
     * 获取指定配置项
     *
     * @param configType 配置类型或配置前缀
     * @param configKey  配置项key
     * @return 配置项
     */
    public SimpleConfig filter(String configType, String configKey) {
        return value.stream()
                .filter(it -> StrUtil.equals(it.getConfigId().getConfigType(), configType))
                .filter(it -> StrUtil.equals(it.getConfigId().getConfigKey(), configKey))
                .filter(SimpleConfig::isEnabled)
                .findFirst()
                .orElse(null);
    }

    /**
     * 获取指定配置类型的所有配置项
     *
     * @param configType 配置类型或配置前缀
     * @return 配置项集合
     */
    public SimpleConfigs filter(String configType) {
        return new SimpleConfigs(
                value.stream()
                        .filter(it -> StrUtil.equals(it.getConfigId().getConfigType(), configType))
                        .filter(SimpleConfig::isEnabled)
                        .collect(Collectors.toList())
        );
    }

    public String value(SimpleConfigId configId) {
        return value(configId.getConfigType(), configId.getConfigKey());
    }

    /**
     * 获取指定配置项对应的值
     *
     * @param configType 配置类型或配置前缀
     * @param configKey  配置项key
     * @return 配置值
     */
    public String value(String configType, String configKey) {
        SimpleConfig config = filter(configType, configKey);
        if (config == null) {
            return null;
        }

        return config.getConfigValue();
    }

    /**
     * 复制当前配置项集合
     *
     * @return 新的配置项集合
     */
    public SimpleConfigs copy() {
        return new SimpleConfigs(
                value.stream()
                        .map(it -> new SimpleConfig(
                                it.getConfigId(),
                                it.getConfigValue(),
                                it.getDescription(),
                                it.getSequenceNo(),
                                it.isEnabled()
                        ))
                        .collect(Collectors.toList())
        );
    }

    /**
     * 将配置项集合转换为Map
     *
     * @return key=configId,value=configValue的Map
     */
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