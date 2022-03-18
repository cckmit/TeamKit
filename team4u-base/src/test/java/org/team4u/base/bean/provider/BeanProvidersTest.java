package org.team4u.base.bean.provider;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.team4u.base.TestUtil;

import java.util.List;
import java.util.stream.Collectors;

public class BeanProvidersTest {

    private BeanProviders providers;
    private final MockBeanProvider mockBeanProvider = new MockBeanProvider();

    @Before
    @After
    public void setUp() {
        providers = new BeanProviders();
        providers.registerPolicy(mockBeanProvider);
    }

    @Test
    public void getBeansOfType() {
        providers.availablePolicyOf(LocalBeanProvider.ID).registerBean(TestUtil.TEST, TestUtil.TEST);
        Assert.assertTrue(mockBeanProvider.registerBean(TestUtil.TEST1, TestUtil.TEST1));
        Assert.assertTrue(mockBeanProvider.registerBean(TestUtil.TEST, TestUtil.TEST2));

        List<String> result = providers.getBeansOfType(String.class)
                .entrySet()
                .stream()
                .map(it -> it.getKey() + "=" + it.getValue())
                .collect(Collectors.toList());
        Assert.assertEquals("[test=test2, test1=test1]", result.toString());
    }

    @Test
    public void getBeanWithFilter() {
        providers.registerBean(TestUtil.TEST, TestUtil.TEST1);
        providers.availablePolicyOf(LocalBeanProvider.ID).registerBean(TestUtil.TEST, TestUtil.TEST);

        Assert.assertEquals(TestUtil.TEST1, providers.getBean(TestUtil.TEST.getClass(), TestUtil.TEST));
        Assert.assertEquals(TestUtil.TEST1, providers.getBean(TestUtil.TEST.getClass(), "tes.+"));
        Assert.assertEquals(TestUtil.TEST1, providers.getBean(TestUtil.TEST.getClass(), ""));
    }

    @Test
    public void getBean() {
        providers.registerBean(TestUtil.TEST, TestUtil.TEST1);
        providers.availablePolicyOf(LocalBeanProvider.ID).registerBean(TestUtil.TEST, TestUtil.TEST);

        Assert.assertEquals(TestUtil.TEST1, providers.getBean(TestUtil.TEST));
    }

    @Test
    public void notExistBean() {
        try {
            providers.getBean("x");
            Assert.fail();
        } catch (NoSuchBeanDefinitionException e) {
            Assert.assertEquals("name=x", e.getMessage());
        }
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