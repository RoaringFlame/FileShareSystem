package com.fss.controller.vo;

import com.fss.util.PageConfig;

import java.io.Serializable;
import java.util.List;

public class PageVO<E> implements Serializable {

    private List<E> dataList;//所有数据

    private Integer dataCount; //当前页面数量

    private Integer pageCount;//总页数

    private Integer pageNum; //当前页

    private Boolean hasNextPage;

    private Integer nextPage;

    private Boolean hasPrePage;

    private Integer prePage;

    public PageVO() {
    }

    public PageVO(List<E> list, PageConfig pageConfig) {
        int allCount = pageConfig.getRowCount();
        int pageSize = pageConfig.getPageSize();
        int totalPageNum = (allCount + pageSize - 1) / pageSize;
        pageConfig.setPageCount(totalPageNum);
        this.setDataList(list);
        loadConfig(pageConfig);
    }

    public void loadConfig(PageConfig config) {
        this.setPageCount(config.getPageCount());
        this.setPageNum(config.getPageNum());
        this.setDataCount(config.getRowCount());
        if (config.getPageNum() > 1) {
            this.setHasPrePage(true);
            this.setPrePage(this.getPageNum() - 1);
        } else {
            this.setHasPrePage(false);
            this.setPrePage(this.getPageNum());
        }
        if (config.getPageNum() < config.getPageCount()) {
            this.setHasNextPage(true);
            this.setNextPage(this.getPageNum() + 1);
        } else {
            this.setHasNextPage(false);
            this.setNextPage(this.getPageNum());
        }
    }

    public List<E> getDataList() {
        return dataList;
    }

    public void setDataList(List<E> dataList) {
        this.dataList = dataList;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Boolean getHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(Boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public Integer getNextPage() {
        return nextPage;
    }

    public void setNextPage(Integer nextPage) {
        this.nextPage = nextPage;
    }

    public Boolean getHasPrePage() {
        return hasPrePage;
    }

    public void setHasPrePage(Boolean hasPrePage) {
        this.hasPrePage = hasPrePage;
    }

    public Integer getPrePage() {
        return prePage;
    }

    public void setPrePage(Integer prePage) {
        this.prePage = prePage;
    }

    public Integer getDataCount() {
        return dataCount;
    }

    public void setDataCount(Integer dataCount) {
        this.dataCount = dataCount;
    }

}
