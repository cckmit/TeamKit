package org.team4u.core.masker.dynamic;

import cn.hutool.core.util.StrUtil;
import org.team4u.core.masker.Masker;

import java.util.List;
import java.util.Map;

/**
 * 动态掩码器配置
 *
 * @author jay.wu
 */
public class DynamicMaskerConfig {

    /**
     * 全局配置标识，当没有找到指定标识时使用
     */
    public static final String GLOBAL_CONFIG_ID = "*";

    /**
     * 配置标识
     */
    private String configId;

    /**
     * 掩码器/属性表达式集合映射关系
     */
    private Map<Masker, List<String>> maskerExpressions;

    public String getConfigId() {
        return configId;
    }

    public DynamicMaskerConfig setConfigId(String configId) {
        this.configId = configId;
        return this;
    }

    public Map<Masker, List<String>> getMaskerExpressions() {
        return maskerExpressions;
    }

    public DynamicMaskerConfig setMaskerExpressions(Map<Masker, List<String>> maskerExpressions) {
        this.maskerExpressions = maskerExpressions;
        return this;
    }

    /**
     * 是否全局配置
     */
    public boolean isGlobal() {
        return StrUtil.equals(configId, GLOBAL_CONFIG_ID);
    }
}