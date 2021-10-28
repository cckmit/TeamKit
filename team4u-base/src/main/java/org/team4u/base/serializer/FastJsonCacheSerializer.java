package org.team4u.base.serializer;

import cn.hutool.cache.CacheUtil;

/**
 * 基于fastjson的可缓存序列化器
 *
 * @author jay.wu
 */
public class FastJsonCacheSerializer {

    private final static CacheableSerializer instance = new CacheableSerializer(
            FastJsonSerializer.instance(), CacheUtil.newLRUCache(1000)
    );

    public static CacheableSerializer instance() {
        return instance;
    }
}