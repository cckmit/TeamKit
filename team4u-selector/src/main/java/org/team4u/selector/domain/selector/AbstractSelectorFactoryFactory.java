package org.team4u.selector.domain.selector;

import cn.hutool.cache.Cache;
import org.team4u.base.lang.CacheableFunc1;

public abstract class AbstractSelectorFactoryFactory
        extends CacheableFunc1<String, Selector>
        implements SelectorFactory {

    public AbstractSelectorFactoryFactory(Cache<String, Selector> cache) {
        super(cache);
    }

    @Override
    public Selector create(String jsonConfig) {
        return callWithCache(jsonConfig);
    }
}