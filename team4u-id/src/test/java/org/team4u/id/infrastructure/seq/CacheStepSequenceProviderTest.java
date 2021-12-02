package org.team4u.id.infrastructure.seq;

import cn.hutool.core.thread.ThreadUtil;
import org.junit.Assert;
import org.junit.Test;
import org.team4u.id.domain.seq.SequenceConfig;
import org.team4u.id.domain.seq.SequenceProvider;
import org.team4u.id.infrastructure.seq.sp.CacheStepSequenceProvider;
import org.team4u.id.infrastructure.seq.sp.StepSequenceProvider;

import java.util.concurrent.atomic.AtomicLong;

public class CacheStepSequenceProviderTest {

    @Test
    public void provide() {
        CacheStepSequenceProvider p = provider(2, 100, 50);
        MockStepSequenceProvider sequenceProvider = (MockStepSequenceProvider) p.getSequenceProvider();

        SequenceProvider.Context context = context();

        Assert.assertEquals(1L, sequenceProvider.counter.get());

        Assert.assertEquals(1, p.provide(context).intValue());
        Assert.assertEquals(2, p.provide(context).intValue());
        Assert.assertEquals(3, p.provide(context).intValue());
        Assert.assertEquals(4, p.provide(context).intValue());
        Assert.assertEquals(5, p.provide(context).intValue());
        Assert.assertEquals(9L, sequenceProvider.counter.get());
    }

    @Test
    public void maxValue() {
        CacheStepSequenceProvider p = provider(1, 2, 50);
        SequenceProvider.Context context = context();

        // 不循环使用
        Assert.assertEquals(1L, p.provide(context));
        Assert.assertEquals(2L, p.provide(context));
        Assert.assertNull(p.provide(context));
    }

    @Test
    public void maxValue2() {
        CacheStepSequenceProvider p = provider(2, 3, 50);
        SequenceProvider.Context context = context();

        // 不循环使用
        Assert.assertEquals(1L, p.provide(context));
        Assert.assertEquals(2L, p.provide(context));
        Assert.assertEquals(3L, p.provide(context));
        Assert.assertNull(p.provide(context));
    }

    @Test
    public void maxValueWithRecycle() {
        CacheStepSequenceProvider p = provider(1, 2, 50);
        p.config().setRecycleAfterMaxValue(true);
        SequenceProvider.Context context = context();

        Assert.assertEquals(1L, p.provide(context));
        Assert.assertEquals(2L, p.provide(context));
        Assert.assertEquals(1L, p.provide(context));
    }

    @Test
    public void maxValueWithRecycle2() {
        CacheStepSequenceProvider p = provider(2, 3, 50);
        p.config().setRecycleAfterMaxValue(true);
        SequenceProvider.Context context = context();

        Assert.assertEquals(1L, p.provide(context));
        Assert.assertEquals(2L, p.provide(context));
        Assert.assertEquals(3L, p.provide(context));
        Assert.assertEquals(1L, p.provide(context));
    }

    @Test
    public void clearExpiredCache() {
        CacheStepSequenceProvider p = provider(1, 2, 50);
        p.config().setExpiredWhenCloseMillis(1);

        Assert.assertEquals(1L, p.provide(context()));
        Assert.assertEquals(2L, p.provide(context()));
        Assert.assertNull(p.provide(context()));

        ThreadUtil.sleep(10);

        Assert.assertEquals(1, p.clearExpiredCache());
        Assert.assertEquals(0, p.clearExpiredCache());
    }

    private CacheStepSequenceProvider provider(int step, int maxValue, int percent) {
        CacheStepSequenceProvider.CacheConfig config = new CacheStepSequenceProvider.CacheConfig();
        config.setStep(step);
        config.setMaxValue((long) maxValue);
        config.setMinAvailableSeqPercent(percent);

        MockStepSequenceProvider sequenceProvider = new MockStepSequenceProvider(config);
        return new CacheStepSequenceProvider(config, sequenceProvider);
    }

    private SequenceProvider.Context context() {
        SequenceConfig sequenceConfig = new SequenceConfig();
        sequenceConfig.setTypeId("TEST");
        return new SequenceProvider.Context(
                sequenceConfig,
                "TEST",
                null
        );
    }

    private static class MockStepSequenceProvider implements StepSequenceProvider {
        private final Config config;
        private final AtomicLong counter;

        private MockStepSequenceProvider(Config config) {
            this.config = config;
            counter = new AtomicLong(config.getStart());
        }

        @Override
        public Number provide(Context context) {
            if (counter.get() > config().getMaxValue()) {
                if (config().isRecycleAfterMaxValue()) {
                    counter.set(config().getStart());
                } else {
                    return null;
                }
            }
            return counter.getAndAdd(config.getStep());
        }

        @Override
        public Config config() {
            return config;
        }
    }
}