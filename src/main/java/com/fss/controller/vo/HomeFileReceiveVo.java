package com.fss.controller.vo;

import java.util.List;

public class HomeFileReceiveVo {
    private List<FileInfoVo> fileInfoVoList;
    private int count;

    public List<FileInfoVo> getFileInfoVoList() {
        return fileInfoVoList;
    }

    public void setFileInfoVoList(List<FileInfoVo> fileInfoVoList) {
        this.fileInfoVoList = fileInfoVoList;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
