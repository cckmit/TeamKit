package org.team4u.ddd.infrastructure.persistence.mybatis;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeConstrainedProcessTrackerMapper extends BaseMapper<TimeConstrainedProcessTrackerEntity> {
}