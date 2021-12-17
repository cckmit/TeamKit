package org.team4u.id.infrastructure.seq.value.jdbc;

import cn.hutool.core.bean.BeanUtil;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.team4u.id.domain.seq.value.AbstractStepSequenceProviderTest;
import org.team4u.id.domain.seq.value.StepSequenceProvider;
import org.team4u.test.spring.DbTestBeanConfig;
import org.team4u.test.spring.SpringDbTest;

@ContextConfiguration(classes = DbTestBeanConfig.class)
public class JdbcStepSequenceProviderTest extends SpringDbTest {

    private final JdbcStepSequenceProviderTest.X x = new JdbcStepSequenceProviderTest.X();

    private class X extends AbstractStepSequenceProviderTest {
        @Override
        protected StepSequenceProvider provider(StepSequenceProvider.Config config) {
            JdbcStepSequenceProvider.Config c = new JdbcStepSequenceProvider.Config();
            BeanUtil.copyProperties(config, c);
            return new JdbcStepSequenceProvider(c, jdbcTemplate().getDataSource());
        }
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