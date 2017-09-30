package com.fss.util;

import java.io.Serializable;

public class PageConfig implements Serializable {
    private int rowCount;
    private int pageSize = 10;
    private int pageNum = 1;
    private int pageCount;
    private String orderBy;

    public PageConfig() {
    }

    public PageConfig(int rowCount, int pageSize, int pageNum, int pageCount, int currRowNum, int startIndex, int endIndex, int pageCode, int nextPage, int previousPage, String orderBy) {
        this.rowCount = rowCount;
        this.pageSize = pageSize;
        this.pageNum = pageNum;
        this.pageCount = pageCount;
        this.orderBy = orderBy;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getRowCount() {
        return this.rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public int getPageNum() {
        return this.pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageCount() {
        return this.pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public String getOrderBy() {
        return this.orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String toString() {
        return "PageConfig [rowCount=" + this.rowCount + ", pageSize=" + this.pageSize + ", pageNum=" + this.pageNum + ", pageCount=" + this.pageCount + ", orderBy=" + this.orderBy + "]";
    }
}
