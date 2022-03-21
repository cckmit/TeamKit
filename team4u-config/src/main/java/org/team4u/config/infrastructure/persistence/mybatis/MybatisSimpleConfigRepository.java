package org.team4u.config.infrastructure.persistence.mybatis;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.team4u.config.domain.SimpleConfig;
import org.team4u.config.domain.SimpleConfigId;
import org.team4u.config.domain.SimpleConfigRepository;
import org.team4u.config.domain.SimpleConfigs;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 基于Mybatis的配置项资源库
 *
 * @author jay.wu
 */
@Component
public class MybatisSimpleConfigRepository implements SimpleConfigRepository {

    private final SystemConfigMapper mapper;

    @Autowired
    public MybatisSimpleConfigRepository(SystemConfigMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public SimpleConfigs allConfigs() {
        List<SystemConfigDo> systemConfigDos = mapper.selectList(new QueryWrapper<>());
        return toSimpleConfigs(systemConfigDos);
    }

    private SimpleConfigs toSimpleConfigs(List<SystemConfigDo> systemConfigDos) {
        return new SimpleConfigs(systemConfigDos.stream()
                .map(this::toSimpleConfig)
                .collect(Collectors.toList()));
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