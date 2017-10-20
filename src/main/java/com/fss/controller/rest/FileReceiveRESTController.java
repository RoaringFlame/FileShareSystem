package com.fss.controller.rest;

import com.fss.controller.vo.FileInfoVO;
import com.fss.controller.vo.FileSearchKeys;
import com.fss.controller.vo.PageVO;
import com.fss.controller.vo.UserInfo;
import com.fss.service.FileReceiveService;
import com.fss.service.UserService;
import com.fss.util.PageConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/file")
public class FileReceiveRESTController {

    @Autowired
    private UserService userService;

    @Autowired
    private FileReceiveService fileReceiveService;

    /**
     * 待接收文件页面信息
     *
     * @param departmentKey 根据部门搜索
     * @param fileNameKey   根据文件名搜索
     * @param pageNum       页码
     * @param pageSize      页面大小
     * @return 文件列表
     */
    @RequestMapping(value = "/pageNeedReceive", method = RequestMethod.GET)
    public PageVO<FileInfoVO> getPageNeedReceive(
            @RequestParam(required = false, defaultValue = "") String departmentKey,
            @RequestParam(required = false, defaultValue = "") String catalogNameKey,
            @RequestParam(required = false, defaultValue = "") String nameKey,
            @RequestParam(required = false, defaultValue = "") String fileNameKey,
            @RequestParam(required = false, defaultValue = "1") int pageNum,
            @RequestParam(required = false, defaultValue = "8") int pageSize) {
        UserInfo userinfo = userService.getNowUserInfo();
        if (userinfo != null) {
            FileSearchKeys fileSearchKeys = new FileSearchKeys();
            fileSearchKeys.setDepartmentKey(departmentKey);
            fileSearchKeys.setCatalogKey(catalogNameKey);
            fileSearchKeys.setNameKey(nameKey);
            fileSearchKeys.setFileNameKey(fileNameKey);
            fileSearchKeys.setUserId(userinfo.getUserId());
            PageConfig pageConfig = new PageConfig();
            pageConfig.setPageNum(pageNum);
            pageConfig.setPageSize(pageSize);
            return fileReceiveService.getPageFileNeedReceive(fileSearchKeys, pageConfig);
        } else
            return null;
    }

    /**
     * 已接收文件页面信息
     *
     * @param departmentKey 根据部门搜索
     * @param fileNameKey   根据文件名搜索
     * @param pageNum       页码
     * @param pageSize      页面大小
     * @return 文件列表
     */
    @RequestMapping(value = "/pageReceived", method = RequestMethod.GET)
    public PageVO<FileInfoVO> getPageReceived(
            @RequestParam(required = false, defaultValue = "") String departmentKey,
            @RequestParam(required = false, defaultValue = "") String catalogNameKey,
            @RequestParam(required = false, defaultValue = "") String nameKey,
            @RequestParam(required = false, defaultValue = "") String fileNameKey,
            @RequestParam(required = false, defaultValue = "1") int pageNum,
            @RequestParam(required = false, defaultValue = "8") int pageSize) {
        UserInfo userinfo = userService.getNowUserInfo();
        if (userinfo != null) {
            FileSearchKeys fileSearchKeys = new FileSearchKeys();
            fileSearchKeys.setDepartmentKey(departmentKey);
            fileSearchKeys.setCatalogKey(catalogNameKey);
            fileSearchKeys.setNameKey(nameKey);
            fileSearchKeys.setFileNameKey(fileNameKey);
            fileSearchKeys.setUserId(userinfo.getUserId());
            PageConfig pageConfig = new PageConfig();
            pageConfig.setPageNum(pageNum);
            pageConfig.setPageSize(pageSize);
            return fileReceiveService.getPageFileReceived(fileSearchKeys, pageConfig);
        } else
            return null;
    }
}
