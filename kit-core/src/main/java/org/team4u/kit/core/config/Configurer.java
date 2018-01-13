package org.team4u.kit.core.config;

import cn.hutool.core.io.FileUtil;
import org.team4u.kit.core.lang.Registry;

import java.io.File;

public class Configurer extends Registry<String, Configurable> {

    private static Configurer configurer = new Configurer();
    private ConfigLoaders loaders = new ConfigLoaders();

    private Configurer() {
    }

    public static Configurer getInstance() {
        return configurer;
    }

    @SuppressWarnings("unchecked")
    public <T extends Configurable, V> T load(Class<T> configClass, String loaderKey, V content) {
        ConfigLoader loader = loaders.get(loaderKey);
        T config = (T) loader.load(configClass, content);
        register(config);
        return config;
    }

    public <T extends Configurable> T loadWithFilePath(Class<T> configClass, String path) {
        return loadWithFile(configClass, FileUtil.file(path));
    }

    public <T extends Configurable> T loadWithFile(Class<T> configClass, File file) {
        return loadWithContent(configClass, FileUtil.extName(file.getName()), FileUtil.readUtf8String(file));
    }

    public <T extends Configurable, V> T loadWithContent(Class<T> configClass,
                                                         String loaderType,
                                                         V content) {
        return load(configClass, loaderType, content);
    }

    @SuppressWarnings("unchecked")
    public <T extends Configurable> T get(Class<T> configClass) {
        return (T) get(configClass.getName());
    }

    public <T extends Configurable> T getOrLoadWithFilePath(Class<T> configClass, String path) {
        T config = get(configClass);

        if (config != null) {
            return config;
        }

        return getOrLoadWithFile(configClass, FileUtil.file(path));
    }

    public <T extends Configurable> T getOrLoadWithFile(Class<T> configClass, File file) {
        T config = get(configClass);

        if (config != null) {
            return config;
        }

        return loadWithContent(configClass, FileUtil.extName(file.getName()), FileUtil.readUtf8String(file));
    }

    public Configurer registerLoader(ConfigLoader loader) {
        loaders.register(loader);
        return this;
    }

    public enum LoaderType {

        JSON("js"), PROPERTIES("properties"), MAP("map");

        private String key;

        LoaderType(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }
}