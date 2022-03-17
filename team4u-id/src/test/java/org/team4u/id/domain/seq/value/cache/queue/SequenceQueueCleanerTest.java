package org.team4u.id.domain.seq.value.cache.queue;

import cn.hutool.core.thread.ThreadUtil;
import org.junit.Assert;
import org.junit.Test;
import org.team4u.id.domain.seq.value.cache.CacheStepSequenceProvider;

import static org.team4u.TestUtil.cacheProvider;
import static org.team4u.TestUtil.sequenceProviderContext;

public class SequenceQueueCleanerTest {

    @Test
    public void clear() {
        CacheStepSequenceProvider p = cacheProvider(1, 10);
        p.config().setExpiredWhenQueueStartedMillis(10);
        p.config().setNextTimeoutMillis(1);
        p.provide(sequenceProviderContext());
        SequenceQueueProducer producer = p.getSequenceQueueHolder().producerOf(sequenceProviderContext());

        ThreadUtil.sleep(200);

        Assert.assertTrue(producer.isClosed());
        Assert.assertFalse(producer.isAlive());
    }
}