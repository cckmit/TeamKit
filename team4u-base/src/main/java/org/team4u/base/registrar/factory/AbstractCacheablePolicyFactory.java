package org.team4u.base.registrar.factory;

import org.team4u.base.lang.lazy.LazyFunction;

/**
 * 抽象缓存策略工厂
 * <p>
 * 同样的配置，仅创建一次策略，后续直接返回
 *
 * @param <CI> 原始配置类型
 * @param <CO> 转换后配置类型
 * @param <P>  策略类型
 * @author jay.wu
 */
public abstract class AbstractCacheablePolicyFactory<CO, CI, P> extends AbstractPolicyFactory<CO, CI, P> {

    private final LazyFunction<CI, P> lazyFactory = createLazyFactory();

    @Override
    public P create(CI config) {
        return lazyFactory.apply(config);
    }

    /**
     * 创建懒加载工厂
     */
    protected LazyFunction<CI, P> createLazyFactory() {
        return LazyFunction.of(super::create);
    }
}