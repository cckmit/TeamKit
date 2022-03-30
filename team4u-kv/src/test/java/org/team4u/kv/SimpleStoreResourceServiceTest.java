package org.team4u.kv;

import org.junit.Assert;
import org.junit.Test;
import org.team4u.kv.infrastructure.resource.SimpleStoreResourceService;
import org.team4u.kv.resource.StoreResource;

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
        SimpleStoreResourceService.Config config = SimpleStoreResourceService.Config.builder()
                .maxResourceCount(4)
                .resourceCount(2)
                .resourceName("T")
                .resourceType("DB")
                .build();
        return new SimpleStoreResourceService(config);
    }
}