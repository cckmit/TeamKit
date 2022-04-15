package org.team4u.config.domain.converter.type;

import cn.hutool.core.util.TypeUtil;
import org.team4u.config.domain.SimpleConfigs;
import org.team4u.config.domain.converter.AbstractConfigBeanRefresher;

import java.lang.reflect.Type;

/**
 * 基于配置类型的对象更新器
 *
 * @author jay.wu
 */
public class ConfigTypeBeanRefresher extends AbstractConfigBeanRefresher<String, SimpleConfigs> {

    private final ConfigTypeBeanConverter beanConverter = new ConfigTypeBeanConverter();

    public ConfigTypeBeanRefresher(String configType, Class<?> configBeanType) {
        super(configBeanType, configType);
    }

    @Override
    public boolean isNotNeedToRefresh(SimpleConfigs latestConfigs) {
        if (getCurrentConfigValue() == null) {
            return false;
        }

        return getCurrentConfigValue().equals(latestConfigs);
    }

    @Override
    protected Object convert(SimpleConfigs latestConfigValue, Type configBeanType) {
        return beanConverter.convert(latestConfigValue, getConfigKey(), TypeUtil.getClass(configBeanType));
    }

    @Override
    protected void refreshConfigs(SimpleConfigs latestConfigValue) {
        // 需要复制副本，防止外部修改值导致比对失效
        setCurrentConfigValue(latestConfigValue.copy());
    }
}