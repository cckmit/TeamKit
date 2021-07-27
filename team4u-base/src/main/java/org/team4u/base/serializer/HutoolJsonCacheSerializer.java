package org.team4u.base.serializer;

import cn.hutool.cache.CacheUtil;

/**
 * 基于hutool的可缓存序列化器
 *
 * @author jay.wu
 */
public class HutoolJsonCacheSerializer {

    private final static CacheableJsonSerializer instance = new CacheableJsonSerializer(
            HutoolJsonSerializer.instance(), CacheUtil.newLRUCache(1000)
    );

    public static CacheableJsonSerializer instance() {
        return instance;
    }
}