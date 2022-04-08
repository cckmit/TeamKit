package org.team4u.base.serializer.json;

import cn.hutool.core.lang.Singleton;
import org.team4u.base.serializer.AbstractSerializers;

/**
 * 具有缓存功能的JSON组合序列化器
 *
 * @author jay.wu
 * @see JsonSerializers
 */
public class JsonCacheableSerializers extends AbstractSerializers<JsonCacheableSerializer> implements JsonCacheableSerializer {

    public static JsonCacheableSerializers getInstance() {
        return Singleton.get(JsonCacheableSerializers.class);
    }
}