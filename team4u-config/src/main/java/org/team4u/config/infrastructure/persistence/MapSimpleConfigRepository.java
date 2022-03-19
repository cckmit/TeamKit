package org.team4u.config.infrastructure.persistence;

import cn.hutool.core.lang.Pair;
import org.team4u.config.domain.SimpleConfig;
import org.team4u.config.domain.SimpleConfigId;
import org.team4u.config.domain.SimpleConfigRepository;
import org.team4u.config.domain.SimpleConfigs;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * 基于Map的配置项资源库
 *
 * @author jay.wu
 */
public class MapSimpleConfigRepository implements SimpleConfigRepository {

    private final Map<?, ?> source;

    public MapSimpleConfigRepository(Map<?, ?> source) {
        this.source = source;
    }

    @Override
    public SimpleConfigs allConfigs() {
        return new SimpleConfigs(getSource().entrySet().stream()
                .map(it -> {
                    Pair<String, String> typeAndKey = typeAndKey(it.getKey().toString());

                    return new SimpleConfig(
                            new SimpleConfigId(typeAndKey.getKey(), typeAndKey.getValue()),
                            it.getValue().toString(),
                            null,
                            0,
                            true,
                            null,
                            null
                    );
                })
                .collect(Collectors.toList()));
    }

    protected Pair<String, String> typeAndKey(String originalKey) {
        int lastDotIndex = originalKey.lastIndexOf('.');
        if (lastDotIndex <= 0) {
            return new Pair<>("", originalKey);
        }

        String type = originalKey.substring(0, lastDotIndex);
        String key = originalKey.substring(lastDotIndex + 1);
        return new Pair<>(type, key);
    }

    public Map<?, ?> getSource() {
        return source;
    }
}