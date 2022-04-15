package org.team4u.config.domain.converter;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.log.Log;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Type;

import static org.team4u.base.log.LogService.withInfoLog;

/**
 * 抽象配置对象更新器
 *
 * @author jay.wu
 */
public abstract class AbstractConfigBeanRefresher<K, V> {

    protected final Log log = Log.get();

    private final Type configBeanType;

    private Object configBean;

    @Getter
    private final K configKey;

    @Getter
    @Setter(AccessLevel.PROTECTED)
    private V currentConfigValue;

    public AbstractConfigBeanRefresher(Type configBeanType, K configKey) {
        this.configBeanType = configBeanType;
        this.configKey = configKey;
    }

    /**
     * 是否无需刷新
     */
    protected abstract boolean isNotNeedToRefresh(V latestConfigValue);

    /**
     * 使用最新的配置项更新配置对象属性
     *
     * @param latestConfigValue 最新的配置项
     */
    public synchronized void refresh(V latestConfigValue) {
        withInfoLog(log, getClass().getSimpleName(), "refresh", (lm) -> {
            lm.append("configKey", configKey);

            if (isNotNeedToRefresh(latestConfigValue)) {
                lm.append("refreshed", false);
                return false;
            }

            // 刷新当前配置对象
            refreshInstance(latestConfigValue);
            // 刷新当前配置项
            refreshConfigs(latestConfigValue);

            lm.append("refreshed", true);
            return true;
        });
    }

    @SuppressWarnings("unchecked")
    public <T> T getConfigBean() {
        return (T) configBean;
    }

    private void refreshInstance(V latestConfigValue) {
        Object newConfigBean = convert(latestConfigValue, configBeanType);

        if (configBean == null) {
            configBean = newConfigBean;
            return;
        }

        // 将最新配置对象字段值赋值到当前配置对象
        BeanUtil.copyProperties(newConfigBean, configBean);
    }

    protected abstract Object convert(V latestConfigValue, Type configBeanType);

    protected void refreshConfigs(V latestConfigValue) {
        currentConfigValue = latestConfigValue;
    }
}