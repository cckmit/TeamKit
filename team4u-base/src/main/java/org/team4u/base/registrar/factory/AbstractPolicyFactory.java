package org.team4u.base.registrar.factory;

import cn.hutool.core.util.TypeUtil;

import java.lang.reflect.Type;

/**
 * 抽象策略工厂
 * <p>
 * 默认启用缓存，即同样的配置，仅创建一次策略，后续直接返回策略
 * 可通过withCache设置是否启用缓存
 *
 * @param <CI> 原始配置类型
 * @param <CO> 转换后配置类型
 * @param <P>  策略类型
 * @author jay.wu
 */
public abstract class AbstractPolicyFactory<CO, CI, P> implements PolicyFactory<CI, P> {

    @Override
    public P create(CI config) {
        return createWithConfig(toConfig(config));
    }

    /**
     * 获取期望配置类型
     */
    protected Type configType() {
        return TypeUtil.getTypeArgument(this.getClass());
    }

    /**
     * 将原始配置值转换为期望配置对象
     *
     * @param configValue 原始配置值
     * @return 配置对象
     */
    protected abstract CO toConfig(CI configValue);

    /**
     * 根据配置创建策略
     *
     * @param config 配置对象
     * @return 策略
     */
    protected abstract P createWithConfig(CO config);
}