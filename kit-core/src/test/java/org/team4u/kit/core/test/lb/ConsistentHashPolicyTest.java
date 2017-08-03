package org.team4u.kit.core.test.lb;

import com.xiaoleilu.hutool.util.CollectionUtil;
import org.junit.Assert;
import org.junit.Test;
import org.team4u.kit.core.lb.ConsistentHashPolicy;

import java.util.List;

public class ConsistentHashPolicyTest {

    private List<ConsistentHashPolicy.PhysicalNode> createPhysicalNodes() {
        return CollectionUtil.newArrayList(
                new ConsistentHashPolicy.PhysicalNode("T", "127.0.0.1", 1),
                new ConsistentHashPolicy.PhysicalNode("T", "127.0.0.1", 2),
                new ConsistentHashPolicy.PhysicalNode("T", "127.0.0.1", 3)
        );
    }

    @Test
    public void getNode() {
        ConsistentHashPolicy policy = new ConsistentHashPolicy(createPhysicalNodes(), 1);
        Assert.assertEquals(1, policy.getNode("1").getPort());
        Assert.assertEquals(2, policy.getNode("3").getPort());
        Assert.assertEquals(3, policy.getNode("7").getPort());
        Assert.assertEquals(1, policy.getNode("4").getPort());
    }

    @Test
    public void addNode() {
        ConsistentHashPolicy policy = new ConsistentHashPolicy(createPhysicalNodes(), 1);
        policy.addNode(new ConsistentHashPolicy.PhysicalNode("T", "127.0.0.1", 4), 1);

        Assert.assertEquals(1, policy.getNode("1").getPort());
        Assert.assertEquals(2, policy.getNode("3").getPort());
        Assert.assertEquals(3, policy.getNode("7").getPort());
        Assert.assertEquals(4, policy.getNode("4").getPort());
    }

    @Test
    public void removeNode() {
        ConsistentHashPolicy policy = new ConsistentHashPolicy(createPhysicalNodes(), 1);
        policy.addNode(new ConsistentHashPolicy.PhysicalNode("T", "127.0.0.1", 4), 1);
        policy.removeNode(new ConsistentHashPolicy.PhysicalNode("T", "127.0.0.1", 4));

        Assert.assertEquals(1, policy.getNode("1").getPort());
        Assert.assertEquals(2, policy.getNode("3").getPort());
        Assert.assertEquals(3, policy.getNode("7").getPort());
        Assert.assertEquals(1, policy.getNode("4").getPort());
    }
}