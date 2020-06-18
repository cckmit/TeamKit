package org.team4u.ddd.serializer;


import cn.hutool.core.lang.TypeReference;

public interface Serializer {

    String serialize(Object value);

    <T> T deserialize(String serialization, Class<T> type);

    <T> T deserialize(String serialization, TypeReference<T> typeReference);
}