package org.team4u.config.domain.repository;

import cn.hutool.core.lang.Pair;
import org.junit.Assert;
import org.junit.Test;

public class MapSimpleConfigRepositoryTest {

    @Test
    public void typeAndKey() {
        MapSimpleConfigRepository r = new MapSimpleConfigRepository(null);
        Pair<String, String> tk = r.typeAndKey("x.y.z");
        Assert.assertEquals("x.y", tk.getKey());
        Assert.assertEquals("z", tk.getValue());

        tk = r.typeAndKey("x.y");
        Assert.assertEquals("x", tk.getKey());
        Assert.assertEquals("y", tk.getValue());

        tk = r.typeAndKey(".x");
        Assert.assertEquals("", tk.getKey());
        Assert.assertEquals(".x", tk.getValue());

        tk = r.typeAndKey("x");
        Assert.assertEquals("", tk.getKey());
        Assert.assertEquals("x", tk.getValue());
    }
}