package org.team4u.id.domain.seq.group;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.json.JSONUtil;
import org.team4u.base.lang.lazy.LazyFunction;

/**
 * 抽象分组提供者工厂
 *
 * @author jay.wu
 */
public abstract class AbstractGroupKeyProviderFactory<C> implements SequenceGroupKeyProvider.Factory<C> {

    private final LazyFunction<String, SequenceGroupKeyProvider> lazyProvider = LazyFunction.of(this::createWithString);

    @Override
    public SequenceGroupKeyProvider create(String config) {
        return lazyProvider.apply(config);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<C> configType() {
        return (Class<C>) ClassUtil.getTypeArgument(this.getClass());
    }

    private SequenceGroupKeyProvider createWithString(String configString) {
        C config = JSONUtil.toBean(configString, configType());
        return createWithConfig(config);
    }

    /**
     * 根据配置创建提供者
     *
     * @param config 配置对象
     * @return 提供者
     */
    protected abstract SequenceGroupKeyProvider createWithConfig(C config);
}