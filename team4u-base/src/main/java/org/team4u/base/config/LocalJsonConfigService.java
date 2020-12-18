package org.team4u.base.config;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;

/**
 * 本地Json文件配置服务
 *
 * @author jay.wu
 */
public class LocalJsonConfigService implements ConfigService {

    @Override
    public String get(String key) {
        return FileUtil.readUtf8String(key + ".json");
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(String key, T defaultValue) {
        T value = Convert.convert((Class<T>) defaultValue.getClass(), get(key));
        return ObjectUtil.defaultIfNull(value, defaultValue);
    }
}