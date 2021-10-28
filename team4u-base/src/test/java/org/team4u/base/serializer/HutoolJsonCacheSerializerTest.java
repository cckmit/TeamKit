package org.team4u.base.serializer;

public class HutoolJsonCacheSerializerTest extends AbstractCacheSerializerTest {

    @Override
    protected CacheableSerializer serializer() {
        return HutoolJsonCacheSerializer.instance();
    }
}