package org.team4u.kv.infrastruture.repository.db;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.parsers.ITableNameHandler;
import org.apache.ibatis.reflection.MetaObject;

/**
 * 键值表名处理器
 * <p>
 * 用于动态处理分表名称
 *
 * @author jay.wu
 */
public class KeyValueTableNameHandler implements ITableNameHandler {

    @Override
    public String dynamicTableName(MetaObject metaObject, String sql, String tableName) {
        if (!StrUtil.equals(TableNameContext.getOriginalTableName(), tableName)) {
            return tableName;
        }

        return TableNameContext.getTableName().replaceAll(":", "_");
    }
}