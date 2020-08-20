package org.team4u.config.application;

import org.team4u.config.domain.SimpleConfigConverter;
import org.team4u.config.domain.SimpleConfigRepository;
import org.team4u.config.infrastructure.converter.ProxySimpleConfigConverter;

public class SimpleConfigAppService {

    private final SimpleConfigConverter simpleConfigConverter;

    public SimpleConfigAppService(SimpleConfigRepository simpleConfigRepository) {
        this.simpleConfigConverter = new ProxySimpleConfigConverter(simpleConfigRepository);
    }

    public <T> T to(Class<T> toType, String configType) {
        return simpleConfigConverter.to(toType, configType);
    }

    public <T> T to(Class<T> toType, String configType, String configKey) {
        return simpleConfigConverter.to(toType, configType, configKey);
    }

    public String to(String configType, String configKey) {
        return simpleConfigConverter.to(configType, configKey);
    }
}