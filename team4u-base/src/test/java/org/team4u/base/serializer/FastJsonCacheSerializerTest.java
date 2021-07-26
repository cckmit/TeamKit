package org.team4u.base.serializer;

public class FastJsonCacheSerializerTest extends AbstractCacheSerializerTest {

    @Override
    protected CacheableJsonSerializer serializer() {
        return FastJsonCacheSerializer.instance();
    }
}