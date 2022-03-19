package org.team4u.config.infrastructure.converter.bytebuddy;

import net.bytebuddy.matcher.ElementMatchers;
import org.team4u.base.aop.SimpleAop;
import org.team4u.config.domain.SimpleConfigRepository;
import org.team4u.config.domain.converter.ConfigBeanRefresher;
import org.team4u.config.domain.converter.DefaultConfigConverter;

/**
 * 基于ByteBuddy的配置转换器
 * <p>
 * 当配置项动态下发后，配置类能够实时获取最新的配置值
 *
 * @author jay.wu
 */
public class ByteBuddyConfigConverter extends DefaultConfigConverter {

    public ByteBuddyConfigConverter(SimpleConfigRepository simpleConfigRepository) {
        super(simpleConfigRepository);
    }

    @Override
    public <T> T to(Class<T> toType, String configType) {
        ConfigBeanRefresher configBeanRefresher = new ConfigBeanRefresher(configType, toType);
        T configBean = SimpleAop.proxyOf(
                toType,
                ElementMatchers.isGetter(),
                new ConfigBeanMethodInterceptor(configBeanRefresher, getSimpleConfigRepository())
        );
        configBeanRefresher.setConfigBean(configBean);
        configBeanRefresher.refresh(allConfigs());
        return configBean;
    }
}