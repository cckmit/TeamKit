package org.team4u.base.serializer;

import org.team4u.base.serializer.json.FastJsonCacheSerializer;

public class FastJsonCacheSerializerTest extends AbstractCacheSerializerTest {

    @Override
    protected Serializer serializer() {
        return new FastJsonCacheSerializer();
    }
}