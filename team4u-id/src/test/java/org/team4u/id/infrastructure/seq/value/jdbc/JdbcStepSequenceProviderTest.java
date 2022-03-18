package org.team4u.id.infrastructure.seq.value.jdbc;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.RandomUtil;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.team4u.id.domain.seq.value.AbstractStepSequenceProviderTest;
import org.team4u.id.domain.seq.value.SequenceProvider;
import org.team4u.id.domain.seq.value.StepSequenceProvider;
import org.team4u.test.spring.DbTestBeanConfig;
import org.team4u.test.spring.SpringDbTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

@ContextConfiguration(classes = DbTestBeanConfig.class)
public class JdbcStepSequenceProviderTest extends SpringDbTest {

    private final X x = new X();

    private class X extends AbstractStepSequenceProviderTest {
        @Override
        protected StepSequenceProvider provider(StepSequenceProvider.Config config) {
            JdbcStepSequenceProvider.Config c = new JdbcStepSequenceProvider.Config();
            BeanUtil.copyProperties(config, c);
            return new JdbcStepSequenceProvider(c, jdbcTemplate().getDataSource());
        }
    }

    @Test
    public void create() {
        SequenceProvider provider = new JdbcStepSequenceProvider.Factory().create(
                FileUtil.readUtf8String("jdbc_step_config.json")
        );
        Assert.assertEquals(1, provider.provide(x.context()).intValue());
        Assert.assertEquals(2, provider.provide(x.context()).intValue());
        Assert.assertNull(provider.provide(x.context()));
    }

    @Test
    public void concurrent2() {
        JdbcStepSequenceProvider.Config c = new JdbcStepSequenceProvider.Config();

        JdbcStepSequenceProvider provider = new JdbcStepSequenceProvider(
                c,
                jdbcTemplate().getDataSource()
        );

        List<JdbcStepSequenceProvider> providers = copy(provider, 8);
        List<Future<?>> result = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            result.add(ThreadUtil.execAsync(() -> providers.get(RandomUtil.randomInt(0, 7)).provide(x.context())));
        }

        result.forEach(it -> {
            try {
                it.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Assert.assertEquals(101, provider.provide(x.context()).intValue());
        Assert.assertFalse(provider.isEmpty(x.context()));
    }

    private List<JdbcStepSequenceProvider> copy(JdbcStepSequenceProvider provider, int n) {
        List<JdbcStepSequenceProvider> providers = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            providers.add(new JdbcStepSequenceProvider(provider.config(), jdbcTemplate().getDataSource()));
        }
        return providers;
    }

    @Test
    public void concurrent() {
        x.concurrent();
    }

    @Test
    public void notRecycle() {
        x.notRecycle();
    }

    @Test
    public void recycle1() {
        x.recycle1();
    }

    @Test
    public void recycle2() {
        x.recycle2();
    }

    @Test
    public void recycle3() {
        x.recycle3();
    }

    @Test
    public void recycle4() {
        x.recycle4();
    }

    @Override
    protected String[] ddlResourcePaths() {
        return new String[]{"sql/seq.sql"};
    }
}