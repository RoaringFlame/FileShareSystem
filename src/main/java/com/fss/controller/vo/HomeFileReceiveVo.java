package com.fss.controller.vo;

import java.util.List;

public class HomeFileReceiveVO {
    private List<FileInfoVO> fileInfoVOList;
    private int count;

    public List<FileInfoVO> getFileInfoVOList() {
        return fileInfoVOList;
    }

    public void setFileInfoVOList(List<FileInfoVO> fileInfoVOList) {
        this.fileInfoVOList = fileInfoVOList;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
