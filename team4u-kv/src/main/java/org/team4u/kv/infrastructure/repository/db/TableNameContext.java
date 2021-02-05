package org.team4u.kv.infrastructure.repository.db;

import cn.hutool.core.util.StrUtil;

/**
 * 表名上下文
 * <p>
 * 用于一次调用过程中的保存动态表名
 *
 * @author jay.wu
 */
public class TableNameContext {

    private static final ThreadLocal<String> TABLE_NAME_THREAD_LOCAL = new ThreadLocal<>();

    public static void setTableName(String originalName, String dynamicName) {
        TABLE_NAME_THREAD_LOCAL.set(originalName + ":" + dynamicName);
    }

    public static String getTableName() {
        return TABLE_NAME_THREAD_LOCAL.get();
    }

    public static String getOriginalTableName() {
        String tableName = TABLE_NAME_THREAD_LOCAL.get();
        if (StrUtil.isEmpty(tableName)) {
            return null;
        }

        return tableName.split(":")[0];
    }

    public static void clear() {
        TABLE_NAME_THREAD_LOCAL.remove();
    }
}