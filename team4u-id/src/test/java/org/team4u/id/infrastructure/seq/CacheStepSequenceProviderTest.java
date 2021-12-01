package org.team4u.id.infrastructure.seq;

import cn.hutool.core.thread.ThreadUtil;
import org.junit.Assert;
import org.junit.Test;
import org.team4u.id.domain.seq.SequenceConfig2;
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

        // 1+2=3,即一次有3个号可用
        // 缓存内
        Assert.assertEquals(1, p.provide(context).intValue());
        Assert.assertEquals(3L, sequenceProvider.counter.get());
        // 缓存内
        Assert.assertEquals(2, p.provide(context).intValue());
        Assert.assertEquals(3L, sequenceProvider.counter.get());

        // 缓存内可用号低于50%，异步刷新
        ThreadUtil.sleep(500);
        Assert.assertEquals(5L, sequenceProvider.counter.get());

        // 缓存内
        Assert.assertEquals(3, p.provide(context).intValue());
        Assert.assertEquals(5L, sequenceProvider.counter.get());
        // 缓存内
        Assert.assertEquals(4, p.provide(context).intValue());
        Assert.assertEquals(5L, sequenceProvider.counter.get());
        // 缓存已用完
        Assert.assertEquals(5, p.provide(context).intValue());
        Assert.assertEquals(7L, sequenceProvider.counter.get());
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

    private CacheStepSequenceProvider provider(int step, int maxValue, int percent) {
        CacheStepSequenceProvider.ProviderConfig config = new CacheStepSequenceProvider.ProviderConfig();
        config.setStep((long) step);
        config.setMaxValue((long) maxValue);
        config.setMinAvailableSeqPercent(percent);

        MockStepSequenceProvider sequenceProvider = new MockStepSequenceProvider(config);
        return new CacheStepSequenceProvider(config, sequenceProvider);
    }

    private SequenceProvider.Context context() {
        SequenceConfig2 sequenceConfig = new SequenceConfig2();
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