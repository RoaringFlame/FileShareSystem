package com.fss.controller.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fss.util.CustomDateSerializer;
import com.fss.util.CustomJsonDateDeserializer;

import java.util.Date;

public class FileHistoryVo {
    private String versionId;
    private Double versionNumber;
    private String name;
    private String role;
    private String department;
    private Integer count;
    private Date operateTime;

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    public Double getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(Double versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @JsonSerialize(using = CustomDateSerializer.class)
    public Date getOperateTime() {
        return operateTime;
    }

    @JsonDeserialize(using = CustomJsonDateDeserializer.class)
    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }
}
