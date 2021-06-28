package org.team4u.workflow.infrastructure.persistence.instance;

import cn.hutool.core.util.StrUtil;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;
import org.team4u.workflow.application.command.InstancesQuery;

public class ProcessInstanceSqlProvider {

    public static String instancesOfPendingSql(@Param("query") InstancesQuery query) {
        SQL sql = new SQL() {{
            SELECT("pi.*");
            FROM("process_instance pi", "process_assignee pa");
            WHERE(
                    "pi.process_instance_id = pa.process_instance_id",
                    "pi.current_node_id = pa.node_id",
                    "pa.action_id = ''",
                    "pa.assignee = #{query.operator}"
            );
            ORDER_BY("id");
        }};

        return appendQuerySql(sql, query).toString();
    }

    public static String instancesOfHistorySql(@Param("query") InstancesQuery query) {
        SQL sql = new SQL() {{
            SELECT("pi.*");
            FROM("process_instance pi", "process_assignee pa");
            WHERE(
                    "pi.process_instance_id = pa.process_instance_id",
                    "pi.current_node_id = pa.node_id",
                    "pa.action_id != ''",
                    "pa.assignee = #{query.operator}"
            );
            ORDER_BY("id desc");
        }};

        return appendQuerySql(sql, query).toString();
    }

    public static String instancesOfApply(@Param("query") InstancesQuery query) {
        SQL sql = new SQL() {{
            SELECT("pi.*");
            FROM("process_instance pi");
            WHERE("pi.create_by = #{query.operator}");
            ORDER_BY("id desc");
        }};

        return appendQuerySql(sql, query).toString();
    }

    private static SQL appendQuerySql(SQL sql, InstancesQuery query) {
        if (StrUtil.isNotBlank(query.getProcessInstanceId())) {
            sql.getSelf().WHERE("pi.process_instance_id = #{query.processInstanceId}");
        }

        if (StrUtil.isNotBlank(query.getProcessInstanceType())) {
            sql.getSelf().WHERE("pi.process_instance_type = #{query.processInstanceType}");
        }
        if (query.getCreateStartTime() != null) {
            sql.getSelf().WHERE("pi.create_time >= #{query.createStartTime}");
        }

        if (query.getCreateEndTime() != null) {
            sql.getSelf().WHERE("pi.create_time >= #{query.createEndTime}");
        }

        if (StrUtil.isNotBlank(query.getProcessInstanceName())) {
            query.setProcessInstanceName("%" + query.getProcessInstanceName() + "%");
            sql.getSelf().WHERE("pi.process_instance_name like #{query.processInstanceName}");
        }
        return sql;
    }
}