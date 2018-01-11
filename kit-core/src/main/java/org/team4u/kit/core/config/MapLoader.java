package org.team4u.kit.core.config;

import org.team4u.kit.core.util.MapExUtil;

import java.util.Map;

public class MapLoader implements ConfigLoader<Map<?, ?>> {

    @Override
    public <T> T load(Class<T> configClass, Map<?, ?> content) {
        return MapExUtil.toObject(MapExUtil.toPathMap(content), configClass);
    }

    @Override
    public String getKey() {
        return Configurer.LoaderType.MAP.getKey();
    }
}