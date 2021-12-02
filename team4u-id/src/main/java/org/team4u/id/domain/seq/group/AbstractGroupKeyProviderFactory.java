package org.team4u.id.domain.seq.group;

import cn.hutool.core.util.ClassUtil;
import org.team4u.base.lang.lazy.LazyFunction;

public abstract class AbstractGroupKeyProviderFactory<C> implements SequenceGroupKeyProvider.Factory<C> {

    private final LazyFunction<C, SequenceGroupKeyProvider> lazyProvider = LazyFunction.of(this::internalCreate);

    @Override
    public SequenceGroupKeyProvider create(C config) {
        return lazyProvider.apply(config);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<C> configType() {
        return (Class<C>) ClassUtil.getTypeArgument(this.getClass());
    }

    protected abstract SequenceGroupKeyProvider internalCreate(C config);
}