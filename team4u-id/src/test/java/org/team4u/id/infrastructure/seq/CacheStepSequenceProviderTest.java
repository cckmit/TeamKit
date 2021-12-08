package org.team4u.id.infrastructure.seq;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import org.junit.Assert;
import org.junit.Test;
import org.team4u.base.bean.provider.BeanProviders;
import org.team4u.id.domain.seq.SequenceConfig;
import org.team4u.id.domain.seq.value.*;

public class CacheStepSequenceProviderTest {

    @Test
    public void provide() {
        CacheStepSequenceProvider p = provider(2, 100, 50);
        InMemoryStepSequenceProvider sequenceProvider = (InMemoryStepSequenceProvider) p.getDelegateProvider();

        SequenceProvider.Context context = context();

        Assert.assertEquals(1L, sequenceProvider.currentSequence(context));

        Assert.assertEquals(1, p.provide(context).intValue());
        Assert.assertEquals(2, p.provide(context).intValue());
        Assert.assertEquals(3, p.provide(context).intValue());
        Assert.assertEquals(4, p.provide(context).intValue());
        Assert.assertEquals(5, p.provide(context).intValue());
        Assert.assertTrue(sequenceProvider.currentSequence(context).intValue() >= 7);
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
        p.getDelegateProvider().config().setRecycleAfterMaxValue(true);
        SequenceProvider.Context context = context();

        Assert.assertEquals(1L, p.provide(context));
        Assert.assertEquals(2L, p.provide(context));
        Assert.assertEquals(1L, p.provide(context));
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
        sequenceConfig.setTypeId("TEST");
        return new SequenceProvider.Context(
                sequenceConfig,
                "TEST",
                null
        );
    }
}