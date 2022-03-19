package org.team4u.config.domain.converter;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.log.Log;
import lombok.Getter;
import org.team4u.base.log.LogMessage;
import org.team4u.base.log.LogMessageContext;
import org.team4u.config.domain.SimpleConfigs;

import static org.team4u.base.log.LogService.withInfoLog;

/**
 * 配置对象更新器
 *
 * @author jay.wu
 */
public class ConfigBeanRefresher {

    private final Log log = Log.get();

    @Getter
    private final String configType;
    @Getter
    private final Class<?> targetType;

    private SimpleConfigs currentConfigs = new SimpleConfigs();
    private final ConfigTypeBeanConverter beanConverter = new ConfigTypeBeanConverter();

    public ConfigBeanRefresher(String configType, Class<?> targetType) {
        this.configType = configType;
        this.targetType = targetType;
    }

    /**
     * 是否无需刷新
     */
    public boolean isNotNeedToRefresh(SimpleConfigs latestConfigs) {
        return currentConfigs.equals(configsOfType(latestConfigs));
    }

    /**
     * 使用最新的配置项更新配置对象属性
     *
     * @param latestAllConfigs 最新的配置项
     * @param configBean       配置对象
     */
    public synchronized void refresh(SimpleConfigs latestAllConfigs, Object configBean) {
        withInfoLog(log, "refresh", () -> {
            LogMessage lm = LogMessageContext.get().append("configType", configType);

            SimpleConfigs latestConfigs = configsOfType(latestAllConfigs);
            if (isNotNeedToRefresh(latestConfigs)) {
                lm.append("refreshed", false);
                return false;
            }

            // 刷新当前配置对象
            refreshInstance(latestConfigs, configBean);
            // 刷新当前配置项
            refreshConfigs(latestConfigs);

            lm.append("refreshed", true);
            return null;
        });
    }

    private SimpleConfigs configsOfType(SimpleConfigs configs) {
        return configs.filter(configType);
    }

    private void refreshInstance(SimpleConfigs latestConfigs, Object currentConfigBean) {
        Object newConfigBean = beanConverter.convert(latestConfigs, configType, targetType);
        // 将最新配置对象字段值赋值到当前配置对象
        BeanUtil.copyProperties(newConfigBean, currentConfigBean);
    }

    private void refreshConfigs(SimpleConfigs latestConfigs) {
        currentConfigs = latestConfigs.copy();
    }
}