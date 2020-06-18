package org.team4u.ddd.event;

import cn.hutool.core.lang.TypeReference;
import org.team4u.ddd.infrastructure.serializer.FastJsonSerializer;
import org.team4u.ddd.serializer.Serializer;

public class EventSerializer implements Serializer {

    public static final EventSerializer instance = new EventSerializer();


    public static EventSerializer instance() {
        return instance;
    }

    @Override
    public String serialize(Object value) {
        return FastJsonSerializer.instance().serialize(value);
    }

    @Override
    public <T> T deserialize(String serialization, Class<T> type) {
        return FastJsonSerializer.instance().deserialize(serialization, type);
    }

    @Override
    public <T> T deserialize(String serialization, TypeReference<T> typeReference) {
        return FastJsonSerializer.instance().deserialize(serialization, typeReference);
    }
}
