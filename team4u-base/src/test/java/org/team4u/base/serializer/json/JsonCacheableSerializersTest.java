package org.team4u.base.serializer.json;

import org.team4u.base.serializer.Serializer;

public class JsonCacheableSerializersTest extends AbstractCacheSerializerTest {

    @Override
    protected Serializer serializer() {
        return JsonCacheableSerializers.getInstance();
    }
}