package org.team4u.base.serializer.simple;

import cn.hutool.core.convert.Convert;
import org.team4u.base.serializer.Serializer;

import java.lang.reflect.Type;

/**
 * 简单类型序列化器
 *
 * @author jay.wu
 */
public class SimpleValueSerializer implements Serializer {

    private final static Serializer instance = new SimpleValueSerializer();

    public static Serializer getInstance() {
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

    @Override
    public boolean supports(Object context) {
        // 兜底策略
        return true;
    }

    @Override
    public int priority() {
        return 100;
    }
}