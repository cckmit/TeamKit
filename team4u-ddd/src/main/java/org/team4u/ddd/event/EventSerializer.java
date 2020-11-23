package org.team4u.ddd.event;

import cn.hutool.core.lang.TypeReference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.team4u.ddd.serializer.Serializer;

import static com.alibaba.fastjson.parser.Feature.SupportAutoType;

public class EventSerializer implements Serializer {

    public static final EventSerializer INSTANCE = new EventSerializer();


    public static EventSerializer instance() {
        return INSTANCE;
    }

    @Override
    public String serialize(Object value) {
        return JSON.toJSONString(value, SerializerFeature.WriteClassName);
    }

    @Override
    public <T> T deserialize(String serialization, Class<T> type) {
        return JSON.parseObject(serialization, type, SupportAutoType);
    }

    @Override
    public <T> T deserialize(String serialization, TypeReference<T> typeReference) {
        return JSON.parseObject(serialization, typeReference.getType(), SupportAutoType);
    }
}