package org.team4u.base.bean.provider;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.team4u.base.TestUtil;

import java.util.List;
import java.util.stream.Collectors;

public class BeanProvidersTest {

    private final BeanProviders providers = new BeanProviders();
    private final MockBeanProvider mockBeanProvider = new MockBeanProvider();

    @Before
    public void setUp() {
        providers.saveIdObject(mockBeanProvider);
    }

    @Test
    public void getBeansOfType() {
        Assert.assertTrue(providers.registerBean(TestUtil.TEST, mockBeanProvider));
        Assert.assertTrue(mockBeanProvider.registerBean(TestUtil.TEST1, mockBeanProvider));

        List<String> result = providers.getBeansOfType(BeanProvider.class)
                .entrySet()
                .stream()
                .map(it -> it.getKey() + "=" + mockBeanProvider.equals(it.getValue()))
                .collect(Collectors.toList());
        Assert.assertEquals("[test=true, test1=true]", result.toString());
    }

    @Test
    public void getBean() {
        providers.registerBean(TestUtil.TEST, TestUtil.TEST1);
        providers.availableObjectOfId(LocalBeanProvider.ID).registerBean(TestUtil.TEST, TestUtil.TEST);

        Assert.assertEquals(TestUtil.TEST1, providers.getBean(TestUtil.TEST));
    }

    public static class MockBeanProvider extends LocalBeanProvider {
        @Override
        public String id() {
            return TestUtil.TEST;
        }

        @Override
        public int priority() {
            return super.priority() - 1;
        }
    }
}