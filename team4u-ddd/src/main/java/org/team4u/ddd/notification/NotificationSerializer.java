package org.team4u.ddd.notification;

import cn.hutool.core.lang.TypeReference;
import org.team4u.ddd.infrastructure.serializer.FastJsonSerializer;
import org.team4u.ddd.serializer.Serializer;

public class NotificationSerializer implements Serializer {

    public static final NotificationSerializer instance = new NotificationSerializer();


    public static NotificationSerializer instance() {
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
