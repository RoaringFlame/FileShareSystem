package com.fss.controller.vo;

public class FileSearchKeys {
    private String userId;
    private int searchFlag;    //1.提示2.未接收3.已接收4.已上传
    private String departmentKey = "";
    private String catalogKey = "";
    private String nameKey = "";
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

    public String getDepartmentKey() {
        return departmentKey;
    }

    public void setDepartmentKey(String departmentKey) {
        this.departmentKey = departmentKey;
    }

    public String getCatalogKey() {
        return catalogKey;
    }

    public void setCatalogKey(String catalogKey) {
        this.catalogKey = catalogKey;
    }

    public String getNameKey() {
        return nameKey;
    }

    public void setNameKey(String nameKey) {
        this.nameKey = nameKey;
    }

    public String getFileNameKey() {
        return fileNameKey;
    }

    public void setFileNameKey(String fileNameKey) {
        this.fileNameKey = fileNameKey;
    }
}
