package org.team4u.workflow.infrastructure.persistence.instance;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Repository;
import org.team4u.sql.infrastructure.mybatis.JetFileSqlProvider;
import org.team4u.workflow.application.command.InstancesQuery;

/**
 * 流程实例数据库映射器
 *
 * @author jay.wu
 */
@Repository
public interface ProcessInstanceMapper extends BaseMapper<ProcessInstanceDo> {
    /**
     * 获取待审批流程实例集合
     *
     * @param query 查询条件
     * @return 待审批流程实例集合
     */
    @SelectProvider(type = JetFileSqlProvider.class, method = "sql")
    IPage<ProcessInstanceDo> instancesOfPending(@Param("page") Page<ProcessInstanceDo> page,
                                                @Param("query") InstancesQuery query);

    /**
     * 获取历史审批流程实例集合
     *
     * @param query 查询条件
     * @return 历史审批流程实例集合
     */
    @SelectProvider(type = JetFileSqlProvider.class, method = "sql")
    IPage<ProcessInstanceDo> instancesOfReviewed(@Param("page") Page<ProcessInstanceDo> page,
                                                 @Param("query") InstancesQuery query);

    /**
     * 获取所有流程实例集合
     *
     * @param query 查询条件
     * @return 历史审批流程实例集合
     */
    @SelectProvider(type = JetFileSqlProvider.class, method = "sql")
    IPage<ProcessInstanceDo> allInstances(@Param("page") Page<ProcessInstanceDo> page,
                                          @Param("query") InstancesQuery query);
}