package org.team4u.id.domain.seq.value.cache.queue;

import lombok.Data;
import org.team4u.id.domain.seq.value.SequenceProvider;
import org.team4u.id.domain.seq.value.StepSequenceProvider;
import org.team4u.id.domain.seq.value.cache.CacheStepSequenceConfig;

/**
 * 序号队列上下文
 *
 * @author jay.wu
 */
@Data
public class SequenceQueueContext {

    private final SequenceProvider.Context providerContext;

    private final CacheStepSequenceConfig sequenceConfig;

    private final StepSequenceProvider delegateProvider;

    public String id() {
        return providerContext.id();
    }

    @Override
    public String toString() {
        return id();
    }
}
