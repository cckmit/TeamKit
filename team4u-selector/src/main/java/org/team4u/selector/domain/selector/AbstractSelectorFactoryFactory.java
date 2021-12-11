package org.team4u.selector.domain.selector;

import org.team4u.base.registrar.factory.AbstractCachealeJsonPolicyFactory;

public abstract class AbstractSelectorFactoryFactory<C>
        extends AbstractCachealeJsonPolicyFactory<C, Selector>
        implements SelectorFactory {
}