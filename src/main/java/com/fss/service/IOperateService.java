package com.fss.service;

import com.fss.controller.vo.FileHistoryVO;
import com.fss.controller.vo.HistorySearchKeys;
import com.fss.controller.vo.PageVO;
import com.fss.dao.domain.FileReceive;
import com.fss.dao.domain.FileVersion;

public interface IOperateService {

    PageVO<FileHistoryVO> getPageFileDownloadHistory(String userId, HistorySearchKeys historySearchKeys,
            PageConfig pageConfig);

    PageVO<FileHistoryVO> getPageFileReviseHistory(String userId, HistorySearchKeys historySearchKeys,
            PageConfig pageConfig);

    PageVO<FileHistoryVO> getPageVersionDownloadHistory(String userId, HistorySearchKeys historySearchKeys,
            PageConfig pageConfig);

    void downloadFile(FileReceive fileReceive);

    void selfDownloadFile(String userId, FileVersion fileVersion);
}
