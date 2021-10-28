package org.team4u.base.serializer;

import cn.hutool.cache.CacheUtil;

/**
 * 简单类型可缓存序列化器
 *
 * @author jay.wu
 * @see SimpleValueCacheSerializer
 */
public class SimpleValueCacheSerializer {

    private final static CacheableSerializer instance = new CacheableSerializer(
            SimpleValueSerializer.instance(), CacheUtil.newLRUCache(1000)
    );

    public static CacheableSerializer instance() {
        return instance;
    }
}