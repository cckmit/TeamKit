package org.team4u.command.infrastructure.util;

import lombok.Data;

/**
 * 提前配置
 *
 * @author jay.wu
 */
@Data
public class ExtractConfig {
    /**
     * 提取模板
     */
    private String template;
    /**
     * 目标类型
     */
    private String targetType;
}
