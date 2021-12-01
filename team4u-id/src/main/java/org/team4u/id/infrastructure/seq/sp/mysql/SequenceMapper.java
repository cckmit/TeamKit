package org.team4u.id.infrastructure.seq.sp.mysql;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * 序号Mapper
 *
 * @author jay.wu
 */
@Repository
public interface SequenceMapper extends BaseMapper<Sequence> {
}