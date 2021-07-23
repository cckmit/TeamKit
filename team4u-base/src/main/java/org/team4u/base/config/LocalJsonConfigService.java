package org.team4u.base.config;

import cn.hutool.core.io.FileUtil;
import cn.hutool.log.Log;
import org.team4u.base.util.PathUtil;

/**
 * 本地Json文件配置服务
 *
 * @author jay.wu
 */
public class LocalJsonConfigService extends ResourceJsonConfigService {

    private final Log log = Log.get();

    private final String path;

    public LocalJsonConfigService() {
        this("");
    }

    public LocalJsonConfigService(String path) {
        this.path = path;
    }

    @Override
    public String get(String key) {
        try {
            return FileUtil.readUtf8String(PathUtil.runningPath(path + key + ".json"));
        } catch (Exception e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    public String getPath() {
        return path;
    }
}