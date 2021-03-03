package org.team4u.config.infrastructure.persistence;

import org.junit.Assert;
import org.junit.Test;
import org.team4u.config.application.SimpleConfigAppService;

public class SimpleConfigServiceTest {

    private final SimpleConfigAppService simpleConfigAppService = new SimpleConfigAppService(
            new PropSimpleConfigRepository("test.properties")
    );

    @Test
    public void get() {
        SimpleConfigService simpleConfigService = new SimpleConfigService("test2", simpleConfigAppService);
        Assert.assertEquals("2", simpleConfigService.get("b"));
        Assert.assertEquals(2, (long) simpleConfigService.get("b", 2L));
    }
}