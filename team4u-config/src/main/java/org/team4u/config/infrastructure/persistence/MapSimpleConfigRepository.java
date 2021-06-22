package org.team4u.config.infrastructure.persistence;

import cn.hutool.core.util.StrUtil;
import org.team4u.config.domain.SimpleConfig;
import org.team4u.config.domain.SimpleConfigId;
import org.team4u.config.domain.SimpleConfigRepository;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MapSimpleConfigRepository implements SimpleConfigRepository {

    private final Map<?, ?> source;

    public MapSimpleConfigRepository(Map<?, ?> source) {
        this.source = source;
    }

    @Override
    public List<SimpleConfig> allConfigs() {
        return getSource()
                .entrySet()
                .stream()
                .map(it -> {
                    String[] typeAndKey = StrUtil.splitToArray(it.getKey().toString(), ".");
                    String type;
                    String key;
                    if (typeAndKey.length == 1) {
                        type = "";
                        key = typeAndKey[0];
                    } else {
                        type = typeAndKey[0];
                        key = typeAndKey[1];
                    }

                    return new SimpleConfig(
                            new SimpleConfigId(type, key),
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

    public Map<?, ?> getSource() {
        return source;
    }
}