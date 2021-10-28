package org.team4u.base.serializer;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ClassUtil;

import java.lang.reflect.Type;

/**
 * 简单类型序列化器
 *
 * @author jay.wu
 * @see ClassUtil isSimpleTypeOrArray
 */
public class SimpleValueSerializer implements Serializer {

    private final static Serializer instance = new SimpleValueSerializer();

    public static Serializer instance() {
        return instance;
    }

    @Override
    public String serialize(Object value) {
        return Convert.toStr(value);
    }

    @Override
    public <T> T deserialize(String serialization, Class<T> type) {
        return Convert.convert(type, serialization);
    }

    @Override
    public <T> T deserialize(String serialization, Type type) {
        return Convert.convert(type, serialization);
    }
}
