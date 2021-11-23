package org.team4u.base.lang.lazy;

import cn.hutool.core.convert.Convert;
import cn.hutool.log.Log;

/**
 * 懒加载值日志格式化器
 *
 * @author jay.wu
 */
public class LazyValueFormatter {

    /**
     * 格式化
     *
     * @param log   日志
     * @param value 值
     * @return 供日志打印的值
     */
    public String format(Log log, Object value) {
        if (value == null) {
            return null;
        }

        if (log.isDebugEnabled()) {
            return Convert.toStr(value);
        }

        return value.getClass().getSimpleName();
    }
}