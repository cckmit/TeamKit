package org.team4u.config.domain.converter;

import lombok.Getter;
import org.team4u.config.domain.SimpleConfigConverter;
import org.team4u.config.domain.SimpleConfigRepository;
import org.team4u.config.domain.SimpleConfigs;
import org.team4u.config.domain.event.ConfigAllChangedEvent;
import org.team4u.ddd.domain.model.DomainEventPublisher;
import org.team4u.ddd.message.AbstractMessageConsumer;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 默认配置项转换器
 * <p>
 * 配合CacheableSimpleConfigRepository可实现动态下发配置后实时生效
 *
 * @author jay.wu
 * @see org.team4u.config.domain.repository.CacheableSimpleConfigRepository
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
        T configBean = configTypeBeanConverter.convert(allConfigs(), configType, toType);
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

    public class ConfigBeanTracker extends AbstractMessageConsumer<ConfigAllChangedEvent> {

        private final Map<String, ConfigBeanRefresher> refreshers = new HashMap<>();

        public ConfigBeanTracker() {
            DomainEventPublisher.instance().subscribe(this);
        }

        public <T> T trace(Class<T> toType, String configType, T configBean) {
            if (refreshers.containsKey(configType)) {
                return refreshers.get(configType).getConfigBean();
            }

            ConfigBeanRefresher configBeanRefresher = new ConfigBeanRefresher(configType, toType, configBean);
            refreshers.put(configType, configBeanRefresher);
            return configBean;
        }

        @Override
        protected void internalOnMessage(ConfigAllChangedEvent event) {
            event.getChangedEvents()
                    .stream()
                    .map(it -> it.getConfigId().getConfigType())
                    .collect(Collectors.toSet())
                    .forEach(configType ->
                            Optional.ofNullable(refreshers.get(configType))
                                    .ifPresent(it -> it.refresh(allConfigs()))
                    );
        }
    }
}