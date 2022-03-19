package org.team4u.config.domain.converter;

import lombok.Getter;
import org.team4u.config.domain.SimpleConfigRepository;
import org.team4u.config.domain.SimpleConfigs;

import java.lang.reflect.Type;

/**
 * 抽象配置项转换器
 *
 * @author jay.wu
 */
@Getter
public abstract class AbstractSimpleConfigConverter implements SimpleConfigConverter {

    private final SimpleConfigRepository simpleConfigRepository;
    private final ConfigValueBeanConverter configValueBeanConverter = new ConfigValueBeanConverter();

    public AbstractSimpleConfigConverter(SimpleConfigRepository simpleConfigRepository) {
        this.simpleConfigRepository = simpleConfigRepository;
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