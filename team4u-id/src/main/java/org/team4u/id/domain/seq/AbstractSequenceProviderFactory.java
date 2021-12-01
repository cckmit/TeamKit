package org.team4u.id.domain.seq;

import cn.hutool.json.JSONObject;
import org.team4u.base.lang.lazy.LazyFunction;

public abstract class AbstractSequenceProviderFactory implements SequenceProvider.Factory {

    private final LazyFunction<JSONObject, SequenceProvider> lazyProvider = LazyFunction.of(this::internalCreate);

    @Override
    public SequenceProvider create(JSONObject jsonConfig) {
        return lazyProvider.apply(jsonConfig);
    }

    protected abstract SequenceProvider internalCreate(JSONObject config);
}