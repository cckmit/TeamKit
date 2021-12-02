package org.team4u.id.domain.seq.value;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.json.JSONUtil;
import org.team4u.base.lang.lazy.LazyFunction;

/**
 * 抽象序号提供者工厂
 *
 * @author jay.wu
 */
public abstract class AbstractSequenceProviderFactory<C> implements SequenceProvider.Factory<C> {

    private final LazyFunction<String, SequenceProvider> lazyProvider = LazyFunction.of(this::createWithString);

    @Override
    public SequenceProvider create(String config) {
        return lazyProvider.apply(config);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<C> configType() {
        return (Class<C>) ClassUtil.getTypeArgument(this.getClass());
    }

    private SequenceProvider createWithString(String configString) {
        C config = JSONUtil.toBean(configString, configType());
        return createWithConfig(config);
    }

    /**
     * 根据配置创建提供者
     *
     * @param config 配置对象
     * @return 提供者
     */
    protected abstract SequenceProvider createWithConfig(C config);
}