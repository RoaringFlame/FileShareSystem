package com.fss.controller.rest;

import com.fss.controller.vo.FileHistoryVo;
import com.fss.controller.vo.HistorySearchKeys;
import com.fss.controller.vo.UserInfo;
import com.fss.service.IOperateService;
import com.fss.service.IUserService;
import com.fss.util.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/history")
public class HistoryRESTController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IOperateService operateService;

    @RequestMapping(value = "/fileDownload/{fileId}", method = RequestMethod.GET) PageVo<FileHistoryVo> getFileDownloadHist(@PathVariable String fileId,
                                              @RequestParam(required = false, defaultValue = "1") int pageNum,
                                              @RequestParam(required = false, defaultValue = "5") int pageSize) {
        UserInfo userInfo = userService.getNowUserInfo();
        if (userInfo != null) {
            String userId = userInfo.getUserId();
            HistorySearchKeys historySearchKeys = new HistorySearchKeys();
            historySearchKeys.setFileId(fileId);
            historySearchKeys.setFlag(1);
            PageConfig pageConfig = new PageConfig();
            pageConfig.setPageNum(pageNum);
            pageConfig.setPageSize(pageSize);
            return operateService.getPageFileDownloadHistory(userId,historySearchKeys, pageConfig);
        } else return null;
    }

    @RequestMapping(value = "/fileRevise/{fileId}", method = RequestMethod.GET)
    PageVo<FileHistoryVo> getFileReviseHist(@PathVariable String fileId,
                                            @RequestParam(required = false, defaultValue = "1") int pageNum,
                                            @RequestParam(required = false, defaultValue = "5") int pageSize) {
        UserInfo userInfo = userService.getNowUserInfo();
        if (userInfo != null) {
            String userId = userInfo.getUserId();
            HistorySearchKeys historySearchKeys = new HistorySearchKeys();
            historySearchKeys.setFileId(fileId);
            historySearchKeys.setFlag(2);
            PageConfig pageConfig = new PageConfig();
            pageConfig.setPageNum(pageNum);
            pageConfig.setPageSize(pageSize);
            return operateService.getPageFileReviseHistory(userId,historySearchKeys, pageConfig);
        } else return null;
    }

    @RequestMapping(value = "/versionDownload/{versionId}", method = RequestMethod.GET)
    PageVo<FileHistoryVo> getVersionDownloadHist(@PathVariable String versionId,
                                                 @RequestParam(required = false, defaultValue = "1") int pageNum,
                                                 @RequestParam(required = false, defaultValue = "5") int pageSize) {
        UserInfo userInfo = userService.getNowUserInfo();
        if (userInfo != null) {
            String userId = userInfo.getUserId();
            HistorySearchKeys historySearchKeys = new HistorySearchKeys();
            historySearchKeys.setVersionId(versionId);
            historySearchKeys.setFlag(3);
            PageConfig pageConfig = new PageConfig();
            pageConfig.setPageNum(pageNum);
            pageConfig.setPageSize(pageSize);
            return operateService.getPageVersionDownloadHistory(userId,historySearchKeys, pageConfig);
        } else return null;
    }

}
