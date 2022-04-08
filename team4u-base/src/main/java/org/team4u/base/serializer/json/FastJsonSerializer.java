package org.team4u.base.serializer.json;

import cn.hutool.core.lang.Singleton;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Type;

/**
 * 基于fastjson的Json序列化器
 *
 * @author jay.wu
 */
public class FastJsonSerializer implements JsonSerializer {

    @Getter
    @Setter
    private SerializerFeature[] features;

    public FastJsonSerializer() {
        features = new SerializerFeature[]{};
    }

    public FastJsonSerializer(SerializerFeature... features) {
        this.features = features;
    }

    public static FastJsonSerializer getInstance() {
        return Singleton.get(FastJsonSerializer.class);
    }

    @Override
    public String serialize(Object value) {
        if (value == null) {
            return null;
        }

        return JSON.toJSONString(value, features);
    }

    @Override
    public <T> T deserialize(String serialization, Class<T> type) {
        if (StrUtil.isEmpty(serialization)) {
            return null;
        }

        return JSON.parseObject(serialization, type);
    }

    @Override
    public <T> T deserialize(String serialization, Type type) {
        if (StrUtil.isEmpty(serialization)) {
            return null;
        }

        return JSON.parseObject(serialization, type);
    }

    @Override
    public int priority() {
        return 10;
    }
}