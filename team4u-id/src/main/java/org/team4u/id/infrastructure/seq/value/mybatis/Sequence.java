package org.team4u.id.infrastructure.seq.value.mybatis;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;

import java.util.Date;

/**
 * 序号
 *
 * @author jay.wu
 */
@Data
@TableName
public class Sequence {
    /**
     * 自增长标识
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 类型标识
     */
    private String typeId;
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
    @Version
    private Long versionNumber;
}