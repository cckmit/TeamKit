package org.team4u.config.domain;

import cn.hutool.core.util.StrUtil;

import java.util.Objects;

public class SimpleConfigId {

    private final static char SEPARATOR = '|';

    private final String configType;

    private final String configKey;

    public SimpleConfigId(String configType, String configKey) {
        this.configType = configType;
        this.configKey = configKey;
    }

    public String getConfigType() {
        return configType;
    }

    public String getConfigKey() {
        return configKey;
    }

    public static SimpleConfigId of(String id) {
        String[] typeAndKey = StrUtil.splitToArray(id, SEPARATOR);
        return new SimpleConfigId(typeAndKey[0], typeAndKey[1]);
    }

    @Override
    public String toString() {
        return configType + SEPARATOR + configKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleConfigId that = (SimpleConfigId) o;
        return configType.equals(that.configType) &&
                configKey.equals(that.configKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(configType, configKey);
    }
}