package org.team4u.base.registrar.factory;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import org.team4u.base.serializer.HutoolJsonSerializer;

/**
 * 基于Json配置的抽象缓存策略工厂
 *
 * @param <C> 配置类型
 * @param <P> 策略类型
 * @author jay.wu
 */
public abstract class AbstractJsonCacheablePolicyFactory<C, P> extends AbstractCacheablePolicyFactory<C, String, P> {

    @Override
    public C toConfig(String configValue) {
        if (StrUtil.isNotBlank(configValue)) {
            return HutoolJsonSerializer.instance().deserialize(configValue, configType());
        }

        if (!isAutoCreateConfigIfPossible()) {
            return null;
        }

        return createConfigIfPossible();
    }

    private C createConfigIfPossible() {
        if (configType() instanceof Class) {
            //noinspection unchecked
            return ReflectUtil.newInstanceIfPossible((Class<C>) configType());
        }

        return null;
    }

    /**
     * 如果配置为null，尝试自动新建对象
     *
     * @return 是否开启自动新建功能
     */
    protected boolean isAutoCreateConfigIfPossible() {
        return false;
    }
}