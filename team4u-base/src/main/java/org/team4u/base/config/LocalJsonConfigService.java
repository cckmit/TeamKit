package org.team4u.base.config;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.log.Log;
import org.team4u.base.util.PathUtil;

/**
 * 本地Json文件配置服务
 *
 * @author jay.wu
 */
public class LocalJsonConfigService implements ConfigService {

    private final Log log = Log.get();

    @Override
    public String get(String key) {
        try {
            return FileUtil.readUtf8String(PathUtil.getPath(key + ".json"));
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