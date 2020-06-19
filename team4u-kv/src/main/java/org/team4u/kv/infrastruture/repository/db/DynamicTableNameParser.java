package org.team4u.kv.infrastruture.repository.db;

import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.core.parser.SqlInfo;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.TableNameParser;
import com.baomidou.mybatisplus.extension.parsers.ITableNameHandler;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Collection;

public class DynamicTableNameParser implements ISqlParser {

    private ITableNameHandler tableNameHandler = new KeyValueTableNameHandler();

    @Override
    public SqlInfo parser(MetaObject metaObject, String sql) {
        Collection<String> tables = new TableNameParser(sql).tables();
        if (CollectionUtils.isNotEmpty(tables)) {
            boolean sqlParsed = false;
            String parsedSql = sql;
            for (final String table : tables) {
                parsedSql = tableNameHandler.process(metaObject, parsedSql, table);
                sqlParsed = true;
            }

            if (sqlParsed) {
                return SqlInfo.newInstance().setSql(parsedSql);
            }
        }

        return null;
    }
}