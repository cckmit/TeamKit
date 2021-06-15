package org.team4u.exporter.domain;

/**
 * 导出器接口
 *
 * @author jay.wu
 */
public interface Exporter<T> {

    /**
     * 执行导出
     *
     * @param context 导出上下文
     */
    void export(T context) throws Exception;
}