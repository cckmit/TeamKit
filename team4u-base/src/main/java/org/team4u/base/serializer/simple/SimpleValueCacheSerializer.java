package org.team4u.base.serializer.simple;

import cn.hutool.core.lang.Singleton;
import org.team4u.base.serializer.AbstractCacheableSerializer;
import org.team4u.base.serializer.Serializer;

/**
 * 简单类型可缓存序列化器
 *
 * @author jay.wu
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