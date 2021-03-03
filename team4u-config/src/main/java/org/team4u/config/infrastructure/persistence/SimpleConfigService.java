package org.team4u.config.infrastructure.persistence;

import org.team4u.base.config.ConfigService;
import org.team4u.config.application.SimpleConfigAppService;

/**
 * 基于SimpleConfig的ConfigService
 *
 * @author jay.wu
 */
public class SimpleConfigService implements ConfigService {

    private final String configType;
    private final SimpleConfigAppService simpleConfigAppService;

    public SimpleConfigService(String configType,
                               SimpleConfigAppService simpleConfigAppService) {
        this.configType = configType;
        this.simpleConfigAppService = simpleConfigAppService;
    }

    @Override
    public String get(String key) {
        return simpleConfigAppService.to(configType, key);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(String key, T defaultValue) {
        return (T) simpleConfigAppService.to(defaultValue.getClass(), configType, key);
    }

    public String getConfigType() {
        return configType;
    }
}