package org.team4u.sql.infrastructure.config;

import cn.hutool.core.io.FileUtil;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 基于文件的sql配置
 *
 * @author jay.wu
 */
public class SimpleFileSqlConfigService extends AbstractFileSqlConfigService {

    public SimpleFileSqlConfigService(Config config) {
        super(config);
    }

    @Override
    protected List<InputStream> loadResources(String path) {
        return FileUtil.loopFiles(path)
                .stream()
                .map(FileUtil::getInputStream)
                .collect(Collectors.toList());
    }
}