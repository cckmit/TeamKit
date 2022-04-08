package org.team4u.base.serializer.json;

import org.team4u.base.serializer.AbstractCacheableSerializer;
import org.team4u.base.serializer.Serializer;

/**
 * 基于hutool的可缓存序列化器
 *
 * @author jay.wu
 * @see HutoolJsonSerializer
 */
public class HutoolJsonCacheSerializer extends AbstractCacheableSerializer implements JsonCacheableSerializer {

    @Override
    protected Serializer newSerializer() {
        return new HutoolJsonSerializer();
    }
}