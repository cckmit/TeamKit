package org.team4u.config.domain;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

@Data
public class SimpleConfigId {

    private final static char SEPARATOR = '|';

    private final String configType;

    private final String configKey;

    public SimpleConfigId(String configType, String configKey) {
        this.configType = configType;
        this.configKey = configKey;
    }

    public static SimpleConfigId of(String id) {
        String[] typeAndKey = StrUtil.splitToArray(id, SEPARATOR);
        return new SimpleConfigId(typeAndKey[0], typeAndKey[1]);
    }

    @Override
    public String toString() {
        return configType + SEPARATOR + configKey;
    }
}