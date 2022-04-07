package org.team4u.base.serializer;

import org.team4u.base.serializer.json.HutoolJsonCacheSerializer;

public class HutoolJsonCacheSerializerTest extends AbstractCacheSerializerTest {

    @Override
    protected Serializer serializer() {
        return new HutoolJsonCacheSerializer();
    }
}