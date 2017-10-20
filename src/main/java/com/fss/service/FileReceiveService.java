package com.fss.service;

import com.fss.controller.vo.FileInfoVO;
import com.fss.controller.vo.FileSearchKeys;
import com.fss.controller.vo.PageVO;
import com.fss.util.PageConfig;

public interface FileReceiveService {

    PageVO<FileInfoVO> getPageFileNeedReceive(FileSearchKeys fileSearchKeys, PageConfig pageConfig);

    PageVO<FileInfoVO> getPageFileReceived(FileSearchKeys fileSearchKeys, PageConfig pageConfig);
}
