package org.team4u.sql.infrastructure.mybatis;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Repository;

@Repository
public interface TestMapper extends BaseMapper<StoredEventEntity> {

    @SelectProvider(type = JetFileSqlProvider.class, method = "sql")
    StoredEventEntity eventOf(@Param("id") Long id);
}
