package org.team4u.base.serializer;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONConfig;
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
        if (value == null) {
            return null;
        }

        return JSONUtil.toJsonStr(value);
    }

    @Override
    public <T> T deserialize(String serialization, Class<T> type) {
        if (StrUtil.isEmpty(serialization)) {
            return null;
        }

        return JSONUtil.parseObj(serialization, JSONConfig.create().setOrder(true)).toBean(type);
    }

    @Override
    public <T> T deserialize(String serialization, Type type) {
        if (StrUtil.isEmpty(serialization)) {
            return null;
        }

        JSONConfig config = JSONConfig.create().setOrder(true);
        return JSONUtil.parseObj(serialization, config).toBean(type);
    }
}