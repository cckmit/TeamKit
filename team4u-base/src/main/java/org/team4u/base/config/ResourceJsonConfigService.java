package org.team4u.base.config;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.log.Log;

/**
 * 本地Json文件配置服务
 *
 * @author jay.wu
 */
public class ResourceJsonConfigService implements ConfigService {

    private final Log log = Log.get();

    @Override
    public String get(String key) {
        try {
            return ResourceUtil.readUtf8Str(key + ".json");
        } catch (Exception e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(String key, T defaultValue) {
        T value = Convert.convert((Class<T>) defaultValue.getClass(), get(key));
        return ObjectUtil.defaultIfNull(value, defaultValue);
    }
}