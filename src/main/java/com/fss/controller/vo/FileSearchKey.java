package com.fss.controller.vo;

public class FileSearchKey {
    private String userId;
    private int searchFlag;
    private String departmentId = "";
    private String catalogId = "";
    private String fileNameKey = "";

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getSearchFlag() {
        return searchFlag;
    }

    public void setSearchFlag(int searchFlag) {
        this.searchFlag = searchFlag;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(String catalogId) {
        this.catalogId = catalogId;
    }

    public String getFileNameKey() {
        return fileNameKey;
    }

    public void setFileNameKey(String fileNameKey) {
        this.fileNameKey = fileNameKey;
    }
}
