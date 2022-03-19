package org.team4u.config.domain;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

/**
 * 配置项标识
 *
 * @author jay.wu
 */
@Data
public class SimpleConfigId {

    private final static char SEPARATOR = '|';
    /**
     * 配置类型
     */
    private final String configType;
    /**
     * 配置键
     */
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