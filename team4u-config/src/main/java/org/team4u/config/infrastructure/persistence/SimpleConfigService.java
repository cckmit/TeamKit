package org.team4u.config.infrastructure.persistence;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.StrUtil;
import org.team4u.base.config.ConfigService;
import org.team4u.config.application.SimpleConfigAppService;

import java.util.List;
import java.util.Map;

/**
 * 基于SimpleConfig的ConfigService
 *
 * @author jay.wu
 */
public class SimpleConfigService implements ConfigService {

    private final SimpleConfigAppService simpleConfigAppService;

    public SimpleConfigService(SimpleConfigAppService simpleConfigAppService) {
        this.simpleConfigAppService = simpleConfigAppService;
    }

    @Override
    public String get(String key) {
        Pair<String, String> typeAndKey = getConfigType(key);
        return simpleConfigAppService.value(typeAndKey.getKey(), typeAndKey.getValue());
    }

    @Override
    public <T> T get(String key, T defaultValue) {
        Pair<String, String> typeAndKey = getConfigType(key);
        return simpleConfigAppService.to(
                typeAndKey.getKey(), typeAndKey.getValue(), defaultValue.getClass()
        );
    }

    @Override
    public Map<String, Object> allConfigs() {
        return simpleConfigAppService.allConfigs().toMap();
    }

    protected Pair<String, String> getConfigType(String key) {
        List<String> typeAndKey = StrUtil.split(key, '.');
        return new Pair<>(CollUtil.getFirst(typeAndKey), CollUtil.getLast(typeAndKey));
    }
}