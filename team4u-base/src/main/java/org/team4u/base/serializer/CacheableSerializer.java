package org.team4u.base.serializer;

import cn.hutool.core.lang.Pair;
import org.team4u.base.lang.lazy.LazyFunction;

import java.lang.reflect.Type;

/**
 * 简单可缓存的序列化器
 * <p>
 * * @author jay.wu
 */
public class CacheableSerializer implements Serializer {

    private final LazyFunction<Pair<Class<?>, String>, Object> classDeserializer;
    private final LazyFunction<Pair<Type, String>, Object> typeDeserializer;
    private final LazyFunction<Object, String> objectSerializer;

    public CacheableSerializer(Serializer serializer) {
        String name = getClass().getSimpleName();

        this.classDeserializer = LazyFunction.of(
                LazyFunction.Config.builder().name(name + "|classDeserializer").build(),
                parameter -> serializer.deserialize(parameter.getValue(), parameter.getKey())
        );

        this.typeDeserializer = LazyFunction.of(
                LazyFunction.Config.builder().name(name + "|typeDeserializer").build(),
                parameter -> serializer.deserialize(parameter.getValue(), parameter.getKey())
        );

        this.objectSerializer = LazyFunction.of(
                LazyFunction.Config.builder().name(name + "|objectSerializer").build(),
                serializer::serialize
        );
    }

    @Override
    public String serialize(Object value) {
        return objectSerializer.apply(value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T deserialize(String serialization, Class<T> type) {
        return (T) classDeserializer.apply(new Pair<>(type, serialization));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T deserialize(String serialization, Type type) {
        return (T) typeDeserializer.apply(new Pair<>(type, serialization));
    }
}