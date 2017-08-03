package org.team4u.kit.core.lang;

import java.io.Serializable;

/**
 * @author Jay Wu
 */
public class Pager implements Serializable {

    public static int DEFAULT_PAGE_SIZE = 20;

    private int pageNumber;
    private int pageSize;
    private int pageCount;
    private int recordCount;

    public Pager() {
        pageNumber = 1;
        pageSize = DEFAULT_PAGE_SIZE;
    }

    public Pager resetPageCount() {
        pageCount = -1;
        return this;
    }

    public int getPageCount() {
        if (pageCount < 0)
            pageCount = (int) Math.ceil((double) recordCount / pageSize);
        return pageCount;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public Pager setPageNumber(int pn) {
        pageNumber = pn;
        return this;
    }

    public int getPageSize() {
        return pageSize;
    }

    public Pager setPageSize(int pageSize) {
        this.pageSize = (pageSize > 0 ? pageSize : DEFAULT_PAGE_SIZE);
        return resetPageCount();
    }

    public int getRecordCount() {
        return recordCount;
    }

    public Pager setRecordCount(int recordCount) {
        this.recordCount = recordCount > 0 ? recordCount : 0;
        this.pageCount = (int) Math.ceil((double) recordCount / pageSize);
        return this;
    }

    public int getOffset() {
        return pageSize * (pageNumber - 1);
    }

    @Override
    public String toString() {
        return String.format("size: %d, total: %d, page: %d/%d",
                pageSize,
                recordCount,
                pageNumber,
                this.getPageCount());
    }

    public boolean isFirst() {
        return pageNumber == 1;
    }

    public boolean isLast() {
        return pageCount == 0 || pageNumber == pageCount;
    }

    public boolean hasNext() {
        return !isLast();
    }

    public boolean hasPrevious() {
        return !isFirst();
    }
}