package org.team4u.config.domain.converter;

import lombok.Getter;
import org.team4u.config.domain.SimpleConfigConverter;
import org.team4u.config.domain.SimpleConfigRepository;
import org.team4u.config.domain.SimpleConfigs;

import java.lang.reflect.Type;

/**
 * 默认配置项转换器
 */
@Getter
public class DefaultConfigConverter implements SimpleConfigConverter {

    private final SimpleConfigRepository simpleConfigRepository;
    private final ConfigTypeBeanConverter configTypeBeanConverter = new ConfigTypeBeanConverter();
    private final ConfigValueBeanConverter configValueBeanConverter = new ConfigValueBeanConverter();

    public DefaultConfigConverter(SimpleConfigRepository simpleConfigRepository) {
        this.simpleConfigRepository = simpleConfigRepository;
    }

    @Override
    public <T> T to(Class<T> toType, String configType) {
        return configTypeBeanConverter.convert(allConfigs(), configType, toType);
    }

    @Override
    public <T> T to(Type toType, String configType, String configKey, boolean isCacheResult) {
        return configValueBeanConverter.to(allConfigs(), toType, configType, configKey, isCacheResult);
    }

    @Override
    public SimpleConfigs allConfigs() {
        return simpleConfigRepository.allConfigs();
    }
}