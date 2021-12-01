package org.team4u.id.domain.seq;

import cn.hutool.core.date.DateUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.team4u.base.config.IdentifiedConfig;

import java.util.Date;

/**
 * 序号配置
 *
 * @author jay.wu
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SequenceConfig2 extends IdentifiedConfig {
    /**
     * 永不过期的固定日期
     */
    public static final Date NEVER_EXPIRE_DATE = DateUtil.parseDate("1979-01-01");

    /**
     * 类型标识
     */
    private String typeId;
    /**
     * 分组key配置
     */
    private String groupKeyConfig;
    /**
     * 序号值配置
     */
    private String sequenceNoConfig;
    /**
     * 过期时长（0为永不过期）
     */
    private Integer expiredSec;

    public Date expiredDate() {
        if (expiredSec == null) {
            return NEVER_EXPIRE_DATE;
        }
        return DateUtil.offsetSecond(new Date(), expiredSec);
    }
}