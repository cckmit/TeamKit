package org.team4u.base.serializer;

public class FastJsonCacheSerializerTest extends AbstractCacheSerializerTest {

    @Override
    protected CacheableSerializer serializer() {
        return FastJsonCacheSerializer.instance();
    }
}