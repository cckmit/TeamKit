package org.team4u.kv.infrastructure.repository.db;

import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Component;

/**
 * 键值表名处理器
 * <p>
 * 用于动态处理分表名称
 *
 * @author jay.wu
 */
@Component
public class KeyValueTableNameHandler implements TableIdHandler {


    @Override
    public String tableName() {
        return "key_value";
    }

    @Override
    public String dynamicTableName(String sql, String tableName) {
        if (!StrUtil.equals(TableNameContext.getOriginalTableName(), tableName)) {
            return tableName;
        }

        return TableNameContext.getTableName().replaceAll(":", "_");
    }
}