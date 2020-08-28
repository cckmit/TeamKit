package org.team4u.config.domain;

import java.lang.reflect.Type;

public interface SimpleConfigConverter {

    <T> T to(Class<T> toType, String configType);

    <T> T to(Class<T> toType, String configType, String configKey);

    <T> T to(Type toType, String configType, String configKey);

    String to(String configType, String configKey);
}
