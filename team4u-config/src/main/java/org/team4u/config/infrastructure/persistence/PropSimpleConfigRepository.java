package org.team4u.config.infrastructure.persistence;

import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.dialect.Props;
import org.team4u.config.domain.SimpleConfig;
import org.team4u.config.domain.SimpleConfigId;
import org.team4u.config.domain.SimpleConfigRepository;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class PropSimpleConfigRepository implements SimpleConfigRepository {

    private final String path;

    public PropSimpleConfigRepository(String path) {
        this.path = path;
    }

    @Override
    public List<SimpleConfig> allConfigs() {
        return new Props(path)
                .entrySet()
                .stream()
                .map(it -> {
                    String[] typeAndKey = StrUtil.split(it.getKey().toString(), ".");
                    return new SimpleConfig(
                            new SimpleConfigId(typeAndKey[0], typeAndKey[1]),
                            it.getValue().toString(),
                            null,
                            0,
                            true,
                            null,
                            new Date()
                    );
                })
                .collect(Collectors.toList());
    }
}