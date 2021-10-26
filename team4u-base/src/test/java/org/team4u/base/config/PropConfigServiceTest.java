package org.team4u.base.config;

import cn.hutool.core.collection.CollUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Properties;

public class PropConfigServiceTest {

    private final Properties properties = new Properties();

    {
        {
            properties.put("a.x", "1");
            properties.put("a.z", "1,2");
        }
    }

    @Test
    public void get() {
        check(null, "a.x", "1", null);
        check("a", "x", "1", null);
        check("a.", "x", "1", null);
        check("a", "z", CollUtil.newArrayList("1", "2"), new ArrayList<String>());
    }

    private void check(String prefix, String key, Object expectedValue, Object defaultValue) {
        PropConfigService configService = new PropConfigService(prefix);
        configService.setProperties(properties);

        if (defaultValue == null) {
            Assert.assertEquals(expectedValue, configService.get(key));
        } else {
            Assert.assertEquals(expectedValue, configService.get(key, defaultValue));
        }
        Assert.assertNull(configService.get("y"));
    }
}