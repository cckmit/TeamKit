package org.team4u.base.config;

import cn.hutool.core.io.FileUtil;
import cn.hutool.log.Log;

/**
 * 本地Json文件配置服务
 *
 * @author jay.wu
 */
public class LocalJsonConfigService extends ResourceJsonConfigService {

    private final Log log = Log.get();

    public LocalJsonConfigService() {
        this(null);
    }

    public LocalJsonConfigService(String path) {
        super(path);
    }

    @Override
    public String get(String key) {
        try {
            return FileUtil.readUtf8String(getPath(key));
        } catch (Exception e) {
            log.warn(e.getMessage());
            return null;
        }
    }
}