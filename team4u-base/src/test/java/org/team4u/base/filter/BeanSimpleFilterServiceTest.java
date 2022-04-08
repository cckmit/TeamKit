package org.team4u.base.filter;

import cn.hutool.core.lang.Dict;
import org.junit.After;
import org.junit.Before;
import org.team4u.base.bean.event.ApplicationInitializedEvent;
import org.team4u.base.bean.provider.BeanProviders;
import org.team4u.base.message.jvm.MessagePublisher;

import java.util.List;
import java.util.stream.Collectors;

public class BeanSimpleFilterServiceTest extends AbstractSimpleFilterServiceTest {

    private final BeanFilterService<Dict> service = new TestFilterService();

    @Before
    @After
    public void before() throws Exception {
        for (Filter<Dict> filter : filters) {
            BeanProviders.getInstance().local().registerBean(filter);
        }

        MessagePublisher.instance().reset();
        MessagePublisher.instance().subscribe(service);
        MessagePublisher.instance().publish(new ApplicationInitializedEvent());
    }

    @Override
    protected FilterService<Dict> filterService() {
        return service;
    }

    private static class TestFilterService extends BeanFilterService<Dict> {
        @SuppressWarnings("unchecked")
        @Override
        protected List<Class<? extends Filter<Dict>>> filterClasses() {
            return filters.stream().map(it -> (Class<Filter<Dict>>) it.getClass()).collect(Collectors.toList());
        }
    }
}