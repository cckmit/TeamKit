package org.team4u.config.domain.converter;

import lombok.Getter;
import org.team4u.base.message.AbstractMessageSubscriber;
import org.team4u.base.message.MessagePublisher;
import org.team4u.config.domain.SimpleConfigRepository;
import org.team4u.config.domain.event.ConfigAllChangedEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 动态跟踪配置项转换器
 * <p>
 * 配合CacheableSimpleConfigRepository可实现动态下发配置后实时生效
 *
 * @author jay.wu
 * @see org.team4u.config.domain.repository.CacheableSimpleConfigRepository
 */
@Getter
public class TraceableConfigConverter extends DefaultConfigConverter {

    private final ConfigBeanTracker configBeanTracker = new ConfigBeanTracker();

    public TraceableConfigConverter(SimpleConfigRepository simpleConfigRepository) {
        super(simpleConfigRepository);
    }

    @Override
    public <T> T to(Class<T> toType, String configType) {
        T configBean = super.to(toType, configType);
        return configBeanTracker.trace(toType, configType, configBean);
    }

    public class ConfigBeanTracker extends AbstractMessageSubscriber<ConfigAllChangedEvent> {

        private final Map<String, ConfigBeanRefresher> refreshers = new HashMap<>();

        public ConfigBeanTracker() {
            MessagePublisher.instance().subscribe(this);
        }

        public <T> T trace(Class<T> toType, String configType, T configBean) {
            // 若配置类型相同，将直接返回之前跟踪的配置对象，确保后续配置项变化可生效
            if (refreshers.containsKey(configType)) {
                return refreshers.get(configType).getConfigBean();
            }

            ConfigBeanRefresher configBeanRefresher = new ConfigBeanRefresher(configType, toType, configBean);
            refreshers.put(configType, configBeanRefresher);
            return configBean;
        }

        @Override
        protected void internalOnMessage(ConfigAllChangedEvent event) {
            event.allConfigTypes().forEach(this::refresh);
        }

        private void refresh(String configType) {
            Optional.ofNullable(refreshers.get(configType))
                    .ifPresent(it -> it.refresh(allConfigs()));
        }
    }
}