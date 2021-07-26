package org.team4u.base.serializer;

import cn.hutool.cache.CacheUtil;

/**
 * 基于fastjson的可缓存序列化器
 *
 * @author jay.wu
 */
public class FastJsonCacheSerializer {

    private final static CacheableJsonSerializer instance = new CacheableJsonSerializer(
            FastJsonSerializer.instance(), CacheUtil.newLRUCache(10000)
    );

    public static CacheableJsonSerializer instance() {
        return instance;
    }
}