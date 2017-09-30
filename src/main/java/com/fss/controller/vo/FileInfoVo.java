package com.fss.controller.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fss.util.CustomDateSerializer;
import com.fss.util.CustomJsonDateDeserializer;

import java.util.Date;

/**
 * 页面文件展示类
 */
public class FileInfoVO {
    private String versionId;
    private String fileId;
    private String name;
    private String fileName;
    private Date createTime;
    private Date downloadTime;
    private double versionNumber;
    private String catalog;
    private String department;
    private boolean isAlert; //是否紧急
    private boolean canRevise;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
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

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @JsonSerialize(using = CustomDateSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }

    @JsonDeserialize(using = CustomJsonDateDeserializer.class)
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @JsonSerialize(using = CustomDateSerializer.class)
    public Date getDownloadTime() {
        return downloadTime;
    }

    @JsonDeserialize(using = CustomJsonDateDeserializer.class)
    public void setDownloadTime(Date downloadTime) {
        this.downloadTime = downloadTime;
    }

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    public double getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(double versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
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

    public boolean isCanRevise() {
        return canRevise;
    }

    public void setCanRevise(boolean canRevise) {
        this.canRevise = canRevise;
    }
}
