package com.fss.service;


import com.fss.controller.vo.*;
import com.fss.dao.domain.User;
import com.fss.util.PageVo;

import java.util.List;

public interface IUserService {
    public User findUserBy(String id);

    public UserInfo getNowUserInfo();

    public User getNowUser();

    public JsonResultVo addOrUpdate(UserVo userVo);

    public JsonResultVo deleteUserById(String userId);

    public UserVo showUserInfoById(String userId);

    public PageVo<UserInfoVo> getUserInfoList(UserSearchKeys userSearchKeys, PageConfig pageConfig);
    
    public SelectOptionVo getSelectOption();
    
    public SelectOptionVo getSelectOption(User user);

    public JsonResultVo changePwd(String userId, String oldPwd, String newPwd);

    public JsonResultVo resetPwd(String userId);

    List<User> getByUserIdList(List<String> sendIds);
}
