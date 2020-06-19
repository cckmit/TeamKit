package org.team4u.kv;

import org.team4u.kv.infrastruture.resource.SimpleStoreResourceService;
import org.team4u.kv.resource.StoreResource;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class SimpleStoreResourceServiceTest {

    @Test
    public void resources() {
        SimpleStoreResourceService service = newSimpleStoreResourceService();
        List<StoreResource> resources = service.resources();

        Assert.assertEquals(2, resources.size());
        Assert.assertEquals(new StoreResource("0", "DB", "T"), resources.get(0));
        Assert.assertEquals(new StoreResource("1", "DB", "T"), resources.get(1));
    }

    @Test
    public void select() {
        SimpleStoreResourceService service = newSimpleStoreResourceService();
        Assert.assertEquals("0", service.select("x").getId());
        Assert.assertEquals("1", service.select("a").getId());
    }

    private SimpleStoreResourceService newSimpleStoreResourceService() {
        SimpleStoreResourceService.Config config = new SimpleStoreResourceService.Config()
                .setMaxResourceCount(4)
                .setResourceCount(2)
                .setResourceName("T")
                .setResourceType("DB");
        return new SimpleStoreResourceService(config);
    }
}