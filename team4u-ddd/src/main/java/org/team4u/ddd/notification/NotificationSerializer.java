package org.team4u.ddd.notification;

import org.team4u.base.serializer.FastJsonSerializer;
import org.team4u.base.serializer.Serializer;

import java.lang.reflect.Type;

public class NotificationSerializer implements Serializer {

    public static final NotificationSerializer INSTANCE = new NotificationSerializer();


    public static NotificationSerializer instance() {
        return INSTANCE;
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
    public <T> T deserialize(String serialization, Type type) {
        return FastJsonSerializer.instance().deserialize(serialization, type);
    }
}
