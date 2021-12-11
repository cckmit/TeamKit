package org.team4u.base.serializer;

/**
 * 基于fastjson的可缓存序列化器
 *
 * @author jay.wu
 */
public class FastJsonCacheSerializer {

    private final static CacheableSerializer instance = new CacheableSerializer(
            FastJsonSerializer.instance()
    );

    public static CacheableSerializer instance() {
        return instance;
    }
}