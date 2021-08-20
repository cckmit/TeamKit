package org.team4u.base.registrar;

import cn.hutool.core.collection.CollUtil;
import org.junit.Assert;
import org.junit.Test;
import org.team4u.base.TestUtil;

import java.util.List;

public class PolicyRegistrarTest {

    @Test
    public void keyPolicy() {
        TestPolicyRegistrar registrar = new TestPolicyRegistrar();

        TestIdPolicy policy1 = new TestIdPolicy(TestUtil.TEST);
        TestIdPolicy policy2 = new TestIdPolicy(TestUtil.TEST1);
        TestIdPolicy policy3 = new TestIdPolicy(TestUtil.TEST2) {
            @Override
            public int priority() {
                return HIGHEST_PRECEDENCE;
            }
        };
        registrar.register(policy1);
        registrar.register(policy2);
        registrar.register(policy3);

        Assert.assertEquals(policy1, registrar.policyOf(TestUtil.TEST));
        Assert.assertEquals(policy2, registrar.policyOf(TestUtil.TEST1));
        Assert.assertEquals(CollUtil.newArrayList(policy3,policy1, policy2), registrar.policies());
    }

    private static class TestPolicyRegistrar extends PolicyRegistrar<String, StringIdPolicy> {

        public TestPolicyRegistrar() {
        }

        public TestPolicyRegistrar(List<? extends StringIdPolicy> policies) {
            super(policies);
        }
    }

    private static class TestIdPolicy implements StringIdPolicy {

        private final String id;

        private TestIdPolicy(String id) {
            this.id = id;
        }

        @Override
        public String id() {
            return id;
        }
    }
}