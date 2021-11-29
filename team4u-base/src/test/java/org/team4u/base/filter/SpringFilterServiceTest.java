package org.team4u.base.filter;

import cn.hutool.core.lang.Dict;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.team4u.base.bean.provider.BeanProviders;
import org.team4u.test.spring.BaseTestBeanConfig;

import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringFilterServiceTest.TestBeanConfig.class)
@ActiveProfiles(value = "test")
public class SpringFilterServiceTest extends AbstractFilterServiceTest {

    @Autowired
    private TestFilterService testFilterService;

    @Override
    protected FilterService<Dict> filterService() {
        return testFilterService;
    }


    public static class TestFilterService extends SpringFilterService<Dict> {
        @SuppressWarnings("unchecked")
        @Override
        protected List<Class<? extends Filter<Dict>>> filterClasses() {
            return filters.stream().map(it -> (Class<Filter<Dict>>) it.getClass()).collect(Collectors.toList());
        }
    }

    @Import({BaseTestBeanConfig.class})
    @Configuration
    public static class TestBeanConfig {

        @Bean
        public List<? extends Filter<Dict>> mockFilters() {
            for (Filter<Dict> filter : filters) {
                BeanProviders.getInstance().registerBean(filter);
            }

            return filters;
        }

        @Bean
        public TestFilterService filterService(List<? extends Filter<Dict>> mockFilters) {
            return new TestFilterService();
        }
    }
}