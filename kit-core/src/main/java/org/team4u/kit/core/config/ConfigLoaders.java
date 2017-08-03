package org.team4u.kit.core.config;

import org.team4u.kit.core.lang.Registry;

public class ConfigLoaders extends Registry<String, ConfigLoader<?>> {

    public ConfigLoaders() {
        register(new JsonConfigLoader());
        register(new PropertiesLoader());
        register(new MapLoader());
    }
}