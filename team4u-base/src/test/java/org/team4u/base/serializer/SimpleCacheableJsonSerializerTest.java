package org.team4u.base.serializer;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.lang.TypeReference;
import org.junit.Assert;
import org.junit.Test;

public class SimpleCacheableJsonSerializerTest {

    @Test
    public void serialize() {
        Dict data = Dict.create().set("a", "1");
        String a = HutoolJsonCacheSerializer.instance().serialize(data);
        String b = HutoolJsonCacheSerializer.instance().serialize(data);
        Assert.assertEquals(a, b);
    }

    @Test
    public void classDeserialize() {
        String json = "{ 'a' : 1 }";
        Dict a = HutoolJsonCacheSerializer.instance().deserialize(json, Dict.class);
        Dict b = HutoolJsonCacheSerializer.instance().deserialize(json, Dict.class);
        Assert.assertEquals(a, b);
    }

    @Test
    public void typeDeserialize() {
        String json = "[{ 'a' : 1 }]";
        Dict a = HutoolJsonCacheSerializer.instance().deserialize(json, new TypeReference<Dict>() {
        }.getType());
        Dict b = HutoolJsonCacheSerializer.instance().deserialize(json, new TypeReference<Dict>() {
        }.getType());
        Assert.assertEquals(a, b);
    }
}