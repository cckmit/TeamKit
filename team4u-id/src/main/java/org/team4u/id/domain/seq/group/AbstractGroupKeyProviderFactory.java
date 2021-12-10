package org.team4u.id.domain.seq.group;

import org.team4u.base.registrar.factory.AbstractCacheJsonPolicyFactory;

/**
 * 抽象分组提供者工厂
 *
 * @author jay.wu
 */
public abstract class AbstractGroupKeyProviderFactory<C>
        extends AbstractCacheJsonPolicyFactory<C, SequenceGroupKeyProvider>
        implements SequenceGroupKeyProvider.Factory {
}