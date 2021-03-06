package com.fss.controller.rest;

import com.fss.controller.vo.*;
import com.fss.service.FileService;
import com.fss.service.UserService;
import com.fss.util.PageConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
@RequestMapping("/file")
public class FileRESTController {

    @Autowired
    private FileService fileService;

    @Autowired
    private UserService userService;

    /**
     * 初始化上传信息
     * @return
     */
    @RequestMapping(value = "uploadInit", method = RequestMethod.GET)
    public FileUploadInit getCatalogAndUserSelector() {
        UserInfo userInfo = userService.getNowUserInfo();
        return fileService.getCatalogAndUserSelector(userInfo.getUserId());
    }

    /**
     * 文件上传
     *
     * @param file             上传的文件
     * @param canLoadUserIds   可下载用户
     * @param canReviseUserIds 可修改用户
     * @param request          请求参数
     * @return json信息类
     * @throws Exception 文件上传错误信息
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public JsonResultVO uploadFile(
            @RequestParam MultipartFile file, @RequestParam String catalogId, @RequestParam String canLoadUserIds,
            @RequestParam String canReviseUserIds,
            @RequestParam(required = false, defaultValue = "false") boolean canCover,
            @RequestParam(required = false, defaultValue = "false") boolean isMailTo, HttpServletRequest request) {
        UserInfo userInfo = userService.getNowUserInfo();
        if (userInfo != null) {
            //去除多余的逗号并转化为数组
            List<String> canLoadIds = new ArrayList<>();
            if (!StringUtils.isEmpty(canLoadUserIds)) {
                canLoadUserIds = canLoadUserIds.substring(0, canLoadUserIds.length() - 1);
                canLoadIds = Arrays.asList(canLoadUserIds.split(","));
            }
            List<String> canReviseIds = new ArrayList<>();
            if (!StringUtils.isEmpty(canReviseUserIds)) {
                canReviseUserIds = canReviseUserIds.substring(0, canReviseUserIds.length() - 1);
                canReviseIds = Arrays.asList(canReviseUserIds.split(","));
            }

            FileUploadParam fileUploadParam = new FileUploadParam();
            fileUploadParam.setCatalogId(catalogId);
            fileUploadParam.setCanOnlyLoadUserIds(canLoadIds);
            fileUploadParam.setCanReviseUserIds(canReviseIds);
            fileUploadParam.setCanCover(canCover);
            fileUploadParam.setMailTo(isMailTo);
            fileUploadParam.setUserInfo(userInfo);
            return this.fileService.upload(userInfo.getName(),file, fileUploadParam, request);
        }
        return new JsonResultVO(JsonResultVO.FAILURE, "请重新登录");
    }

    /**
     * 已上传文件页面信息
     *
     * @param fileNameKey 根据文件名搜索
     * @param pageNum     页码
     * @param pageSize    页面大小
     * @return 文件列表
     */
    @RequestMapping(value = "/pageUploaded", method = RequestMethod.GET)
    private PageVO<FileInfoVO> getPageUploaded(
            @RequestParam(required = false, defaultValue = "") String catalogNameKey,
            @RequestParam(required = false, defaultValue = "") String fileNameKey,
            @RequestParam(required = false, defaultValue = "1") int pageNum,
            @RequestParam(required = false, defaultValue = "8") int pageSize) {
        UserInfo userinfo = userService.getNowUserInfo();
        if (userinfo != null) {
            FileSearchKeys fileSearchKeys = new FileSearchKeys();
            fileSearchKeys.setCatalogKey(catalogNameKey);
            fileSearchKeys.setFileNameKey(fileNameKey);
            fileSearchKeys.setUserId(userinfo.getUserId());
            PageConfig pageConfig = new PageConfig();
            pageConfig.setPageNum(pageNum);
            pageConfig.setPageSize(pageSize);
            return fileService.getPageUploaded(fileSearchKeys, pageConfig);
        } else
            return null;
    }

    /**
     * 下载文件
     * @param versionId 文件版本编号
     * @return 文件流或错误信息
     */
    @RequestMapping(value = "/download/{versionId}", method = RequestMethod.GET)
    public JsonResultVO downloadFile(
            @PathVariable String versionId, HttpServletRequest request, HttpServletResponse response) {
        UserInfo userInfo = userService.getNowUserInfo();
        if (userInfo != null) {
            return fileService.download(userInfo.getUserId(), versionId, request, response);
        } else
            return new JsonResultVO(JsonResultVO.FAILURE, "请重新登录");
    }

    /**
     * 删除文件
     * @param fileId 文件编号
     */
    @RequestMapping(value = "/delete/{fileId}", method = RequestMethod.POST)
    public JsonResultVO deleteFile(@PathVariable String fileId) {
        UserInfo userInfo = userService.getNowUserInfo();
        if (userInfo != null) {
            return fileService.deleteFile(fileId, userInfo.getUserId());
        } else
            return new JsonResultVO(JsonResultVO.FAILURE, "请重新登录");
    }

    /**
     * 初始化文件修改权限信息（加载权限）
     * @param fileId 文件编号
     * @return 权限信息
     */
    @RequestMapping(value = "/initRevise/{fileId}", method = RequestMethod.GET)
    public FileUploadInit initRevise(@PathVariable String fileId) {
        UserInfo userInfo = userService.getNowUserInfo();
        return fileService.getReviseSelector(userInfo.getUserId(), fileId);
    }

    /**
     * 修改文件权限
     * @param fileId 文件编号
     * @param canLoadUserIds 所有可下载人id
     * @param canReviseUserIds 所有可修改人
     */
    @RequestMapping(value = "/reviseRole/{fileId}", method = RequestMethod.POST)
    public JsonResultVO reviseRole(@PathVariable String fileId,
            @RequestParam String canLoadUserIds,
            @RequestParam String canReviseUserIds) {
        UserInfo userInfo = userService.getNowUserInfo();
        if (userInfo != null) {
            List<String> canLoadIds = new ArrayList<>();
            if (!StringUtils.isEmpty(canLoadUserIds)) {
                canLoadUserIds = canLoadUserIds.substring(0, canLoadUserIds.length() - 1);
                canLoadIds = Arrays.asList(canLoadUserIds.split(","));
            }
            List<String> canReviseIds = new ArrayList<>();
            if (!StringUtils.isEmpty(canReviseUserIds)) {
                canReviseUserIds = canReviseUserIds.substring(0, canReviseUserIds.length() - 1);
                canReviseIds = Arrays.asList(canReviseUserIds.split(","));
            }
            Set<String> canLoadIdSet = new HashSet<>();
            canLoadIdSet.addAll(canLoadIds);
            Set<String> canReviseSet = new HashSet<>();
            canReviseSet.addAll(canReviseIds);
            return fileService.reviseRole(userInfo.getUserId(), fileId,canLoadIdSet,canReviseSet);
        } else return new JsonResultVO(JsonResultVO.FAILURE, "请重新登录");
    }

    /**
     * 修改文件（重传新版本）
     * @param file 最新文件
     * @param fileVersionId 文件旧版本编号
     * @param canCover 是否可以覆盖
     */
    @RequestMapping(value = "/reviseFile/{fileVersionId}", method = RequestMethod.POST)
    public JsonResultVO reviseFile(
            @RequestParam MultipartFile file, @PathVariable String fileVersionId,
            @RequestParam(required = false, defaultValue = "false") boolean canCover, HttpServletRequest request) {
        UserInfo userInfo = userService.getNowUserInfo();
        if (userInfo != null) {
            return fileService.reviseFile(userInfo.getUserId(), file, fileVersionId, canCover, request);
        }
        return new JsonResultVO(JsonResultVO.FAILURE, "请重新登录");
    }
}
