package com.fss.controller.vo;

import java.util.List;

public class FileUploadParam {
    private String catalogId;
    private List<String> canOnlyLoadUserIds;
    private List<String> canReviseUserIds;
    private UserInfo userInfo;
    private boolean canCover;
    private boolean isMailTo;

    public String getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(String catalogId) {
        this.catalogId = catalogId;
    }

    public List<String> getCanOnlyLoadUserIds() {
        return canOnlyLoadUserIds;
    }

    public void setCanOnlyLoadUserIds(List<String> canOnlyLoadUserIds) {
        this.canOnlyLoadUserIds = canOnlyLoadUserIds;
    }

    public List<String> getCanReviseUserIds() {
        return canReviseUserIds;
    }

    public void setCanReviseUserIds(List<String> canReviseUserIds) {
        this.canReviseUserIds = canReviseUserIds;
    }

    public boolean isCanCover() {
        return canCover;
    }

    public void setCanCover(boolean canCover) {
        this.canCover = canCover;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public boolean isMailTo() {
        return isMailTo;
    }

    public void setMailTo(boolean mailTo) {
        isMailTo = mailTo;
    }
}
