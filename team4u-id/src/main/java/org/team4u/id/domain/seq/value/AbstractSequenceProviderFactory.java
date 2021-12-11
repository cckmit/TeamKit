package org.team4u.id.domain.seq.value;

import org.team4u.base.registrar.factory.AbstractCachealeJsonPolicyFactory;

/**
 * 抽象序号提供者工厂
 *
 * @author jay.wu
 */
public abstract class AbstractSequenceProviderFactory<C>
        extends AbstractCachealeJsonPolicyFactory<C, SequenceProvider>
        implements SequenceProvider.Factory {
}