package org.team4u.base.serializer.json;

import org.team4u.base.serializer.AbstractCacheableSerializer;
import org.team4u.base.serializer.Serializer;

/**
 * 基于fastjson带缓存功能的JSON序列化器
 *
 * @author jay.wu
 * @see FastJsonSerializer
 */
public class FastJsonCacheSerializer extends AbstractCacheableSerializer implements JsonCacheableSerializer {

    @Override
    protected Serializer newSerializer() {
        return new FastJsonSerializer();
    }
}