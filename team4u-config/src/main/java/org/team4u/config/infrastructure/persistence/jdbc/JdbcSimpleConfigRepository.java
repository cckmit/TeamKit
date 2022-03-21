package org.team4u.config.infrastructure.persistence.jdbc;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import lombok.Getter;
import org.team4u.base.error.NestedException;
import org.team4u.config.domain.SimpleConfig;
import org.team4u.config.domain.SimpleConfigId;
import org.team4u.config.domain.SimpleConfigRepository;
import org.team4u.config.domain.SimpleConfigs;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 基于JDBC的配置项资源库
 *
 * @author jay.wu
 */
public class JdbcSimpleConfigRepository implements SimpleConfigRepository {

    @Getter
    private final Db db;

    public JdbcSimpleConfigRepository(DataSource dataSource) {
        this.db = Db.use(dataSource);
    }

    @Override
    public SimpleConfigs allConfigs() {
        try {
            List<SystemConfigDo> systemConfigDos = db.findAll(
                    Entity.create("system_config"),
                    SystemConfigDo.class
            );
            return toSimpleConfigs(systemConfigDos);
        } catch (SQLException e) {
            throw new NestedException(e);
        }
    }

    private SimpleConfigs toSimpleConfigs(List<SystemConfigDo> systemConfigDos) {
        return new SimpleConfigs(
                systemConfigDos.stream()
                        .map(this::toSimpleConfig)
                        .collect(Collectors.toList())
        );
    }

    private SimpleConfig toSimpleConfig(SystemConfigDo systemConfigDo) {
        SimpleConfig simpleConfig = new SimpleConfig(
                new SimpleConfigId(systemConfigDo.getConfigType(), systemConfigDo.getConfigKey()),
                systemConfigDo.getConfigValue(),
                systemConfigDo.getDescription(),
                systemConfigDo.getSequenceNo(),
                systemConfigDo.getEnabled()
        );

        BeanUtil.copyProperties(systemConfigDo, simpleConfig);

        return simpleConfig;
    }
}