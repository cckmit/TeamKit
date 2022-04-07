package org.team4u.base.serializer.json;

import org.team4u.base.serializer.AbstractCacheableSerializer;
import org.team4u.base.serializer.Serializer;

/**
 * 基于fastjson的可缓存序列化器
 *
 * @author jay.wu
 */
public class FastJsonCacheSerializer extends AbstractCacheableSerializer implements JsonCacheableSerializer {

    @Override
    protected Serializer newSerializer() {
        return new FastJsonSerializer();
    }

    @Override
    public int priority() {
        return 10;
    }
}