package org.team4u.id.domain.seq.group;

import cn.hutool.json.JSONObject;
import org.team4u.base.lang.lazy.LazyFunction;

public abstract class AbstractGroupKeyProviderFactory implements SequenceGroupKeyProvider.Factory {

    private final LazyFunction<JSONObject, SequenceGroupKeyProvider> lazyProvider = LazyFunction.of(this::internalCreate);

    @Override
    public SequenceGroupKeyProvider create(JSONObject jsonConfig) {
        return lazyProvider.apply(jsonConfig);
    }

    protected abstract SequenceGroupKeyProvider internalCreate(JSONObject config);
}