package org.team4u.id.infrastructure.seq.value.mybatis;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import org.team4u.id.infrastructure.seq.value.jdbc.Sequence;

/**
 * 序号Mapper
 *
 * @author jay.wu
 */
@Repository
public interface SequenceMapper extends BaseMapper<Sequence> {
}