package org.team4u.base.lang.lazy;

import cn.hutool.core.convert.Convert;
import cn.hutool.log.Log;

/**
 * 默认的懒加载值日志格式化器
 *
 * @author jay.wu
 */
public class DefaultLazyValueFormatter implements LazyValueFormatter {

    @Override
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