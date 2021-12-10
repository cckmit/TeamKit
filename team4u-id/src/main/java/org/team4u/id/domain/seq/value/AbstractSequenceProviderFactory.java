package org.team4u.id.domain.seq.value;

import org.team4u.base.registrar.factory.AbstractJsonPolicyFactory;

/**
 * 抽象序号提供者工厂
 *
 * @author jay.wu
 */
public abstract class AbstractSequenceProviderFactory<C>
        extends AbstractJsonPolicyFactory<C, SequenceProvider>
        implements SequenceProvider.Factory<C> {
}