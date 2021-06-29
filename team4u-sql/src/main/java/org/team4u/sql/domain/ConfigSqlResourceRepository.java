package org.team4u.sql.domain;

import cn.hutool.core.util.StrUtil;
import org.team4u.base.config.ConfigService;

/**
 * 基于配置的sql资源库
 *
 * @author jay.wu
 */
public class ConfigSqlResourceRepository implements SqlResourceRepository {

    private final ConfigService configService;

    public ConfigSqlResourceRepository(ConfigService configService) {
        this.configService = configService;
    }

    @Override
    public SqlResource resourceOf(String id) {
        String sqlContent = configService.get(id);

        if (StrUtil.isBlank(sqlContent)) {
            return null;
        }

        return new SqlResource(id, sqlContent);
    }
}