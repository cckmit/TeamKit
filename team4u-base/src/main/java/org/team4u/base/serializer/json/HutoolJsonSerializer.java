package org.team4u.base.serializer.json;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONUtil;
import org.team4u.base.serializer.Serializer;

import java.lang.reflect.Type;

/**
 * 基于hutool的Json序列化器
 *
 * @author jay.wu
 */
public class HutoolJsonSerializer implements JsonSerializer {

    private final static Serializer instance = new HutoolJsonSerializer();
    private final JSONConfig config = JSONConfig.create().setOrder(true);

    public static Serializer getInstance() {
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

        return JSONUtil.parse(serialization, config).toBean(type);
    }

    @Override
    public <T> T deserialize(String serialization, Type type) {
        if (StrUtil.isEmpty(serialization)) {
            return null;
        }

        return JSONUtil.parse(serialization, config).toBean(type);
    }

    @Override
    public int priority() {
        return 20;
    }
}