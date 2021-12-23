package org.team4u.id.infrastructure.seq.value.jdbc;

import lombok.Data;

import java.util.Date;

/**
 * 序号
 *
 * @author jay.wu
 */
@Data
public class Sequence {
    /**
     * 自增长标识
     */
    private Long id;
    /**
     * 类型标识
     */
    private String configId;
    /**
     * 分组标识
     */
    private String groupKey;
    /**
     * 当前序号值
     */
    private Long currentValue;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 版本号
     */
    private Long versionNumber;
}