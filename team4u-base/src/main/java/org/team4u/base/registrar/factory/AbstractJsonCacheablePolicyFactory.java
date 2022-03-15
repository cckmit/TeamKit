package org.team4u.base.registrar.factory;

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
        return HutoolJsonSerializer.instance().deserialize(configValue, configType());
    }
}