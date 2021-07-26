package org.team4u.base.serializer;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.lang.TypeReference;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public abstract class AbstractCacheSerializerTest {

    @Test
    public void serialize() {
        Dict data = Dict.create().set("a", "1");
        String a = serializer().serialize(data);
        String b = serializer().serialize(data);
        Assert.assertEquals(a, b);
    }

    @Test
    public void classDeserialize() {
        String json = "{ 'a' : 1 }";
        Dict a = serializer().deserialize(json, Dict.class);
        Dict b = serializer().deserialize(json, Dict.class);
        Assert.assertEquals(a, b);
    }

    @Test
    public void typeDeserialize() {
        String json = "[{ 'a' : 1 }]";
        List<Dict> a = serializer().deserialize(json, new TypeReference<List<Dict>>() {
        }.getType());
        List<Dict> b = serializer().deserialize(json, new TypeReference<List<Dict>>() {
        }.getType());
        Assert.assertEquals(a, b);
    }

    protected abstract CacheableJsonSerializer serializer();
}