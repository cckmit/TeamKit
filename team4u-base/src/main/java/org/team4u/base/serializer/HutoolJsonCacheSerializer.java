package org.team4u.base.serializer;

import cn.hutool.cache.CacheUtil;

/**
 * 基于hutool的可缓存序列化器
 *
 * @author jay.wu
 */
public class HutoolJsonCacheSerializer {

    private final static Serializer instance = new CacheableJsonSerializer(
            HutoolJsonSerializer.instance(), CacheUtil.newLRUCache(10000)
    );

    public static Serializer instance() {
        return instance;
    }
}