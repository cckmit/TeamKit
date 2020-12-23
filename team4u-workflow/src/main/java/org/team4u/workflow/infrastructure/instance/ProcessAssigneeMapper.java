package org.team4u.workflow.infrastructure.instance;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * 流程处理人数据库映射器
 *
 * @author jay.wu
 */
@Repository
public interface ProcessAssigneeMapper extends BaseMapper<ProcessAssigneeDo> {
}