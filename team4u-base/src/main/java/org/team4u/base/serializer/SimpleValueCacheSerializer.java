package org.team4u.base.serializer;

import cn.hutool.core.lang.Singleton;

/**
 * 简单类型可缓存序列化器
 *
 * @author jay.wu
 * @see SimpleValueCacheSerializer
 */
public class SimpleValueCacheSerializer extends AbstractCacheableSerializer {

    public static SimpleValueCacheSerializer getInstance() {
        return Singleton.get(SimpleValueCacheSerializer.class);
    }

    @Override
    protected Serializer newSerializer() {
        return SimpleValueSerializer.getInstance();
    }
}