package org.team4u.config.domain.converter;

import lombok.Getter;
import org.team4u.base.message.jvm.AbstractMessageSubscriber;
import org.team4u.base.message.jvm.MessagePublisher;
import org.team4u.config.domain.SimpleConfigConverter;
import org.team4u.config.domain.SimpleConfigId;
import org.team4u.config.domain.SimpleConfigs;
import org.team4u.config.domain.converter.type.ConfigTypeBeanRefresher;
import org.team4u.config.domain.converter.value.ConfigValueBeanRefresher;
import org.team4u.config.domain.event.ConfigAllChangedEvent;
import org.team4u.config.domain.repository.CacheableSimpleConfigRepository;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 动态跟踪配置项转换器
 * <p>
 * 可实现动态下发配置后实时生效
 *
 * @author jay.wu
 */
@Getter
public class TraceableConfigConverter implements SimpleConfigConverter {

    private final CacheableSimpleConfigRepository simpleConfigRepository;
    private final ConfigBeanTracker configBeanTracker = new ConfigBeanTracker();

    public TraceableConfigConverter(CacheableSimpleConfigRepository simpleConfigRepository) {
        this.simpleConfigRepository = simpleConfigRepository;
    }

    @Override
    public <T> T to(String configType, Class<T> toType) {
        return configBeanTracker.trace(configType, toType);
    }

    @Override
    public <T> T to(String configType, String configKey, Type toType) {
        return configBeanTracker.trace(new SimpleConfigId(configType, configKey), toType);
    }

    @Override
    public SimpleConfigs allConfigs() {
        return simpleConfigRepository.allConfigs();
    }

    public class ConfigBeanTracker extends AbstractMessageSubscriber<ConfigAllChangedEvent> {

        private final Map<String, ConfigTypeBeanRefresher> typeRefreshers = new HashMap<>();

        private final Map<SimpleConfigId, ConfigValueBeanRefresher> valueRefreshers = new HashMap<>();

        public ConfigBeanTracker() {
            MessagePublisher.instance().subscribe(this);
        }

        public synchronized <T> T trace(String configType, Class<T> toType) {
            ConfigTypeBeanRefresher beanRefresher = typeRefreshers.get(configType);

            if (beanRefresher == null) {
                beanRefresher = new ConfigTypeBeanRefresher(configType, toType);
                beanRefresher.refresh(allConfigs().filter(configType));
                typeRefreshers.put(configType, beanRefresher);
            }

            return beanRefresher.getConfigBean();
        }

        public synchronized <T> T trace(SimpleConfigId configId, Type toType) {
            ConfigValueBeanRefresher beanRefresher = valueRefreshers.get(configId);

            if (beanRefresher == null) {
                beanRefresher = new ConfigValueBeanRefresher(configId, toType);
                beanRefresher.refresh(allConfigs().value(configId));
                valueRefreshers.put(configId, beanRefresher);
            }

            return beanRefresher.getConfigBean();
        }

        @Override
        protected void internalOnMessage(ConfigAllChangedEvent event) {
            event.allConfigIdList().forEach(this::refresh);
            event.allConfigTypes().forEach(this::refresh);
        }

        private void refresh(String configType) {
            try {
                Optional.ofNullable(typeRefreshers.get(configType))
                        .ifPresent(it -> it.refresh(allConfigs().filter(configType)));
            } catch (Exception e) {
                // 内部已打印error日志，防止影响其他配置刷新
            }
        }

        private void refresh(SimpleConfigId configId) {
            try {
                Optional.ofNullable(valueRefreshers.get(configId))
                        .ifPresent(it -> it.refresh(allConfigs().value(configId)));
            } catch (Exception e) {
                // 内部已打印error日志，防止影响其他配置刷新
            }
        }
    }
}