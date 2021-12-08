package org.team4u.id.infrastructure.seq.value.mybatis;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.team4u.id.domain.seq.value.AbstractStepSequenceProviderTest;
import org.team4u.id.domain.seq.value.StepSequenceProvider;
import org.team4u.test.spring.DbTestBeanConfig;
import org.team4u.test.spring.SpringDbTest;

@ComponentScan("org.team4u.id.infrastructure.seq")
@ContextConfiguration(classes = DbTestBeanConfig.class)
public class MybatisStepSequenceProviderTest extends SpringDbTest {

    @Autowired
    private SequenceMapper mapper;

    private final X x = new X();

    private class X extends AbstractStepSequenceProviderTest {
        @Override
        protected StepSequenceProvider provider(StepSequenceProvider.Config config) {
            return new MybatisStepSequenceProvider(config, mapper);
        }
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