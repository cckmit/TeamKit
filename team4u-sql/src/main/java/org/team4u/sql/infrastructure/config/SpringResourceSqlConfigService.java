package org.team4u.sql.infrastructure.config;

import cn.hutool.core.exceptions.ExceptionUtil;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 基于spring资源文件的sql配置
 *
 * @author jay.wu
 */
public class SpringResourceSqlConfigService extends AbstractFileSqlConfigService {

    public SpringResourceSqlConfigService(Config config) {
        super(config);
    }

    @Override
    protected List<InputStream> loadResources(String path) {
        try {
            return Arrays.stream(new PathMatchingResourcePatternResolver().getResources(path + "/*.sql"))
                    .map(it -> {
                        try {
                            return it.getInputStream();
                        } catch (IOException e) {
                            throw ExceptionUtil.wrapRuntime(e);
                        }
                    })
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw ExceptionUtil.wrapRuntime(e);
        }
    }
}