package com.fss.service;

import com.fss.controller.vo.FileHistoryVo;
import com.fss.controller.vo.HistorySearchKeys;
import com.fss.dao.domain.FileReceive;
import com.fss.dao.domain.FileVersion;
import com.fss.util.PageVo;

public interface IOperateService {

    PageVo<FileHistoryVo> getPageFileDownloadHistory(String userId, HistorySearchKeys historySearchKeys,
            PageConfig pageConfig);

    PageVo<FileHistoryVo> getPageFileReviseHistory(String userId, HistorySearchKeys historySearchKeys,
            PageConfig pageConfig);

    PageVo<FileHistoryVo> getPageVersionDownloadHistory(String userId, HistorySearchKeys historySearchKeys,
            PageConfig pageConfig);

    void downloadFile(FileReceive fileReceive);

    void selfDownloadFile(String userId, FileVersion fileVersion);
}
