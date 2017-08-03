package org.team4u.kit.core.test.util;


import org.junit.Assert;
import org.junit.Test;
import org.team4u.kit.core.util.UrlUtil;

import java.util.Map;

public class UrlUtilTest {

    @Test
    public void deserialize() {
        Map<String, String> map = UrlUtil.deserialize("a=1&b=2&c=&d");
        Assert.assertEquals(2, map.size());
        Assert.assertEquals("1", map.get("a"));
        Assert.assertEquals("2", map.get("b"));
    }
}