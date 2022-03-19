package org.team4u.config.domain.converter;

import cn.hutool.core.util.ReflectUtil;
import lombok.Getter;
import org.team4u.config.domain.SimpleConfigConverter;
import org.team4u.config.domain.SimpleConfigRepository;
import org.team4u.config.domain.SimpleConfigs;
import org.team4u.config.domain.event.AbstractConfigChangedEvent;
import org.team4u.ddd.domain.model.DomainEventPublisher;
import org.team4u.ddd.message.AbstractMessageConsumer;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 默认配置项转换器
 *
 * @author jay.wu
 */
@Getter
public class DefaultConfigConverter implements SimpleConfigConverter {

    private final ConfigBeanTracker configBeanTracker = new ConfigBeanTracker();

    private final SimpleConfigRepository simpleConfigRepository;
    private final ConfigTypeBeanConverter configTypeBeanConverter = new ConfigTypeBeanConverter();
    private final ConfigValueBeanConverter configValueBeanConverter = new ConfigValueBeanConverter();

    public DefaultConfigConverter(SimpleConfigRepository simpleConfigRepository) {
        this.simpleConfigRepository = simpleConfigRepository;
    }

    @Override
    public <T> T to(Class<T> toType, String configType) {
        T configBean = ReflectUtil.newInstanceIfPossible(toType);
        return configBeanTracker.trace(toType, configType, configBean);
    }

    @Override
    public <T> T to(Type toType, String configType, String configKey, boolean isCacheResult) {
        return configValueBeanConverter.to(allConfigs(), toType, configType, configKey, isCacheResult);
    }

    @Override
    public SimpleConfigs allConfigs() {
        return simpleConfigRepository.allConfigs();
    }

    public class ConfigBeanTracker extends AbstractMessageConsumer<AbstractConfigChangedEvent<?>> {

        private final Map<String, ConfigBeanRefresher> refreshers = new HashMap<>();

        public ConfigBeanTracker() {
            DomainEventPublisher.instance().subscribe(this);
        }

        public <T> T trace(Class<T> toType, String configType, T configBean) {
            if(refreshers.containsKey(configType)){
                return configBean;
            }

            ConfigBeanRefresher configBeanRefresher = new ConfigBeanRefresher(configType, toType);
            configBeanRefresher.setConfigBean(configBean);
            refreshers.put(configType, configBeanRefresher);

            configBeanRefresher.refresh(allConfigs());
            return configBean;
        }

        @Override
        protected void internalOnMessage(AbstractConfigChangedEvent<?> message) {
            Optional.ofNullable(refreshers.get(message.getConfigId().getConfigType()))
                    .ifPresent(it -> it.refresh(allConfigs()));
        }
    }
}