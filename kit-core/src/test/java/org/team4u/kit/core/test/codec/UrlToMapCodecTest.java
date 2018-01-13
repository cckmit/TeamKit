package org.team4u.kit.core.test.codec;

import cn.hutool.core.collection.CollectionUtil;
import org.junit.Assert;
import org.junit.Test;
import org.team4u.kit.core.codec.CodecRegistry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jay Wu
 */
public class UrlToMapCodecTest {

    @Test
    public void encode() {
        Map<String, String> x = CodecRegistry.URL_TO_MAP_CODEC.encode("x=1&y=2&y=1");
        Assert.assertEquals("{x=1, y=2,1}", x.toString());

        Map<String, List<String>> y = CodecRegistry.URL_TO_MAP_LIST_CODEC.encode("x=1&y=2&y=1");

        Assert.assertEquals("{x=[1], y=[2, 1]}", y.toString());
    }

    @Test
    public void decode() {
        Map<String, String> x = new HashMap<String, String>();
        x.put("x", "1");
        x.put("y", "2,1");

        String z = CodecRegistry.URL_TO_MAP_CODEC.decode(x);
        Assert.assertEquals("x=1&y=2%2C1", z);

        Map<String, List<String>> y = new HashMap<String, List<String>>();
        y.put("x", CollectionUtil.newArrayList("1"));
        y.put("y", CollectionUtil.newArrayList("2", "1"));

        z = CodecRegistry.URL_TO_MAP_LIST_CODEC.decode(y);
        Assert.assertEquals("x=1&y=2&y=1", z);
    }
}
