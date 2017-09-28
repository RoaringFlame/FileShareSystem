package com.fss.util;

import java.io.Serializable;
import java.util.List;

public class ExecResult implements Serializable {

    private Boolean success;

    private String msg;

    private List dataList;

    public ExecResult(){}

    public ExecResult(boolean success, String msg, List dataList) {
        this.success = success;
        this.msg = msg;
        this.dataList = dataList;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List getDataList() {
        return dataList;
    }

    public void setDataList(List dataList) {
        this.dataList = dataList;
    }


}
