package org.team4u.config.infrastructure.converter.simple;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.log.Log;
import org.team4u.base.log.LogMessage;
import org.team4u.config.domain.SimpleConfigs;

import java.util.function.Supplier;

/**
 * 配置对象更新器
 *
 * @author jay.wu
 */
public class ConfigBeanRefresher {

    private final Log log = Log.get();

    private final String configType;
    private final Class<?> targetType;

    private SimpleConfigs currentConfigs = new SimpleConfigs();
    private final ConfigBeanConverter beanConverter = new ConfigBeanConverter();

    public ConfigBeanRefresher(String configType, Class<?> targetType) {
        this.configType = configType;
        this.targetType = targetType;
    }

    /**
     * 是否无需刷新
     */
    public boolean isNotNeedToRefresh(SimpleConfigs latestConfigs) {
        return currentConfigs.equals(latestConfigs);
    }

    /**
     * 使用最新的配置项更新配置对象属性
     *
     * @param allLatestConfigs 最新的配置项
     * @param configBean       配置对象
     */
    public synchronized void refresh(SimpleConfigs allLatestConfigs, Object configBean) {
        withLog(() -> {
            SimpleConfigs latestConfigs = allLatestConfigs.filter(configType);

            if (isNotNeedToRefresh(latestConfigs)) {
                return false;
            }

            // 刷新当前配置对象
            refreshInstance(latestConfigs, configBean);
            // 刷新当前配置项
            refreshConfigs(latestConfigs);
            return true;
        });
    }

    private void withLog(Supplier<Boolean> refreshWorker) {
        LogMessage lm = LogMessage.create(targetType.getSimpleName(), "refresh")
                .append("configType", configType);
        try {
            boolean refreshed = refreshWorker.get();

            log.info(lm.success().append("refreshed", refreshed).toString());
        } catch (RuntimeException e) {
            log.error(e, lm.fail(e.getMessage()).toString());
            throw e;
        }
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