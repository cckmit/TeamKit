package org.team4u.workflow.infrastructure.persistence.instance;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

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
     * @param assignee 处理人
     * @return 待审批流程实例集合
     */
    @Select("select pi.current_node_id,\n" +
            "       pi.current_node_name,\n" +
            "       pi.process_definition_id,\n" +
            "       pi.process_definition_name,\n" +
            "from process_instance pi,\n" +
            "     process_assignee pa,\n" +
            "where pi.process_instance_id = pa.process_instance_id\n" +
            "  and pi.current_node_id = pa.node_id\n" +
            "  and pa.action_id = ''\n" +
            "  and pa.assignee = #{assignee}")
    IPage<ProcessInstanceDo> instancesOfPending(Page<ProcessInstanceDo> page, String assignee);

    /**
     * 获取历史审批流程实例集合
     *
     * @param assignee 处理人
     * @return 历史审批流程实例集合
     */
    @Select("select pi.current_node_id,\n" +
            "       pi.current_node_name,\n" +
            "       pi.process_definition_id,\n" +
            "       pi.process_definition_name,\n" +
            "from process_instance pi,\n" +
            "     process_assignee pa,\n" +
            "where pi.process_instance_id = pa.process_instance_id\n" +
            "  and pi.current_node_id = pa.node_id\n" +
            "  and pa.action_id != ''\n" +
            "  and pa.assignee = #{assignee}")
    IPage<ProcessInstanceDo> instancesOfHistory(Page<ProcessInstanceDo> page, String assignee);
}