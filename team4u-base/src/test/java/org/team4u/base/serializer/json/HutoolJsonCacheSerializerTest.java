package org.team4u.base.serializer.json;

import org.team4u.base.serializer.Serializer;


public class HutoolJsonCacheSerializerTest extends AbstractCacheSerializerTest {

    @Override
    protected Serializer serializer() {
        return new HutoolJsonCacheSerializer();
    }
}