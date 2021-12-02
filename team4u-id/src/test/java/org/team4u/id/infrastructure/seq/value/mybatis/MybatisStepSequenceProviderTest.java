package org.team4u.id.infrastructure.seq.value.mybatis;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.team4u.id.domain.seq.SequenceConfig;
import org.team4u.id.domain.seq.value.SequenceProvider;
import org.team4u.id.domain.seq.value.StepSequenceProvider;
import org.team4u.test.spring.DbTestBeanConfig;
import org.team4u.test.spring.SpringDbTest;

@ComponentScan("org.team4u.id.infrastructure.seq")
@ContextConfiguration(classes = DbTestBeanConfig.class)
public class MybatisStepSequenceProviderTest extends SpringDbTest {

    @Autowired
    private SequenceMapper mapper;

    @Test
    public void notRecycle() {
        StepSequenceProvider.Config config = new StepSequenceProvider.Config();
        config.setMaxValue(2L);
        MybatisStepSequenceProvider provider = new MybatisStepSequenceProvider(config, mapper);

        Assert.assertEquals(1L, provider.provide(context()));
        Assert.assertEquals(2L, provider.provide(context()));
        Assert.assertNull(provider.provide(context()));
    }

    @Test
    public void recycle() {
        StepSequenceProvider.Config config = new StepSequenceProvider.Config();
        config.setMaxValue(2L);
        config.setRecycleAfterMaxValue(true);
        MybatisStepSequenceProvider provider = new MybatisStepSequenceProvider(config, mapper);

        Assert.assertEquals(1L, provider.provide(context()));
        Assert.assertEquals(2L, provider.provide(context()));
        Assert.assertEquals(1L, provider.provide(context()));
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

    @Override
    protected String[] ddlResourcePaths() {
        return new String[]{"sql/seq.sql"};
    }
}