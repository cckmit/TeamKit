package org.team4u.kit.core.test.lb;

import cn.hutool.core.collection.CollectionUtil;
import org.junit.Assert;
import org.junit.Test;
import org.team4u.kit.core.action.Callback;
import org.team4u.kit.core.lang.Pair;
import org.team4u.kit.core.lb.RoundRobinPolicy;
import org.team4u.kit.core.lb.WeightRoundRobinPolicy;
import org.team4u.kit.core.util.MapExUtil;

public class RoundRobinPolicyTest {

    @Test
    public void invoke() {
        RoundRobinPolicy<Integer> policy = new RoundRobinPolicy<Integer>(CollectionUtil.newArrayList(1, 2, 3));

        // 1
        policy.request(new Callback<Integer>() {
            @Override
            public void invoke(Integer obj) {
                Assert.assertEquals(obj, Integer.valueOf(1));
            }
        });

        // 2
        policy.request(new Callback<Integer>() {
            @Override
            public void invoke(Integer obj) {
                Assert.assertEquals(obj, Integer.valueOf(2));
            }
        });

        // 3
        policy.request(new Callback<Integer>() {
            @Override
            public void invoke(Integer obj) {
                Assert.assertEquals(obj, Integer.valueOf(3));
            }
        });

        // 1
        policy.request(new Callback<Integer>() {
            @Override
            public void invoke(Integer obj) {
                Assert.assertEquals(obj, Integer.valueOf(1));
            }
        });

        // 2 will fail then get 3
        policy.request(new Callback<Integer>() {
            @Override
            public void invoke(Integer obj) {
                if (obj == 2) {
                    throw new RuntimeException("2");
                }

                Assert.assertEquals(obj, Integer.valueOf(3));
            }
        });


        // Get 3 again
        policy.request(new Callback<Integer>() {
            @Override
            public void invoke(Integer obj) {
                Assert.assertEquals(obj, Integer.valueOf(3));
            }
        });

        // All fail
        try {
            policy.request(new Callback<Integer>() {
                @Override
                public void invoke(Integer obj) {
                    throw new RuntimeException("2");
                }
            });
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals("2", e.getMessage());
        }
    }

    @Test
    public void select() {
        RoundRobinPolicy<Integer> policy = new RoundRobinPolicy<Integer>(CollectionUtil.newArrayList(1, 2, 3));
        Assert.assertEquals(Integer.valueOf(1), policy.select());
        Assert.assertEquals(Integer.valueOf(2), policy.select());
        Assert.assertEquals(Integer.valueOf(3), policy.select());
        Assert.assertEquals(Integer.valueOf(1), policy.select());
    }

    @Test
    public void weightSelect() {
        WeightRoundRobinPolicy<Integer> policy = new WeightRoundRobinPolicy<Integer>(MapExUtil.hashMapOf(
                new Pair<Integer, Integer>(1, 1),
                new Pair<Integer, Integer>(2, 2)));
        Assert.assertEquals(Integer.valueOf(1), policy.select());
        Assert.assertEquals(Integer.valueOf(2), policy.select());
        Assert.assertEquals(Integer.valueOf(2), policy.select());
        Assert.assertEquals(Integer.valueOf(1), policy.select());
    }
}
