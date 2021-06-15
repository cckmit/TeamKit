package org.team4u.exporter.infrastructure.exporter;

import org.team4u.exporter.domain.Title;

import java.io.OutputStream;
import java.util.Collection;
import java.util.List;

public class RowContext {
    private boolean writeTitle = true;
    private List<Title> titles;
    private Collection<?> rows;
    private boolean isCloseOut = true;
    private OutputStream outputStream;

    public boolean isWriteTitle() {
        return writeTitle;
    }

    public RowContext setWriteTitle(boolean writeTitle) {
        this.writeTitle = writeTitle;
        return this;
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
