package org.team4u.base.registrar;

import cn.hutool.core.collection.CollUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.team4u.base.TestUtil;
import org.team4u.base.bean.provider.BeanProviders;
import org.team4u.base.bean.provider.LocalBeanProvider;

import java.util.List;
import java.util.stream.Collectors;

public class PolicyRegistrarTest {

    private final TestPolicyRegistrar registrar = new TestPolicyRegistrar();

    @Before
    public void setUp() {
        registrar.unregisterAllPolicies();
        LocalBeanProvider beanProvider = (LocalBeanProvider) BeanProviders.getInstance().availablePolicyOf(
                LocalBeanProvider.ID
        );
        beanProvider.unregisterAllBeans();
    }

    @Test
    public void keyPolicy() {
        registrar.registerPoliciesBySuper(this.getClass().getPackage().getName(), StringIdPolicy.class);

        Assert.assertEquals(TestIdPolicy.class, registrar.policyOf(TestUtil.TEST).getClass());
        Assert.assertEquals(Test2IdPolicy.class, registrar.policyOf(TestUtil.TEST2).getClass());

        Assert.assertEquals(
                CollUtil.newArrayList(TestIdPolicy.class, Test1IdPolicy.class),
                registrar.policiesOf(TestUtil.TEST).stream().map(StringIdPolicy::getClass).collect(Collectors.toList())
        );
        Assert.assertEquals(CollUtil.newArrayList(Test2IdPolicy.class, TestIdPolicy.class, Test1IdPolicy.class),
                registrar.policies().stream().map(StringIdPolicy::getClass).collect(Collectors.toList()));
    }

    @Test
    public void noSuchPolicy() {
        try {
            registrar.availablePolicyOf(TestUtil.TEST);
            Assert.fail();
        } catch (NoSuchPolicyException e) {
            Assert.assertEquals("Unable to find policy|context=" + TestUtil.TEST, e.getMessage());
        }
    }

    @Test
    public void unregister() {
        TestIdPolicy policy = new TestIdPolicy();

        registrar.registerPolicy(policy);
        Assert.assertNotNull(registrar.policyOf(policy.id()));

        registrar.unregisterPolicy(policy);
        Assert.assertNull(registrar.policyOf(policy.id()));
    }

    @Test
    public void registerByBeanInitializedEvent() {
        TestIdPolicy policy = new TestIdPolicy();

        registrar.registerPoliciesByBeanInitializedEvent();
        BeanProviders.getInstance().registerBean(policy);

        Assert.assertEquals(policy, registrar.policyOf(policy.id()));
    }

    @Test
    public void registerByBeanProviders() {
        TestIdPolicy policy = new TestIdPolicy();
        BeanProviders.getInstance().registerBean(policy);

        registrar.registerPoliciesByBeanProviders(BeanProviders.getInstance());

        Assert.assertEquals(policy, registrar.policyOf(policy.id()));
    }

    private static class TestPolicyRegistrar extends PolicyRegistrar<String, StringIdPolicy> {

        public TestPolicyRegistrar() {
        }

        public TestPolicyRegistrar(List<? extends StringIdPolicy> policies) {
            super(policies);
        }
    }

    private static class TestIdPolicy implements StringIdPolicy {

        @Override
        public String id() {
            return TestUtil.TEST;
        }

        @Override
        public int priority() {
            return 10;
        }
    }

    private static class Test1IdPolicy implements StringIdPolicy {

        @Override
        public String id() {
            return TestUtil.TEST;
        }

        @Override
        public int priority() {
            return 20;
        }
    }

    private static class Test2IdPolicy implements StringIdPolicy {

        @Override
        public String id() {
            return TestUtil.TEST2;
        }

        @Override
        public int priority() {
            return HIGHEST_PRECEDENCE;
        }
    }
}