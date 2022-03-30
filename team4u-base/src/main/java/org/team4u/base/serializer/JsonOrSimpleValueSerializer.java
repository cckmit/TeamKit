package org.team4u.base.serializer;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.TypeUtil;
import cn.hutool.json.JSONUtil;

import java.lang.reflect.Type;

/**
 * 简单类型或者JSON类型序列化器
 *
 * @author jay.wu
 */
public class JsonOrSimpleValueSerializer implements Serializer {

    private final static Serializer noCacheInstance = new JsonOrSimpleValueSerializer(false);
    private final static Serializer cacheInstance = new JsonOrSimpleValueSerializer(true);

    protected final boolean cache;

    public JsonOrSimpleValueSerializer(boolean cache) {
        this.cache = cache;
    }

    public static Serializer noCache() {
        return noCacheInstance;
    }

    public static Serializer cache() {
        return cacheInstance;
    }

    @Override
    public String serialize(Object value) {
        if (ClassUtil.isSimpleTypeOrArray(value.getClass())) {
            return simpleSerializer(cache).serialize(value);
        }

        return jsonSerializer(cache).serialize(value);
    }

    @Override
    public <T> T deserialize(String serialization, Class<T> type) {
        if (isSimpleType(serialization, type)) {
            return simpleSerializer(cache).deserialize(serialization, type);
        }

        return jsonSerializer(cache).deserialize(serialization, type);
    }

    @Override
    public <T> T deserialize(String serialization, Type type) {
        if (isSimpleType(serialization, TypeUtil.getClass(type))) {
            return simpleSerializer(cache).deserialize(serialization, type);
        }

        return jsonSerializer(cache).deserialize(serialization, type);
    }

    private boolean isSimpleType(String value, Class<?> toType) {
        return ClassUtil.isSimpleTypeOrArray(toType) ||
                !JSONUtil.isJson(value);
    }

    protected Serializer simpleSerializer(boolean isCacheResult) {
        // 缓存结果对象，仅value不同时进行反序列化
        if (isCacheResult) {
            return SimpleValueCacheSerializer.instance();
        }

        // 不缓存结果对象，每次进行反序列化
        return SimpleValueSerializer.instance();
    }

    protected Serializer jsonSerializer(boolean isCacheResult) {
        // 缓存结果对象，仅value不同时进行反序列化
        if (isCacheResult) {
            return HutoolJsonCacheSerializer.instance();
        }

        // 不缓存结果对象，每次进行反序列化
        return HutoolJsonSerializer.instance();
    }
}