package org.team4u.base.serializer.json;

import cn.hutool.core.lang.Singleton;
import org.team4u.base.registrar.PolicyRegistrar;

import java.lang.reflect.Type;

/**
 * JSON序列化器服务
 *
 * @author jay.wu
 */
public class JsonSerializers extends PolicyRegistrar<String, JsonSerializer> {

    public static JsonSerializers getInstance() {
        return Singleton.get(JsonSerializers.class);
    }

    public String serialize(Object value) {
        return serializer().serialize(value);
    }

    public <T> T deserialize(String serialization, Class<T> type) {
        return serializer().deserialize(serialization, type);
    }

    public <T> T deserialize(String serialization, Type type) {
        return serializer().deserialize(serialization, type);
    }

    public JsonSerializer serializer() {
        return firstAvailablePolicy();
    }
}