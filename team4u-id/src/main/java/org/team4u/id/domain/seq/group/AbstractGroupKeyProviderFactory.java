package org.team4u.id.domain.seq.group;

import org.team4u.base.registrar.factory.AbstractJsonCacheablePolicyFactory;

/**
 * 抽象分组提供者工厂
 *
 * @author jay.wu
 */
public abstract class AbstractGroupKeyProviderFactory<C>
        extends AbstractJsonCacheablePolicyFactory<C, SequenceGroupKeyProvider>
        implements SequenceGroupKeyProvider.Factory {

    @Override
    protected boolean isAutoCreateConfigIfPossible() {
        return true;
    }
}