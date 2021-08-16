package org.team4u.base.bean.provider;

import org.junit.Assert;
import org.junit.Test;
import org.team4u.base.TestUtil;

import java.util.List;
import java.util.stream.Collectors;

public class BeanProvidersTest {

    private final BeanProviders providers = new BeanProviders();
    private final MockBeanProvider mockBeanProvider = new MockBeanProvider();

    @Test
    public void getBeansOfType() {
        providers.saveIdObject(mockBeanProvider);

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
        Assert.assertTrue(providers.registerBean(TestUtil.TEST, mockBeanProvider));
        Assert.assertEquals(mockBeanProvider, providers.getBean(TestUtil.TEST));
    }

    public static class MockBeanProvider extends LocalBeanProvider {
        @Override
        public String id() {
            return TestUtil.TEST;
        }

        @Override
        public int priority() {
            return super.priority() + 1;
        }
    }
}