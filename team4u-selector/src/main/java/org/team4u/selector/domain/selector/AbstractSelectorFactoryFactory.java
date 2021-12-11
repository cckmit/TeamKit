package org.team4u.selector.domain.selector;

import org.team4u.base.registrar.factory.AbstractCacheableJsonPolicyFactory;

public abstract class AbstractSelectorFactoryFactory<C>
        extends AbstractCacheableJsonPolicyFactory<C, Selector>
        implements SelectorFactory {
}