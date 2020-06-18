package org.team4u.ddd.infrastructure.serializer;

import cn.hutool.core.lang.TypeReference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.team4u.ddd.serializer.Serializer;

public class FastJsonSerializer implements Serializer {

    private final static Serializer instance = new FastJsonSerializer();

    private SerializerFeature[] features;

    public FastJsonSerializer(SerializerFeature... features) {
        this.features = features;
    }

    public static Serializer instance() {
        return instance;
    }

    @Override
    public String serialize(Object value) {
        return JSON.toJSONString(value, features);
    }

    @Override
    public <T> T deserialize(String serialization, Class<T> type) {
        return JSON.parseObject(serialization, type);
    }

    @Override
    public <T> T deserialize(String serialization, TypeReference<T> typeReference) {
        return JSON.parseObject(serialization, typeReference.getType());
    }
}