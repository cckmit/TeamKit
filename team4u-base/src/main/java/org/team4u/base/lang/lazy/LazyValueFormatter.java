package org.team4u.base.lang.lazy;

import cn.hutool.log.Log;

/**
 * 懒加载值日志格式化器
 *
 * @author jay.wu
 */
public interface LazyValueFormatter<V> {

    /**
     * 格式化
     *
     * @param log   日志对象，主要用于判断当前日志级别
     * @param value 值
     * @return 供日志打印的值
     */
    String format(Log log, V value);
}