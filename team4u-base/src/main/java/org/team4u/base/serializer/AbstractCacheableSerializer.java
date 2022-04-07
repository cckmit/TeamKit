package org.team4u.base.serializer;

import java.lang.reflect.Type;

/**
 * 抽象可缓存序列化器
 *
 * @author jay.wu
 */
public abstract class AbstractCacheableSerializer implements Serializer {

    private final CacheableSerializer cacheableSerializer = new CacheableSerializer(
            newSerializer()
    );

    @Override
    public String serialize(Object value) {
        return cacheableSerializer.serialize(value);
    }

    @Override
    public <T> T deserialize(String serialization, Class<T> type) {
        return cacheableSerializer.deserialize(serialization, type);
    }

    @Override
    public <T> T deserialize(String serialization, Type type) {
        return cacheableSerializer.deserialize(serialization, type);
    }

    protected abstract Serializer newSerializer();
}