package org.team4u.base.serializer;

public class HutoolJsonCacheSerializerTest extends AbstractCacheSerializerTest {

    @Override
    protected CacheableJsonSerializer serializer() {
        return HutoolJsonCacheSerializer.instance();
    }
}