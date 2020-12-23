package org.team4u.workflow.infrastructure.persistence.instance;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * 流程实例明细数据库映射器
 *
 * @author jay.wu
 */
@Repository
public interface ProcessInstanceDetailMapper extends BaseMapper<ProcessInstanceDetailDo> {
}