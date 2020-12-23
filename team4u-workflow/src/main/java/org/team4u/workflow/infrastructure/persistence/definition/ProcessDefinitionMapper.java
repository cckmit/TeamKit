package org.team4u.workflow.infrastructure.persistence.definition;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * 流程定义数据库映射器
 *
 * @author jay.wu
 */
@Repository
public interface ProcessDefinitionMapper extends BaseMapper<ProcessDefinitionDo> {
}