package org.team4u.config.infrastructure.persistence;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.team4u.config.domain.SimpleConfig;
import org.team4u.config.domain.SimpleConfigId;
import org.team4u.config.domain.SimpleConfigRepository;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MybatisSimpleConfigRepository implements SimpleConfigRepository {

    private final SystemConfigMapper mapper;

    @Autowired
    public MybatisSimpleConfigRepository(SystemConfigMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public List<SimpleConfig> allConfigs() {
        List<SystemConfig> systemConfigs = mapper.selectList(new QueryWrapper<>());
        return toSimpleConfigs(systemConfigs);
    }

    private List<SimpleConfig> toSimpleConfigs(List<SystemConfig> systemConfigs) {
        return systemConfigs.stream()
                .map(this::toSimpleConfig)
                .collect(Collectors.toList());
    }

    private SimpleConfig toSimpleConfig(SystemConfig systemConfig) {
        SimpleConfig simpleConfig = new SimpleConfig(
                new SimpleConfigId(systemConfig.getConfigType(), systemConfig.getConfigKey()),
                systemConfig.getConfigValue(),
                systemConfig.getDescription(),
                systemConfig.getSequenceNo(),
                systemConfig.getEnabled(),
                systemConfig.getCreatedBy(),
                systemConfig.getCreateTime()
        );

        BeanUtil.copyProperties(systemConfig, simpleConfig);

        return simpleConfig;
    }
}