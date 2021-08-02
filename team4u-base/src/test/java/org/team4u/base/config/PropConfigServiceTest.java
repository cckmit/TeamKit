package org.team4u.base.config;

import org.junit.Assert;
import org.junit.Test;

import java.util.Properties;

public class PropConfigServiceTest {

    private final Properties properties = new Properties();

    {
        {
            properties.put("a.x", "1");
        }
    }

    @Test
    public void get() {
        check("a");
        check("a.");
    }

    private void check(String prefix) {
        PropConfigService configService = new PropConfigService(prefix);
        configService.setProperties(properties);

        Assert.assertEquals("1", configService.get("x"));
        Assert.assertNull(configService.get("y"));
    }
}