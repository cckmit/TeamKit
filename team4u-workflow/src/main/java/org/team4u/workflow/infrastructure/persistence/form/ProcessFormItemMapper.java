package org.team4u.workflow.infrastructure.persistence.form;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * 流程表单内容数据库映射器
 *
 * @author jay.wu
 */
@Repository
public interface ProcessFormItemMapper extends BaseMapper<ProcessFormItemDo> {
}