package org.team4u.base.serializer;

import cn.hutool.cache.Cache;
import cn.hutool.core.lang.Pair;
import org.team4u.base.lang.CacheableFunc1;

import java.lang.reflect.Type;

/**
 * 简单可缓存的json序列化器
 * <p>
 * * @author jay.wu
 */
public class CacheableJsonSerializer implements Serializer {

    private final Serializer serializer;

    private final ClassDeserializer classDeserializer;
    private final TypeDeserializer typeDeserializer;
    private final ObjectSerializer objectSerializer;

    @SuppressWarnings({"rawtypes", "unchecked"})
    public CacheableJsonSerializer(Serializer serializer, Cache cache) {
        this.serializer = serializer;

        this.classDeserializer = new ClassDeserializer(cache);
        this.typeDeserializer = new TypeDeserializer(cache);
        this.objectSerializer = new ObjectSerializer(cache);
    }

    @Override
    public String serialize(Object value) {
        return objectSerializer.callWithCacheAndRuntimeException(value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T deserialize(String serialization, Class<T> type) {
        return (T) classDeserializer.callWithCacheAndRuntimeException(new Pair<>(type, serialization));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T deserialize(String serialization, Type type) {
        return (T) typeDeserializer.callWithCacheAndRuntimeException(new Pair<>(type, serialization));
    }

    private class ObjectSerializer extends CacheableFunc1<Object, String> {

        public ObjectSerializer(Cache<Object, String> cache) {
            super(cache);
        }

        @Override
        public String call(Object parameter) throws Exception {
            return serializer.serialize(parameter);
        }
    }

    private class ClassDeserializer extends CacheableFunc1<Pair<Class<?>, String>, Object> {

        public ClassDeserializer(Cache<Pair<Class<?>, String>, Object> cache) {
            super(cache);
        }

        @Override
        public Object call(Pair<Class<?>, String> parameter) throws Exception {
            return serializer.deserialize(parameter.getValue(), parameter.getKey());
        }
    }

    private class TypeDeserializer extends CacheableFunc1<Pair<Type, String>, Object> {

        public TypeDeserializer(Cache<Pair<Type, String>, Object> cache) {
            super(cache);
        }

        @Override
        public Object call(Pair<Type, String> parameter) throws Exception {
            return serializer.deserialize(parameter.getValue(), parameter.getKey());
        }
    }
}