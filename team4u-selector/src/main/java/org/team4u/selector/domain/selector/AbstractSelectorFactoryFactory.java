package org.team4u.selector.domain.selector;

import org.team4u.base.registrar.factory.AbstractCacheJsonPolicyFactory;

public abstract class AbstractSelectorFactoryFactory<C>
        extends AbstractCacheJsonPolicyFactory<C, Selector>
        implements SelectorFactory {
}