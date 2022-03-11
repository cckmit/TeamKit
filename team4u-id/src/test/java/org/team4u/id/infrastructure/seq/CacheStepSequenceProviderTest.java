package org.team4u.id.infrastructure.seq;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.RandomUtil;
import org.junit.Assert;
import org.junit.Test;
import org.team4u.base.bean.provider.BeanProviders;
import org.team4u.id.domain.seq.SequenceConfig;
import org.team4u.id.domain.seq.value.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

public class CacheStepSequenceProviderTest {

    @Test
    public void concurrent() {
        CacheStepSequenceProvider provider = provider(1, 100, 60);
        List<CacheStepSequenceProvider> providers = copy(provider, 2);
        providers.add(provider);
        List<Future<?>> result = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            result.add(ThreadUtil.execAsync(() -> providers.get(RandomUtil.randomInt(0, 2)).provide(context())));
        }

        result.forEach(it -> {
            try {
                it.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Assert.assertEquals(100, provider.getDelegateProvider().currentSequence(context()).intValue());
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
        CacheStepSequenceProvider p = provider(2, 100, 50);
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
        CacheStepSequenceProvider p = provider(2, 100, 50);
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
        CacheStepSequenceProvider p = provider(1, 2, 50);
        SequenceProvider.Context context = context();

        // 不循环使用
        Assert.assertEquals(1L, p.provide(context));
        Assert.assertEquals(2L, p.provide(context));
        Assert.assertNull(p.provide(context));
        Assert.assertTrue(p.isEmpty(context));
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
        Assert.assertTrue(p.isEmpty(context));
    }

    @Test
    public void maxValueWithRecycle() {
        CacheStepSequenceProvider p = provider(1, 2, 50);
        p.getDelegateProvider().config().setRecycleAfterMaxValue(true);
        SequenceProvider.Context context = context();

        Assert.assertEquals(1L, p.provide(context));
        Assert.assertEquals(2L, p.provide(context));
        Assert.assertEquals(1L, p.provide(context));
        Assert.assertFalse(p.isEmpty(context));
    }

    @Test
    public void maxValueWithRecycle2() {
        CacheStepSequenceProvider p = provider(2, 3, 50);
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
        CacheStepSequenceProvider p = provider(2, 3, 50);
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
        CacheStepSequenceProvider p = provider(1, 2, 50);
        p.config().setExpiredWhenCloseMillis(1);

        Assert.assertEquals(1L, p.provide(context()));
        Assert.assertEquals(2L, p.provide(context()));
        Assert.assertNull(p.provide(context()));
        Assert.assertTrue(p.isEmpty(context()));

        ThreadUtil.sleep(10);

        Assert.assertEquals(1, p.clearExpiredCache());
        Assert.assertEquals(0, p.clearExpiredCache());
    }

    @Test
    public void create() {
        BeanProviders.getInstance().registerBean(new SequenceProviderFactoryHolder());

        CacheStepSequenceProvider p = (CacheStepSequenceProvider) new CacheStepSequenceProvider.Factory()
                .create(FileUtil.readUtf8String("cache_step_config.json"));

        Assert.assertEquals(2, p.getDelegateProvider().config().getStep().intValue());
        Assert.assertEquals(1, p.provide(context()).intValue());
    }

    private CacheStepSequenceProvider provider(int step, int maxValue, int percent) {
        CacheStepSequenceProvider.Config config = new CacheStepSequenceProvider.Config();
        config.setMinAvailableSeqPercent(percent);

        StepSequenceProvider.Config delegateConfig = new StepSequenceProvider.Config();
        delegateConfig.setStep(step);
        delegateConfig.setMaxValue((long) maxValue);
        InMemoryStepSequenceProvider sequenceProvider = new InMemoryStepSequenceProvider(delegateConfig);

        return new CacheStepSequenceProvider(config, sequenceProvider);
    }

    private SequenceProvider.Context context() {
        SequenceConfig sequenceConfig = new SequenceConfig();
        sequenceConfig.setConfigId("TEST");
        return new SequenceProvider.Context(
                sequenceConfig,
                "TEST",
                null
        );
    }
}