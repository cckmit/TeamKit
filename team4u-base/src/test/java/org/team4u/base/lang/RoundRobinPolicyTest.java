package org.team4u.base.lang;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.func.VoidFunc1;
import org.junit.Assert;
import org.junit.Test;
import org.team4u.base.error.NoAvailableResourceException;
import org.team4u.base.lang.RoundRobinPolicy;

public class RoundRobinPolicyTest {

    @Test
    public void invoke() {
        RoundRobinPolicy<Integer> policy = new RoundRobinPolicy<Integer>(CollectionUtil.newArrayList(1, 2, 3));

        // 1
        policy.request(obj -> {
            Assert.assertEquals(obj, Integer.valueOf(1));
        });

        // 2
        policy.request(obj -> {
            Assert.assertEquals(obj, Integer.valueOf(2));
        });

        // 3
        policy.request(obj -> {
            Assert.assertEquals(obj, Integer.valueOf(3));
        });

        // 1
        policy.request(obj -> {
            Assert.assertEquals(obj, Integer.valueOf(1));
        });

        // 2 will fail then get 3
        policy.request(obj -> {
            if (obj == 2) {
                throw new RuntimeException("2");
            }

            Assert.assertEquals(obj, Integer.valueOf(3));
        });


        // Get 3 again
        policy.request(obj -> {
            Assert.assertEquals(obj, Integer.valueOf(3));
        });

        // All fail
        try {
            policy.request((VoidFunc1<Integer>) obj -> {
                throw new RuntimeException("2");
            });
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(NoAvailableResourceException.class, e.getClass());
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
}
