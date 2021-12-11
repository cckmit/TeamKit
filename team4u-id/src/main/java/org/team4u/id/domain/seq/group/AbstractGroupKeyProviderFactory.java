package org.team4u.id.domain.seq.group;

import org.team4u.base.registrar.factory.AbstractCacheableJsonPolicyFactory;

/**
 * 抽象分组提供者工厂
 *
 * @author jay.wu
 */
public abstract class AbstractGroupKeyProviderFactory<C>
        extends AbstractCacheableJsonPolicyFactory<C, SequenceGroupKeyProvider>
        implements SequenceGroupKeyProvider.Factory {
}