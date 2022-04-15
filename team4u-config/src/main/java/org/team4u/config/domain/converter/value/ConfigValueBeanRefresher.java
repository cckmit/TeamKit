package org.team4u.config.domain.converter.value;

import cn.hutool.core.util.StrUtil;
import org.team4u.config.domain.SimpleConfigId;
import org.team4u.config.domain.converter.AbstractConfigBeanRefresher;

import java.lang.reflect.Type;

/**
 * 基于配置值的对象更新器
 *
 * @author jay.wu
 */
public class ConfigValueBeanRefresher extends AbstractConfigBeanRefresher<SimpleConfigId, String> {

    private final ConfigValueBeanConverter configValueBeanConverter = new ConfigValueBeanConverter();

    public ConfigValueBeanRefresher(SimpleConfigId configId, Type configBeanType) {
        super(configBeanType, configId);
    }

    @Override
    public boolean isNotNeedToRefresh(String latestConfigValue) {
        return StrUtil.equals(latestConfigValue, getCurrentConfigValue());
    }

    @Override
    protected Object convert(String latestConfigValue, Type configBeanType) {
        return configValueBeanConverter.convert(latestConfigValue, configBeanType);
    }
}