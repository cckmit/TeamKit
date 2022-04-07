package org.team4u.selector.infrastructure.persistence;

import cn.hutool.core.util.StrUtil;
import org.team4u.base.config.ConfigService;
import org.team4u.base.serializer.json.JsonSerializers;
import org.team4u.selector.domain.selector.SelectorConfig;
import org.team4u.selector.domain.selector.SelectorConfigRepository;

/**
 * 基于配置的选择器资源库
 *
 * @author jay.wu
 * @see JsonSelectorConfigRepository
 * @see org.team4u.base.config.PropConfigService
 * @deprecated Use JsonSelectorConfigRepository instead
 */
public class PropertySelectorConfigRepository implements SelectorConfigRepository {

    private final Config config;
    private final ConfigService configService;

    public PropertySelectorConfigRepository(Config config, ConfigService configService) {
        this.config = config;
        this.configService = configService;
    }

    @Override
    public SelectorConfig configOfId(String id) {
        String jsonString = configService.get(config.getPropertyPrefix() + id);
        if (StrUtil.isEmpty(jsonString)) {
            return null;
        }

        return JsonSerializers.getInstance().deserialize(jsonString, SelectorConfig.class);
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