package org.team4u.base.serializer;

/**
 * 基于hutool的可缓存序列化器
 *
 * @author jay.wu
 */
public class HutoolJsonCacheSerializer {

    private final static CacheableSerializer instance = new CacheableSerializer(
            HutoolJsonSerializer.instance()
    );

    public static CacheableSerializer instance() {
        return instance;
    }
}