package com.fss.service.impl;

import com.eshore.fss.enums.Department;
import com.eshore.fss.enums.Gender;
import com.eshore.fss.enums.UserRole;
import com.eshore.fss.sysmgr.dao.UserDao;
import com.eshore.fss.sysmgr.pojo.User;
import com.eshore.fss.sysmgr.service.IUserService;
import com.eshore.fss.vo.*;
import com.eshore.khala.common.model.PageConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

//import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User findUserBy(String id) {
        return userDao.findOne(id);
    }

    @Override
    public UserInfo getNowUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated()) {
            try {
                return (UserInfo) authentication.getPrincipal();
            } catch (Exception e) {
                return null;
            }
        } else
            return null;
    }

    /**
     * 获得当前用户，比上一个方法多一次数据库查询
     */
    @Override
    public User getNowUser() {
        UserInfo userInfo = this.getNowUserInfo();
        if (userInfo != null) {
            return userDao.findOne(userInfo.getUserId());
        } else
            return null;
    }

    @Override
    public JsonResultVo addOrUpdate(UserVo userVo) {
    	User user = new User();
    	User oUser = userDao.findByUsername(userVo.getUsername());
        if (userVo.getId() != null) {		//修改用户信息
            user = userDao.get(userVo.getId());
            user.setUsername(userVo.getUsername());
            user.setName(userVo.getName());
            user.setEmail(userVo.getEmail());
            user.setDepartment(userVo.getDepartmentById());
            user.setGender(userVo.getGenderById());
            user.setRole(userVo.getRoleById());
        }
        else {		//添加用户
        	if (oUser == null) {		//用户名不存在
                Md5PasswordEncoder md5 = new Md5PasswordEncoder();
                user.setPassword(md5.encodePassword("123456", ""));
                user.setUsable(true);
            }
        	else if(!oUser.getUsable()){		//用户名已存在
        		oUser.setName(userVo.getName());
        		oUser.setEmail(userVo.getEmail());
        		oUser.setDepartment(userVo.getDepartmentById());
        		oUser.setGender(userVo.getGenderById());
        		oUser.setRole(userVo.getRoleById());
        		oUser.setUsable(true);
        		userDao.update(oUser);
        		return new JsonResultVo(JsonResultVo.SUCCESS, "操作成功！");
        	}
        	else {
        		return new JsonResultVo(JsonResultVo.FAILURE, "帐号已存在！");
			}
            user.setUsername(userVo.getUsername());
            user.setName(userVo.getName());
            user.setEmail(userVo.getEmail());
            user.setDepartment(userVo.getDepartmentById());
            user.setGender(userVo.getGenderById());
            user.setRole(userVo.getRoleById());
        }
        boolean flag = userDao.saveOrUpdate(user);
        if (flag)
            return new JsonResultVo(JsonResultVo.SUCCESS, "操作成功！");
        else
        	return new JsonResultVo(JsonResultVo.FAILURE, "操作失败！");
}

    public JsonResultVo deleteUserById(String userId) {
        boolean flag = userDao.deleteUser(userId);
        if (flag) {
            return new JsonResultVo(JsonResultVo.SUCCESS, "删除成功！");
        }
        return new JsonResultVo(JsonResultVo.FAILURE, "删除失败！");
    }

    @Override
    public UserVo showUserInfoById(String userId) {
        User user = userDao.findOne(userId);
        if (user != null) {
            return UserVo.generateBy(user);
        }
        return null;
    }

    @Override
    public PageVo<UserInfoVo> getUserInfoList(UserSearchKeys userSearchKeys, PageConfig pageConfig) {

        List<User> userPage = userDao.queryUserPage(userSearchKeys, pageConfig);
        UserRole searchRole = userSearchKeys.getSearchRole();
        List<UserInfoVo> userInfoVos = UserInfoVo.generateBy(userPage, searchRole);
        PageVo<UserInfoVo> userInfoPageVo = new PageVo<>();
        userInfoPageVo.setDataList(userInfoVos);
        userInfoPageVo.loadConfig(pageConfig);

        return userInfoPageVo;
    }

    @Override
    public SelectOptionVo getSelectOption(User user) {
        SelectOptionVo selectOptionVo = new SelectOptionVo();
        selectOptionVo.setGenderList(Gender.toList());
        selectOptionVo.setDepartmentList(Department.toList());
        selectOptionVo.setRoleList(UserRole.toList(user));
        return selectOptionVo;
    }

    @Override
    public SelectOptionVo getSelectOption() {
        SelectOptionVo selectOptionVo = new SelectOptionVo();
        selectOptionVo.setGenderList(Gender.toList());
        selectOptionVo.setDepartmentList(Department.toList());
        selectOptionVo.setRoleList(UserRole.toList());
        return selectOptionVo;
    }

    @Override
    public JsonResultVo changePwd(String userId, String oldPwd, String newPwd) {
        try {
            User user = this.findUserBy(userId);
            if (!oldPwd.equals(user.getPassword())) {
                return new JsonResultVo(JsonResultVo.FAILURE, "原密码输入错误！");
            }
            user.setPassword(newPwd);
            boolean flag = userDao.saveOrUpdate(user);
            if (flag) {
                return new JsonResultVo(JsonResultVo.SUCCESS, "密码修改成功！");
            } else return new JsonResultVo(JsonResultVo.FAILURE, "修改失败！");
        } catch (Exception e) {
            return new JsonResultVo(JsonResultVo.FAILURE, "未知错误！");
        }
    }

    @Override
    public JsonResultVo resetPwd(String userId) {
        User user = this.findUserBy(userId);
        if (user != null) {
            Md5PasswordEncoder md5 = new Md5PasswordEncoder();
            user.setPassword(md5.encodePassword("123456", ""));
            boolean flag = userDao.saveOrUpdate(user);
            if (flag) {
                return new JsonResultVo(JsonResultVo.SUCCESS, "重置密码成功！");
            } else return new JsonResultVo(JsonResultVo.FAILURE, "重置失败！");
        } else
            return new JsonResultVo(JsonResultVo.FAILURE, "账号不存在！");
    }

    @Override
    @Transactional
    public List<User> getByUserIdList(List<String> sendIds) {
        List<User> users = new ArrayList<>();
        for(String id:sendIds){
            User user = userDao.get(id);
            users.add(user);
        }
        return users;
    }
}
