package com.fss.controller.vo;

public class HistorySearchKeys {
    private String fileId;
    private String versionId;
    private int flag; //1.文件下载历史 2.文件修改历史 3.版本文件下载历史

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
