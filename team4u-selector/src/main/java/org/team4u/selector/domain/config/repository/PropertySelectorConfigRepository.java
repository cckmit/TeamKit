package org.team4u.selector.domain.config.repository;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import org.team4u.selector.domain.config.entity.SelectorConfig;
import org.team4u.base.config.ConfigService;

/**
 * 基于配置的选择器资源库
 *
 * @author jay.wu
 */
public class PropertySelectorConfigRepository implements SelectorConfigRepository {

    private final Config config;
    private final ConfigService configService;

    public PropertySelectorConfigRepository(Config config, ConfigService configService) {
        this.config = config;
        this.configService = configService;
    }

    @Override
    public SelectorConfig selectorConfigOfId(String id) {
        String jsonString = configService.get(config.getPropertyPrefix() + id);
        if (StrUtil.isEmpty(jsonString)) {
            return null;
        }

        return JSONUtil.toBean(jsonString, SelectorConfig.class);
    }

    public static class Config {

        private String propertyPrefix = "selector.";

        public String getPropertyPrefix() {
            return propertyPrefix;
        }

        public Config setPropertyPrefix(String propertyPrefix) {
            this.propertyPrefix = propertyPrefix;
            return this;
        }
    }
}