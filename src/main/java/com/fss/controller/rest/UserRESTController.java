package com.fss.controller.rest;

import com.fss.controller.vo.*;
import com.fss.dao.domain.User;
import com.fss.dao.enums.UserRole;
import com.fss.service.IFileService;
import com.fss.service.IMailService;
import com.fss.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/user")
public class UserRESTController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IMailService mailService;

    @Autowired
    private IFileService fileService;

    @RequestMapping(value = "/alert", method = RequestMethod.GET)
    public UserAlertVO userAlert() {
        UserInfo userInfo = userService.getNowUserInfo();
        if (userInfo != null) {
            return fileService.getAlertById(userInfo.getUserId());
        }
        return new UserAlertVO();
    }

    @RequestMapping(value = "/changePwd", method = RequestMethod.POST)
    public JsonResultVO changePwd(
            @RequestParam String oldPwd, @RequestParam String newPwd) {
        UserInfo userInfo = userService.getNowUserInfo();
        if (userInfo != null) {
            return userService.changePwd(userInfo.getUserId(), oldPwd, newPwd);
        }
        return new JsonResultVO(JsonResultVO.FAILURE, "请重新登录！");
    }

    @RequestMapping(value = "/mailTest", method = RequestMethod.GET)
    public JsonResultVO sendAttacheMail(
            HttpServletRequest request) {
        // TODO: 2017/9/3 测试通过，需集成到项目中
        try {
            mailService.sendEmailWithAttachment(request, "13260592767@163.com", "测试邮件发送", "mail-test.txt");
        } catch (MessagingException e) {
            return new JsonResultVO(JsonResultVO.FAILURE, "发送失败", e.toString());
        }
        return new JsonResultVO(JsonResultVO.SUCCESS, "发送成功");
    }

    /**
     * 添加或修改用户信息
     *
     * @param userVO 用户数据类
     * @return json处理类
     */
    @RequestMapping(value = "/addOrUpdate", method = RequestMethod.POST)
    public JsonResultVO addOrUpdateUser(UserVO userVO) {
        User user = userService.getNowUser();
        if (user.getRole().ordinal() <= Integer.parseInt(userVO.getDepartmentId())) {
            return this.userService.addOrUpdate(userVO);
        } else
            return new JsonResultVO(JsonResultVO.FAILURE, "权限不足！");
    }

    /**
     * 重置用户密码
     *
     * @param userId 用户id
     * @return json处理类
     */
    @RequestMapping(value = "/resetPwd/{userId}", method = RequestMethod.POST)
    public JsonResultVO resetUserPwd(@PathVariable String userId) {
        User user = userService.getNowUser();
        if (user.getRole().ordinal() == 0) {
            return this.userService.resetPwd(userId);
        } else
            return new JsonResultVO(JsonResultVO.FAILURE, "权限不足！");
    }


    /**
     * 删除用户
     *
     * @param userId 用户id
     * @return json处理类
     */
    @RequestMapping(value = "/delete/{userId}", method = RequestMethod.DELETE)
    public JsonResultVO deleteUser(
            @PathVariable String userId) {
        User user = userService.getNowUser();
        User dUser = userService.findUserBy(userId);
        if (user.getRole().ordinal() <= dUser.getRole().ordinal()) {
            return this.userService.deleteUserById(userId);
        } else
            return new JsonResultVO(JsonResultVO.FAILURE, "权限不足！");
    }

    /**
     * 得到某位用户的个人信息
     *
     * @param userId 用户id
     * @return 用户信息数据类
     */
    @RequestMapping(value = "/showInfo/{userId}", method = RequestMethod.DELETE)
    public UserVO showUserInfo(@PathVariable String userId) {
        return this.userService.showUserInfoById(userId);
    }

    /**
     * 根据条件返回用户信息列表
     *
     * @return PageVO<UserInfoVO>
     */
    @RequestMapping(value = "/showInfoList", method = RequestMethod.GET)
    public PageVO<UserInfoVO> showUserList(
            @RequestParam(required = false, defaultValue = "") String departmentKey,
            @RequestParam(required = false, defaultValue = "") String roleKey,
            @RequestParam(required = false, defaultValue = "") String name,
            @RequestParam(required = false, defaultValue = "") String userName,
            @RequestParam(required = false, defaultValue = "1") int pageNum,
            @RequestParam(required = false, defaultValue = "5") int pageSize) {
        User user = userService.getNowUser();
        if (user != null) {
        	UserRole searchRole = user.getRole();
            UserSearchKeys userSearchKeys = new UserSearchKeys();
            userSearchKeys.setDepartmentKey(departmentKey);
            userSearchKeys.setRoleKey(roleKey);
            userSearchKeys.setNameKey(name);
            userSearchKeys.setUsernameKey(userName);
            userSearchKeys.setSearchRole(searchRole);

            PageConfig pageConfig = new PageConfig();
            pageConfig.setPageNum(pageNum);
            pageConfig.setPageSize(pageSize);
            return this.userService.getUserInfoList(userSearchKeys, pageConfig);
        }
        return null;
    }

    /**
     * 重载下拉框全部内容
     *
     * @return
     */
    @RequestMapping(value = "/selectAllOption", method = RequestMethod.GET)
    public SelectOptionVO selectAllOptionList() {
        return this.userService.getSelectOption();
    }
    
    /**
     * 重载下拉框部分内容
     *
     * @return
     */
    @RequestMapping(value = "/selectOption", method = RequestMethod.GET)
    public SelectOptionVO selectOptionList() {
    	User user = userService.getNowUser();
        return this.userService.getSelectOption(user);
    }
}
