package org.team4u.kit.core.config;

import org.team4u.kit.core.error.ExceptionUtil;
import org.team4u.kit.core.util.MapExUtil;

import java.io.StringReader;
import java.util.Properties;

public class PropertiesLoader implements ConfigLoader<String> {

    @Override
    public <T> T load(Class<T> configClass, String content) {
        Properties properties = new Properties();
        try {
            properties.load(new StringReader(content));
            return MapExUtil.toObject(MapExUtil.toPathMap(properties), configClass);
        } catch (Exception e) {
            throw ExceptionUtil.toRuntimeException(e);
        }
    }

    @Override
    public String getKey() {
        return Configurer.LoaderType.PROPERTIES.getKey();
    }
}