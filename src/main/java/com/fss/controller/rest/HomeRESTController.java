package com.fss.controller.rest;

import com.fss.controller.vo.FileInfoVO;
import com.fss.controller.vo.HomeFileReceiveVO;
import com.fss.controller.vo.UserInfo;
import com.fss.service.FileReceiveService;
import com.fss.service.FileService;
import com.fss.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/home")
public class HomeRESTController {

    @Autowired
    private UserService userService;

    @Autowired
    private FileReceiveService fileReceiveService;

    @Autowired
    private FileService fileService;

    /**
     * 首页待接收文件
     *
     * @return 文件列表
     */
    @RequestMapping(value = "/file/needReceive", method = RequestMethod.GET)
    public HomeFileReceiveVO getNeedReceive() {
        UserInfo userInfo = userService.getNowUserInfo();
        if (userInfo != null) {
            return fileReceiveService.getHomeFileNeedReceive(userInfo.getUserId());
        } else
            return null;
    }

    /**
     * 首页已接收文件
     *
     * @return 文件列表
     */
    @RequestMapping(value = "/file/received", method = RequestMethod.GET)
    public List<FileInfoVO> getReceived() {
        UserInfo userInfo = userService.getNowUserInfo();
        if (userInfo != null) {
            return fileReceiveService.getHomeFileReceived(userInfo.getUserId());
        } else
            return null;
    }

    /**
     * 首页已上传文件
     *
     * @return 文件列表
     */
    @RequestMapping(value = "/file/uploaded", method = RequestMethod.GET)
    private List<FileInfoVO> getUploaded() {
        UserInfo userinfo = userService.getNowUserInfo();
        if (userinfo != null) {
            return fileService.getHomeFileUploaded(userinfo.getUserId());
        } else
            return null;
    }
}
