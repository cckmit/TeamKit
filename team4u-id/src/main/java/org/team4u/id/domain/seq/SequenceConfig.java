package org.team4u.id.domain.seq;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.team4u.base.config.IdentifiedConfig;

/**
 * 序号配置
 *
 * @author jay.wu
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SequenceConfig extends IdentifiedConfig {
    /**
     * 分组配置标识
     */
    private String groupFactoryId;
    /**
     * 分组配置
     */
    private String groupConfig;
    /**
     * 序号配置标识
     */
    private String sequenceFactoryId;
    /**
     * 序号配置
     */
    private String sequenceConfig;
}