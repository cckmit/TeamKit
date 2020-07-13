package org.team4u.base.masker.dynamic;

import cn.hutool.core.io.resource.NoResourceException;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.team4u.base.masker.Masker;
import org.team4u.base.masker.Maskers;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 基于本地文件的动态掩码器配置资源库
 *
 * @author jay.wu
 */
public class LocalDynamicMaskerConfigRepository implements DynamicMaskerConfigRepository {

    private String configPath;

    public LocalDynamicMaskerConfigRepository() {
        this("masker/dynamicMaskerConfig.json");
    }

    public LocalDynamicMaskerConfigRepository(String configPath) {
        this.configPath = configPath;
    }

    @Override
    public List<DynamicMaskerConfig> allConfigs() {
        return toPropDynamicMaskerConfigs(configPayload())
                .stream()
                .map(this::toDynamicMaskerConfig)
                .collect(Collectors.toList());
    }

    protected String configPayload() {
        try {
            return ResourceUtil.readUtf8Str(configPath);
        } catch (NoResourceException e) {
            return null;
        }
    }

    private List<PropDynamicMaskerConfig> toPropDynamicMaskerConfigs(String config) {
        if (StrUtil.isEmpty(config)) {
            return Collections.emptyList();
        }

        return JSON.parseObject(config,
                new TypeReference<List<PropDynamicMaskerConfig>>() {
                });
    }

    private DynamicMaskerConfig toDynamicMaskerConfig(PropDynamicMaskerConfig config) {
        Map<Masker, List<String>> me = new HashMap<>();
        for (Map.Entry<String, List<String>> maskerExpressions : config.getMaskerExpressions().entrySet()) {
            me.put(Maskers.instance().maskerOf(maskerExpressions.getKey()),
                    maskerExpressions.getValue());
        }

        return new DynamicMaskerConfig()
                .setConfigId(config.getConfigId())
                .setMaskerExpressions(me);
    }

    public String getConfigPath() {
        return configPath;
    }

    public static class PropDynamicMaskerConfig {
        private String configId;
        private Map<String, List<String>> maskerExpressions;

        public String getConfigId() {
            return configId;
        }

        public PropDynamicMaskerConfig setConfigId(String configId) {
            this.configId = configId;
            return this;
        }

        public Map<String, List<String>> getMaskerExpressions() {
            return maskerExpressions;
        }

        public PropDynamicMaskerConfig setMaskerExpressions(Map<String, List<String>> maskerExpressions) {
            this.maskerExpressions = maskerExpressions;
            return this;
        }
    }
}