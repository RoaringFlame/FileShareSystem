package com.fss.controller.rest;

import com.fss.controller.vo.*;
import com.fss.service.IFileService;
import com.fss.service.IUserService;
import com.fss.util.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
@RequestMapping("/file")
public class FileRESTController {

    @Autowired
    private IFileService fileService;

    @Autowired
    private IUserService userService;

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
    public JsonResultVo updateUserPicture(
            @RequestParam MultipartFile file, @RequestParam String catalogId, @RequestParam String canLoadUserIds,
            @RequestParam String canReviseUserIds,
            @RequestParam(required = false, defaultValue = "false") boolean canCover,
            @RequestParam(required = false, defaultValue = "false") boolean isMailTo, HttpServletRequest request) {
        UserInfo userInfo = userService.getNowUserInfo();
        if (userInfo != null) {
            //去除多余的逗号并转化为数组
            List<String> canLoadIds = new ArrayList<>();
            if (StringUtils.isNotEmpty(canLoadUserIds)) {
                canLoadUserIds = canLoadUserIds.substring(0, canLoadUserIds.length() - 1);
                canLoadIds = Arrays.asList(canLoadUserIds.split(","));
            }
            List<String> canReviseIds = new ArrayList<>();
            if (StringUtils.isNotEmpty(canReviseUserIds)) {
                canReviseUserIds = canReviseUserIds.substring(0, canReviseUserIds.length() - 1);
                canReviseIds = Arrays.asList(canReviseUserIds.split(","));
            }

            //            canLoadIds.removeAll(canReviseIds);//去除重复（不需要，选框不同）
            FileUploadParam fileUploadParam = new FileUploadParam();
            fileUploadParam.setCatalogId(catalogId);
            fileUploadParam.setCanOnlyLoadUserIds(canLoadIds);
            fileUploadParam.setCanReviseUserIds(canReviseIds);
            fileUploadParam.setCanCover(canCover);
            fileUploadParam.setMailTo(isMailTo);
            fileUploadParam.setUserInfo(userInfo);
            return this.fileService.upload(userInfo.getName(),file, fileUploadParam, request);
        }
        return new JsonResultVo(JsonResultVo.FAILURE, "请重新登录");
    }

    @RequestMapping(value = "/initRevise/{fileId}", method = RequestMethod.GET)
    public FileUploadInit initRevise(@PathVariable String fileId) {
        UserInfo userInfo = userService.getNowUserInfo();
        return fileService.getReviseSelector(userInfo.getUserId(), fileId);
    }

    @RequestMapping(value = "/reviseRole/{fileId}", method = RequestMethod.POST)
    public JsonResultVo reviseRole(@PathVariable String fileId,
                                   @RequestParam String canLoadUserIds,
                                   @RequestParam String canReviseUserIds) {
        UserInfo userInfo = userService.getNowUserInfo();
        if (userInfo != null) {
            List<String> canLoadIds = new ArrayList<>();
            if (StringUtils.isNotEmpty(canLoadUserIds)) {
                canLoadUserIds = canLoadUserIds.substring(0, canLoadUserIds.length() - 1);
                canLoadIds = Arrays.asList(canLoadUserIds.split(","));
            }
            List<String> canReviseIds = new ArrayList<>();
            if (StringUtils.isNotEmpty(canReviseUserIds)) {
                canReviseUserIds = canReviseUserIds.substring(0, canReviseUserIds.length() - 1);
                canReviseIds = Arrays.asList(canReviseUserIds.split(","));
            }
            Set<String> canLoadIdSet = new HashSet<>();
            canLoadIdSet.addAll(canLoadIds);
            Set<String> canReviseSet = new HashSet<>();
            canReviseSet.addAll(canReviseIds);
            FileUploadParam fileUploadParam = new FileUploadParam();
            return fileService.reviseRole(userInfo.getUserId(), fileId,canLoadIdSet,canReviseSet);
        } else return new JsonResultVo(JsonResultVo.FAILURE, "请重新登录");
    }

    @RequestMapping(value = "/reviseFile/{fileVersionId}", method = RequestMethod.POST)
    public JsonResultVo reviseFile(
            @RequestParam MultipartFile file, @PathVariable String fileVersionId,
            @RequestParam(required = false, defaultValue = "false") boolean canCover, HttpServletRequest request) {
        UserInfo userInfo = userService.getNowUserInfo();
        if (userInfo != null) {
            return fileService.reviseFile(userInfo.getUserId(), file, fileVersionId, canCover, request);
        }
        return new JsonResultVo(JsonResultVo.FAILURE, "请重新登录");
    }

    @RequestMapping(value = "/download/{versionId}", method = RequestMethod.GET)
    public JsonResultVo downloadFile(
            @PathVariable String versionId, HttpServletRequest request, HttpServletResponse response) {
        UserInfo userInfo = userService.getNowUserInfo();
        if (userInfo != null) {
            return fileService.download(userInfo.getUserId(), versionId, request, response);
        } else
            return new JsonResultVo(JsonResultVo.FAILURE, "请重新登录");
    }

    @RequestMapping(value = "/delete/{fileId}", method = RequestMethod.POST)
    public JsonResultVo deleteFile(@PathVariable String fileId) {
        UserInfo userInfo = userService.getNowUserInfo();
        if (userInfo != null) {
            return fileService.deleteFile(fileId, userInfo.getUserId());
        } else
            return new JsonResultVo(JsonResultVo.FAILURE, "请重新登录");
    }

    /**
     * 首页待接收文件
     *
     * @return 文件列表
     */
    @RequestMapping(value = "/home/needReceive", method = RequestMethod.GET)
    public HomeFileReceiveVo getNeedReceive() {
        UserInfo userInfo = userService.getNowUserInfo();
        if (userInfo != null) {
            return fileService.getHomeFileNeedReceive(userInfo.getUserId());
        } else
            return null;
    }

    /**
     * 首页已接收文件
     *
     * @return 文件列表
     */
    @RequestMapping(value = "/home/received", method = RequestMethod.GET)
    public List<FileInfoVo> getReceived() {
        UserInfo userInfo = userService.getNowUserInfo();
        if (userInfo != null) {
            return fileService.getHomeFileReceived(userInfo.getUserId());
        } else
            return null;
    }
    
    /**
     * 首页已上传文件
     *
     * @return 文件列表
     */
    @RequestMapping(value = "/home/uploaded", method = RequestMethod.GET)
    private List<FileInfoVo> getUploaded() {
        UserInfo userinfo = userService.getNowUserInfo();
        if (userinfo != null) {
            return fileService.getHomeFileUploaded(userinfo.getUserId());
        } else
            return null;
    }

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
    private PageVo<FileInfoVo> getPageNeedReceive(
            @RequestParam(required = false, defaultValue = "") String departmentKey,
            @RequestParam(required = false, defaultValue = "") String catalogNameKey,
            @RequestParam(required = false, defaultValue = "") String nameKey,
            @RequestParam(required = false, defaultValue = "") String fileNameKey,
            @RequestParam(required = false, defaultValue = "1") int pageNum,
            @RequestParam(required = false, defaultValue = "8") int pageSize) {
        UserInfo userinfo = userService.getNowUserInfo();
        if (userinfo != null) {
            FileSearchKeys fileSearchKeys = new FileSearchKeys();
            fileSearchKeys.setDepartmentId(departmentKey);
            fileSearchKeys.setCatalogKey(catalogNameKey);
            fileSearchKeys.setNameKey(nameKey);
            fileSearchKeys.setFileNameKey(fileNameKey);
            fileSearchKeys.setUserId(userinfo.getUserId());
            PageConfig pageConfig = new PageConfig();
            pageConfig.setPageNum(pageNum);
            pageConfig.setPageSize(pageSize);
            return fileService.getPageFileNeedReceive(fileSearchKeys, pageConfig);
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
    private PageVo<FileInfoVo> getPageReceived(
            @RequestParam(required = false, defaultValue = "") String departmentKey,
            @RequestParam(required = false, defaultValue = "") String catalogNameKey,
            @RequestParam(required = false, defaultValue = "") String nameKey,
            @RequestParam(required = false, defaultValue = "") String fileNameKey,
            @RequestParam(required = false, defaultValue = "1") int pageNum,
            @RequestParam(required = false, defaultValue = "8") int pageSize) {
        UserInfo userinfo = userService.getNowUserInfo();
        if (userinfo != null) {
            FileSearchKeys fileSearchKeys = new FileSearchKeys();
            fileSearchKeys.setDepartmentId(departmentKey);
            fileSearchKeys.setCatalogKey(catalogNameKey);
            fileSearchKeys.setNameKey(nameKey);
            fileSearchKeys.setFileNameKey(fileNameKey);
            fileSearchKeys.setUserId(userinfo.getUserId());
            PageConfig pageConfig = new PageConfig();
            pageConfig.setPageNum(pageNum);
            pageConfig.setPageSize(pageSize);
            return fileService.getPageFileReceived(fileSearchKeys, pageConfig);
        } else
            return null;
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
    private PageVo<FileInfoVo> getPageUploaded(
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
}
