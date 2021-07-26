package org.team4u.base.serializer;

import cn.hutool.json.JSONUtil;

import java.lang.reflect.Type;

/**
 * 基于hutool的Json序列化器
 *
 * @author jay.wu
 */
public class HutoolJsonSerializer implements Serializer {

    private final static Serializer instance = new HutoolJsonSerializer();

    public static Serializer instance() {
        return instance;
    }

    @Override
    public String serialize(Object value) {
        return JSONUtil.toJsonStr(value);
    }

    @Override
    public <T> T deserialize(String serialization, Class<T> type) {
        return JSONUtil.toBean(serialization, type);
    }

    @Override
    public <T> T deserialize(String serialization, Type type) {
        return JSONUtil.toBean(serialization, type, false);
    }
}