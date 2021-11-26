package org.team4u.base.config;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;

import java.util.Map;

/**
 * 本地Json文件配置服务
 *
 * @author jay.wu
 */
public class ResourceJsonConfigService implements ConfigService {

    private final Log log = Log.get();

    private final String dir;

    public ResourceJsonConfigService() {
        this(null);
    }

    public ResourceJsonConfigService(String dir) {
        if (StrUtil.isNotBlank(dir)) {
            this.dir = dir + "/";
        } else {
            this.dir = "";
        }
    }

    @Override
    public String get(String key) {
        try {
            return ResourceUtil.readUtf8Str(getPath(key));
        } catch (Exception e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    @Override
    public Map<String, Object> allConfigs() {
        throw new UnsupportedOperationException("allConfigs not supported");
    }

    protected String getPath(String key) {
        return getDir() + key + ".json";
    }

    public String getDir() {
        return dir;
    }
}