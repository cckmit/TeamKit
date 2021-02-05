package org.team4u.kv.infrastructure.repository.db;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * 基于mybatis的数据查询
 *
 * @author jay.wu
 */
@Repository
public interface DbKeyValueMapper extends BaseMapper<KeyValueEntity> {
}