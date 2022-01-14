package org.team4u.workflow.infrastructure.persistence.instance.ext;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Mybatis实例明细字段处理器
 *
 * @author jay.wu
 */
public class MybatisDetailTypeHandler implements TypeHandler<String> {

    @Override
    public void setParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
        ExtTypeHandlerHolder.getInstance().handler().setParameter(ps, i, parameter, jdbcType);
    }

    @Override
    public String getResult(ResultSet rs, String columnName) throws SQLException {
        return ExtTypeHandlerHolder.getInstance().handler().getResult(rs, columnName);
    }

    @Override
    public String getResult(ResultSet rs, int columnIndex) throws SQLException {
        return ExtTypeHandlerHolder.getInstance().handler().getResult(rs, columnIndex);
    }

    @Override
    public String getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return ExtTypeHandlerHolder.getInstance().handler().getResult(cs, columnIndex);
    }
}