package org.team4u.exporter.infrastructure.exporter;

import cn.hutool.core.util.StrUtil;
import org.team4u.exporter.domain.Title;

import java.io.OutputStream;
import java.util.Collection;
import java.util.List;

/**
 * 基于行数据的上下文
 *
 * @author jay.wu
 */
public class RowContext {
    /**
     * 第一行是否输出标题
     */
    private boolean writeTitle = true;
    /**
     * 标题定义集合
     */
    private List<Title> titles;
    /**
     * 行数据集合
     */
    private Collection<?> rows;
    /**
     * 导出完成后是否关闭输出流
     */
    private boolean isCloseOut = true;
    /**
     * 输出流
     */
    private OutputStream outputStream;

    public boolean isWriteTitle() {
        return writeTitle;
    }

    public RowContext setWriteTitle(boolean writeTitle) {
        this.writeTitle = writeTitle;
        return this;
    }

    public boolean anyMatchTitleKey(String key) {
        return titles.stream().anyMatch(it -> StrUtil.contains(key, it.getKey()));
    }

    public List<Title> getTitles() {
        return titles;
    }

    public RowContext setTitles(List<Title> titles) {
        this.titles = titles;
        return this;
    }

    public Collection<?> getRows() {
        return rows;
    }

    public RowContext setRows(Collection<?> rows) {
        this.rows = rows;
        return this;
    }

    public boolean isCloseOut() {
        return isCloseOut;
    }

    public RowContext setCloseOut(boolean closeOut) {
        isCloseOut = closeOut;
        return this;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public RowContext setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
        return this;
    }
}
