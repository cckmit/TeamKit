package org.team4u.id.domain.seq.value;

import cn.hutool.core.thread.ThreadUtil;
import org.junit.Assert;
import org.junit.Test;
import org.team4u.id.domain.seq.SequenceConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

public abstract class AbstractStepSequenceProviderTest {

    protected abstract StepSequenceProvider provider(StepSequenceProvider.Config config);

    @Test
    public void concurrent() {
        StepSequenceProvider.Config config = new StepSequenceProvider.Config();
        StepSequenceProvider provider = provider(config);

        List<Future<?>> result = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            result.add(ThreadUtil.execAsync(() ->  provider.provide(context())));
        }

        result.forEach(it -> {
            try {
                it.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Assert.assertEquals(101, provider.provide(context()).intValue());
    }

    @Test
    public void notRecycle() {
        StepSequenceProvider.Config config = new StepSequenceProvider.Config();
        config.setMaxValue(2L);
        StepSequenceProvider provider = provider(config);

        Assert.assertEquals(1L, provider.provide(context()));
        Assert.assertEquals(2L, provider.provide(context()));
        Assert.assertNull(provider.provide(context()));
    }

    @Test
    public void recycle1() {
        StepSequenceProvider.Config config = new StepSequenceProvider.Config();
        config.setMaxValue(2L);
        config.setRecycleAfterMaxValue(true);
        StepSequenceProvider provider = provider(config);

        Assert.assertEquals(1L, provider.provide(context()));
        Assert.assertEquals(2L, provider.provide(context()));
        Assert.assertEquals(1L, provider.provide(context()));
        Assert.assertEquals(2L, provider.provide(context()));
    }

    @Test
    public void recycle2() {
        StepSequenceProvider.Config config = new StepSequenceProvider.Config();
        config.setStep(2);
        config.setMaxValue(3L);
        config.setRecycleAfterMaxValue(true);
        StepSequenceProvider provider = provider(config);

        Assert.assertEquals(1L, provider.provide(context()));
        Assert.assertEquals(3L, provider.provide(context()));
        Assert.assertEquals(1L, provider.provide(context()));
        Assert.assertEquals(3L, provider.provide(context()));
    }

    @Test
    public void recycle3() {
        StepSequenceProvider.Config config = new StepSequenceProvider.Config();
        config.setStart(2L);
        config.setStep(2);
        config.setMaxValue(5L);
        config.setRecycleAfterMaxValue(true);
        StepSequenceProvider provider = provider(config);

        Assert.assertEquals(2L, provider.provide(context()));
        Assert.assertEquals(4L, provider.provide(context()));
        Assert.assertEquals(2L, provider.provide(context()));
        Assert.assertEquals(4L, provider.provide(context()));
    }

    @Test
    public void recycle4() {
        StepSequenceProvider.Config config = new StepSequenceProvider.Config();
        config.setStart(2L);
        config.setStep(2);
        config.setMaxValue(4L);
        config.setRecycleAfterMaxValue(true);
        StepSequenceProvider provider = provider(config);

        Assert.assertEquals(2L, provider.provide(context()));
        Assert.assertEquals(4L, provider.provide(context()));
        Assert.assertEquals(2L, provider.provide(context()));
        Assert.assertEquals(4L, provider.provide(context()));
    }

    protected SequenceProvider.Context context() {
        SequenceConfig sequenceConfig = new SequenceConfig();
        sequenceConfig.setTypeId("TEST");
        return new SequenceProvider.Context(
                sequenceConfig,
                "TEST",
                null
        );
    }
}