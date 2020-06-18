package org.team4u.core.masker.dynamic;

import java.util.List;

/**
 * 动态掩码器配置资源库
 *
 * @author jay.wu
 */
public interface DynamicMaskerConfigRepository {

    /**
     * 获取所有配置
     */
    List<DynamicMaskerConfig> allConfigs();
}
