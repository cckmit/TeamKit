package org.team4u.command.infrastructure.config;

import cn.hutool.core.io.resource.ResourceUtil;
import org.team4u.base.config.ConfigService;

import java.util.Map;

/**
 * @author jay.wu
 */
public class CommandConfigService implements ConfigService {
    @Override
    public String get(String key) {
        return ResourceUtil.readUtf8Str(key);
    }

    @Override
    public <T> T get(String key, T defaultValue) {
        return null;
    }

    @Override
    public Map<String, Object> allConfigs() {
        throw new UnsupportedOperationException("allConfigs not supported");
    }
}
