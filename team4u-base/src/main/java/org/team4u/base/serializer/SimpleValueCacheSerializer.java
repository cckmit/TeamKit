package org.team4u.base.serializer;

/**
 * 简单类型可缓存序列化器
 *
 * @author jay.wu
 * @see SimpleValueCacheSerializer
 */
public class SimpleValueCacheSerializer {

    private final static CacheableSerializer instance = new CacheableSerializer(
            SimpleValueSerializer.instance()
    );

    public static CacheableSerializer instance() {
        return instance;
    }
}