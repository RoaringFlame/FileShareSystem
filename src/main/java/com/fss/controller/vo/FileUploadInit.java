package com.fss.controller.vo;

import com.fss.util.Selector;

import java.util.List;

public class FileUploadInit {
    private List<Selector> userSelector;
    private List<Selector> catalogSelector;
    private List<Selector> canLoadSelector;
    private List<Selector> canReviseSelector;
    private boolean canCover;

    public List<Selector> getUserSelector() {
        return userSelector;
    }

    public void setUserSelector(List<Selector> userSelector) {
        this.userSelector = userSelector;
    }

    public List<Selector> getCatalogSelector() {
        return catalogSelector;
    }

    public void setCatalogSelector(List<Selector> catalogSelector) {
        this.catalogSelector = catalogSelector;
    }

    public List<Selector> getCanLoadSelector() {
        return canLoadSelector;
    }

    public void setCanLoadSelector(List<Selector> canLoadSelector) {
        this.canLoadSelector = canLoadSelector;
    }

    public List<Selector> getCanReviseSelector() {
        return canReviseSelector;
    }

    public void setCanReviseSelector(List<Selector> canReviseSelector) {
        this.canReviseSelector = canReviseSelector;
    }

    public boolean isCanCover() {
        return canCover;
    }

    public void setCanCover(boolean canCover) {
        this.canCover = canCover;
    }
}
