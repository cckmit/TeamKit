package org.team4u.base.serializer;

import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import org.team4u.base.lang.lazy.LazyFunction;
import org.team4u.base.lang.lazy.NullValueException;

import java.lang.reflect.Type;

/**
 * 可缓存的序列化器
 * <p>
 * 最大可缓存最近使用的1000个元素
 *
 * @author jay.wu
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
        if (ObjectUtil.isNull(value)) {
            return null;
        }

        try {
            return objectSerializer.apply(value);
        } catch (NullValueException e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T deserialize(String serialization, Class<T> type) {
        if (StrUtil.isEmpty(serialization)) {
            return null;
        }

        try {
            return (T) classDeserializer.apply(new Pair<>(type, serialization));
        } catch (NullValueException e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T deserialize(String serialization, Type type) {
        if (StrUtil.isEmpty(serialization)) {
            return null;
        }

        try {
            return (T) typeDeserializer.apply(new Pair<>(type, serialization));
        } catch (NullValueException e) {
            return null;
        }
    }
}