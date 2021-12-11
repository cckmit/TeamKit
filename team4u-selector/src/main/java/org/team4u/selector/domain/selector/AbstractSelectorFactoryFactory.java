package org.team4u.selector.domain.selector;

import org.team4u.base.registrar.factory.AbstractJsonCacheablePolicyFactory;

public abstract class AbstractSelectorFactoryFactory<C>
        extends AbstractJsonCacheablePolicyFactory<C, Selector>
        implements SelectorFactory {
}