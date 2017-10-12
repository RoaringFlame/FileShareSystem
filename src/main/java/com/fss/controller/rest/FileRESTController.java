package com.fss.controller.rest;

import com.fss.controller.vo.FileUploadInit;
import com.fss.controller.vo.FileUploadParam;
import com.fss.controller.vo.JsonResultVO;
import com.fss.controller.vo.UserInfo;
import com.fss.service.FileService;
import com.fss.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
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
}
