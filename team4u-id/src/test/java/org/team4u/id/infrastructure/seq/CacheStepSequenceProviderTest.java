package org.team4u.id.infrastructure.seq;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.RandomUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.team4u.base.bean.provider.BeanProviders;
import org.team4u.id.domain.seq.SequenceConfig;
import org.team4u.id.domain.seq.value.InMemoryStepSequenceProvider;
import org.team4u.id.domain.seq.value.SequenceProvider;
import org.team4u.id.domain.seq.value.SequenceProviderFactoryHolder;
import org.team4u.id.domain.seq.value.cache.CacheStepSequenceProvider;
import org.team4u.id.domain.seq.value.cache.CacheStepSequenceProviderFactory;
import org.team4u.id.domain.seq.value.cache.queue.SequenceQueueHolder;
import org.team4u.id.domain.seq.value.cache.queue.SequenceQueueProducer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Future;

import static org.team4u.TestUtil.cacheProvider;
import static org.team4u.TestUtil.sequenceProviderContext;

public class CacheStepSequenceProviderTest {

    @Before
    public void setUp() {
        SequenceQueueHolder.getInstance().clear();
    }

    @Test
    public void concurrent() {
        CacheStepSequenceProvider provider = cacheProvider(1, 200);
        List<CacheStepSequenceProvider> providers = copy(provider, 2);
        providers.add(provider);
        List<Future<?>> result = new ArrayList<>();

        Collection<Integer> seqList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            result.add(ThreadUtil.execAsync(() -> seqList.add(providers.get(RandomUtil.randomInt(0, 3)).provide(context()).intValue())));
        }

        result.forEach(it -> {
            try {
                it.get();
                Assert.assertTrue(it.isDone());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Assert.assertEquals(100, seqList.size());
        Assert.assertTrue(provider.getDelegateProvider().currentSequence(context()).intValue() >= 100);
    }

    private List<CacheStepSequenceProvider> copy(CacheStepSequenceProvider provider, int n) {
        List<CacheStepSequenceProvider> providers = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            providers.add(new CacheStepSequenceProvider(provider.config(), provider.getDelegateProvider()));
        }

        return providers;
    }

    @Test
    public void provide() {
        CacheStepSequenceProvider p = cacheProvider(2, 100);
        InMemoryStepSequenceProvider sequenceProvider = (InMemoryStepSequenceProvider) p.getDelegateProvider();

        SequenceProvider.Context context = context();

        Assert.assertEquals(1, p.provide(context).intValue());
        Assert.assertEquals(2, p.provide(context).intValue());
        Assert.assertEquals(3, p.provide(context).intValue());
        Assert.assertEquals(4, p.provide(context).intValue());
        Assert.assertEquals(5, p.provide(context).intValue());
        Assert.assertTrue(sequenceProvider.currentSequence(context).intValue() >= 5);
    }

    @Test
    public void provide2() {
        CacheStepSequenceProvider p = cacheProvider(2, 100);
        p.config().setCacheStep(2L);
        InMemoryStepSequenceProvider sequenceProvider = (InMemoryStepSequenceProvider) p.getDelegateProvider();

        SequenceProvider.Context context = context();

        Assert.assertEquals(1, p.provide(context).intValue());
        Assert.assertEquals(3, p.provide(context).intValue());
        Assert.assertEquals(5, p.provide(context).intValue());
        Assert.assertEquals(7, p.provide(context).intValue());
        Assert.assertEquals(9, p.provide(context).intValue());
        Assert.assertTrue(sequenceProvider.currentSequence(context).intValue() >= 9);
    }

    @Test
    public void maxValue() {
        CacheStepSequenceProvider p = cacheProvider(1, 2);
        SequenceProvider.Context context = context();

        // 不循环使用
        Assert.assertEquals(1L, p.provide(context));
        Assert.assertEquals(2L, p.provide(context));
        Assert.assertNull(p.provide(context));
        Assert.assertTrue(p.isEmpty(context));
    }

    @Test
    public void maxValue2() {
        CacheStepSequenceProvider p = cacheProvider(2, 3);
        SequenceProvider.Context context = context();

        // 不循环使用
        Assert.assertEquals(1L, p.provide(context));
        Assert.assertEquals(2L, p.provide(context));
        Assert.assertEquals(3L, p.provide(context));
        Assert.assertNull(p.provide(context));
        Assert.assertTrue(p.isEmpty(context));
    }

    @Test
    public void maxValueWithRecycle() {
        CacheStepSequenceProvider p = cacheProvider(1, 2);
        p.getDelegateProvider().config().setRecycleAfterMaxValue(true);
        SequenceProvider.Context context = context();

        Assert.assertEquals(1L, p.provide(context));
        Assert.assertEquals(2L, p.provide(context));
        Assert.assertEquals(1L, p.provide(context));
        Assert.assertFalse(p.isEmpty(context));
    }

    @Test
    public void maxValueWithRecycle2() {
        CacheStepSequenceProvider p = cacheProvider(2, 3);
        p.getDelegateProvider().config().setRecycleAfterMaxValue(true);
        SequenceProvider.Context context = context();

        Assert.assertEquals(1L, p.provide(context));
        Assert.assertEquals(2L, p.provide(context));
        Assert.assertEquals(3L, p.provide(context));
        Assert.assertEquals(1L, p.provide(context));
        Assert.assertFalse(p.isEmpty(context));
    }

    @Test
    public void maxValueWithRecycle3() {
        CacheStepSequenceProvider p = cacheProvider(2, 3);
        p.getDelegateProvider().config().setStart(2L);
        p.getDelegateProvider().config().setRecycleAfterMaxValue(true);
        SequenceProvider.Context context = context();

        Assert.assertEquals(2L, p.provide(context));
        Assert.assertEquals(3L, p.provide(context));
        Assert.assertEquals(2L, p.provide(context));
        Assert.assertEquals(3L, p.provide(context));
        Assert.assertFalse(p.isEmpty(context));
    }

    @Test
    public void clearExpiredCache() {
        CacheStepSequenceProvider p = cacheProvider(1, 2);
        p.config().setExpiredWhenQueueStartedMillis(20);

        Assert.assertEquals(1L, p.provide(sequenceProviderContext()));
        SequenceQueueProducer producer = p.getSequenceQueueHolder().producerOf(sequenceProviderContext());
        Assert.assertEquals(2L, p.provide(sequenceProviderContext()));
        Assert.assertNull(p.provide(sequenceProviderContext()));
        Assert.assertTrue(p.isEmpty(sequenceProviderContext()));

        ThreadUtil.sleep(10);
        Assert.assertEquals(0, p.getSequenceQueueHolder().getQueueCleaner().clear());

        ThreadUtil.sleep(11);
        Assert.assertEquals(1, p.getSequenceQueueHolder().getQueueCleaner().clear());
        Assert.assertEquals(0, p.getSequenceQueueHolder().getQueueCleaner().clear());

        Assert.assertTrue(producer.isClosed());

        producer.awaitTermination();
        Assert.assertFalse(producer.isAlive());
    }

    @Test
    public void create() {
        BeanProviders.getInstance().registerBean(new SequenceProviderFactoryHolder());

        CacheStepSequenceProvider p = (CacheStepSequenceProvider) new CacheStepSequenceProviderFactory().create(
                FileUtil.readUtf8String("cache_step_config.json")
        );

        Assert.assertEquals(2, p.getDelegateProvider().config().getStep().intValue());
        Assert.assertEquals(1, p.provide(context()).intValue());
    }

    private SequenceProvider.Context context() {
        SequenceConfig sequenceConfig = new SequenceConfig();
        sequenceConfig.setConfigId("TEST");
        return new SequenceProvider.Context(sequenceConfig, "TEST", null);
    }
}