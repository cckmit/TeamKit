package org.team4u.config.infrastructure.persistence;

import lombok.Data;
import org.junit.Assert;
import org.junit.Test;
import org.team4u.config.application.SimpleConfigAppService;

public class SimpleConfigServiceTest {

    private final SimpleConfigAppService simpleConfigAppService = new SimpleConfigAppService(
            new PropSimpleConfigRepository("test.properties")
    );

    @Test
    public void get() {
        SimpleConfigService simpleConfigService = new SimpleConfigService(simpleConfigAppService);
        Assert.assertEquals("2", simpleConfigService.get("test2.b"));
        Assert.assertEquals(2, (long) simpleConfigService.get("test2.b", 2L));
    }

    @Data
    public static class A {

        private boolean a;
    }
}