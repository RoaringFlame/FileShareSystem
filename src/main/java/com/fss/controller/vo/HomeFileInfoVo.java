package com.fss.controller.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fss.util.CustomDateSerializer;
import com.fss.util.CustomJsonDateDeserializer;

import java.util.Date;
import java.util.List;

/**
 * 页面文件展示类
 */
public class HomeFileInfoVO {
    private String userId;
    private String headPicture;
    private String name;
    private String fileName;
    private Date uploadTime;
    private double VersionNumber; //最新版本号
    private String Catalog;
    private String department;
    private boolean isAlert; //是否紧急
    private List<String> operation;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHeadPicture() {
        return headPicture;
    }

    public void setHeadPicture(String headPicture) {
        this.headPicture = headPicture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileName() {
        return fileName;
    }

    @JsonDeserialize(using = CustomJsonDateDeserializer.class)
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @JsonSerialize(using = CustomDateSerializer.class)
    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public double getVersionNumber() {
        return VersionNumber;
    }

    public void setVersionNumber(double versionNumber) {
        VersionNumber = versionNumber;
    }

    public String getCatalog() {
        return Catalog;
    }

    public void setCatalog(String catalog) {
        Catalog = catalog;
    }

    public boolean isAlert() {
        return isAlert;
    }

    public void setAlert(boolean alert) {
        isAlert = alert;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public List<String> getOperation() {
        return operation;
    }

    public void setOperation(List<String> operation) {
        this.operation = operation;
    }
}
