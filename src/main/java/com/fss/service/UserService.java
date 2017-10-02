package com.fss.service;

import com.fss.controller.vo.*;
import com.fss.dao.domain.User;
import com.fss.util.PageConfig;

import java.util.List;

public interface UserService {
    User findUserBy(String id);

    UserInfo getNowUserInfo();

    User getNowUser();

    JsonResultVO addOrUpdate(UserVO userVO);

    JsonResultVO deleteUserById(String userId);

    UserVO showUserInfoById(String userId);

    PageVO<UserInfoVO> getUserInfoList(UserSearchKeys userSearchKeys, PageConfig pageConfig);
    
    SelectOptionVO getSelectOption();
    
    SelectOptionVO getSelectOption(User user);

    JsonResultVO changePwd(String userId, String oldPwd, String newPwd);

    JsonResultVO resetPwd(String userId);

    List<User> getByUserIdList(List<String> sendIds);
}
