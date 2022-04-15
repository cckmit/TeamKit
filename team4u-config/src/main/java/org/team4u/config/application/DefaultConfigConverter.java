package org.team4u.config.application;

import lombok.Getter;
import org.team4u.config.domain.SimpleConfigRepository;
import org.team4u.config.domain.SimpleConfigs;
import org.team4u.config.domain.converter.type.ConfigTypeBeanConverter;
import org.team4u.config.domain.converter.value.ConfigValueBeanConverter;

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
    public <T> T to(String configType, Class<T> toType) {
        return configTypeBeanConverter.convert(allConfigs(), configType, toType);
    }

    @Override
    public <T> T to(String configType, String configKey, Type toType) {
        return configValueBeanConverter.convert(allConfigs(), configType, configKey, toType);
    }

    @Override
    public SimpleConfigs allConfigs() {
        return simpleConfigRepository.allConfigs();
    }
}