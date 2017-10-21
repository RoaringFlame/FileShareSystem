package com.fss.service.impl;


import com.fss.controller.vo.*;
import com.fss.dao.domain.User;
import com.fss.dao.enums.Department;
import com.fss.dao.enums.Gender;
import com.fss.dao.enums.UserRole;
import com.fss.dao.repositories.UserRepository;
import com.fss.service.UserService;
import com.fss.util.PageConfig;
import com.fss.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User findUserBy(String id) {
        return userRepository.findOne(id);
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
            return userRepository.findOne(userInfo.getUserId());
        } else
            return null;
    }

    /**
     * 增加或者修改用户
     * 注意：增加用户必须保证账号唯一
     */
    @Override public JsonResultVO addOrUpdate(UserVO userVO) {
        User user = new User();
        User oUser = userRepository.findByUsername(userVO.getUsername());
        if(userVO.getId() == null){ //增加用户
            if(oUser != null){
                return new JsonResultVO(JsonResultVO.FAILURE, "帐号已被使用！");
            }
            Md5PasswordEncoder md5 = new Md5PasswordEncoder();
            user.setPassword(md5.encodePassword("123456", ""));
            user.setUsable(true);
        }else{ //修改用户信息(用户名肯定一致且唯一)
            user = oUser;
        }
        user.setUsername(userVO.getUsername());
        user.setName(userVO.getName());
        user.setEmail(userVO.getEmail());
        user.setDepartment(userVO.getDepartmentById());
        user.setGender(userVO.getGenderById());
        user.setRole(userVO.getRoleById());
        userRepository.saveAndFlush(user);
        return new JsonResultVO(JsonResultVO.SUCCESS, "操作成功！");
    }

    @Override
    public JsonResultVO deleteUserById(String userId) {
        try {
            userRepository.delete(userId);
            return new JsonResultVO(JsonResultVO.SUCCESS, "删除成功！");
        }catch (Exception e){
            return new JsonResultVO(JsonResultVO.FAILURE, "删除失败！");
        }
    }

    @Override
    public UserVO showUserInfoById(String userId) {
        User user = userRepository.findOne(userId);
        if(user!=null){
            return UserVO.generateBy(user);
        }
        return null;
    }

    @Override
    public PageVO<UserInfoVO> getUserInfoList(UserSearchKeys userSearchKeys, PageConfig pageConfig) {
        Specification<User> specification = getWhereClause(userSearchKeys);
        Page<User> userPage = userRepository.findAll(specification,
                new PageRequest(pageConfig.getPageNum()-1,pageConfig.getPageSize(),
                        new Sort(Sort.Direction.DESC,"name")));
        List<UserInfoVO> list = UserInfoVO.generateBy(userPage.getContent(),userSearchKeys.getSearchRole());
        return (PageVO<UserInfoVO>) PageUtil.generateBy(userPage,list);
    }

    private Specification<User> getWhereClause(final UserSearchKeys userSearchKeys){
        return new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (!StringUtils.isEmpty(userSearchKeys.getDepartmentKey())) {
                    int index = Integer.valueOf(userSearchKeys.getDepartmentKey());
                    Department department = Department.values()[index];
                    predicates.add(cb.equal(root.get("department").as(Department.class),department));
                }
                if (!StringUtils.isEmpty(userSearchKeys.getRoleKey())) {
                    int index = Integer.valueOf(userSearchKeys.getRoleKey());
                    UserRole role = UserRole.values()[index];
                    predicates.add(cb.equal(root.get("role").as(UserRole.class),role));
                }
                if (!StringUtils.isEmpty(userSearchKeys.getNameKey())) {
                    predicates.add(cb.like(root.get("name").as(String.class), "%" +userSearchKeys.getNameKey()+ "%" ));
                }
                if (!StringUtils.isEmpty(userSearchKeys.getUsernameKey())) {
                    predicates.add(cb.like(root.get("username").as(String.class), "%" +userSearchKeys.getUsernameKey()+ "%" ));
                }
                Predicate[] pre = new Predicate[predicates.size()];
                return query.where(predicates.toArray(pre)).getRestriction();
            }
        };
    }

    @Override
    public SelectOptionVO getSelectOption(User user) {
        SelectOptionVO selectOptionVo = new SelectOptionVO();
        selectOptionVo.setGenderList(Gender.toList());
        selectOptionVo.setDepartmentList(Department.toList());
        selectOptionVo.setRoleList(UserRole.toList(user));
        return selectOptionVo;
    }

    @Override
    public JsonResultVO changePwd(String userId, String oldPwd, String newPwd) {
        try {
            User user = this.findUserBy(userId);
            Md5PasswordEncoder md5 = new Md5PasswordEncoder();
            oldPwd = md5.encodePassword(oldPwd, "");
            if (!oldPwd.equals(user.getPassword())) {
                return new JsonResultVO(JsonResultVO.FAILURE, "原密码输入错误！");
            }
            newPwd = md5.encodePassword(newPwd, "");
            user.setPassword(newPwd);
            userRepository.saveAndFlush(user);
            return new JsonResultVO(JsonResultVO.SUCCESS, "密码修改成功！");
        } catch (Exception e) {
        return new JsonResultVO(JsonResultVO.FAILURE, "修改失败！");
        }
    }

    @Override public JsonResultVO resetPwd(String userId) {
        User user = userRepository.findOne(userId);
        if (user != null) {
            Md5PasswordEncoder md5 = new Md5PasswordEncoder();
            user.setPassword(md5.encodePassword("123456", ""));
            try {
                userRepository.saveAndFlush(user);
                return new JsonResultVO(JsonResultVO.SUCCESS, "重置密码成功！");
            }catch (Exception e){
                return new JsonResultVO(JsonResultVO.FAILURE, "重置失败！");
            }
        } else
            return new JsonResultVO(JsonResultVO.FAILURE, "账号不存在！");
    }

    @Override
    public SelectOptionVO getSelectOption() {
        SelectOptionVO selectOptionVo = new SelectOptionVO();
        selectOptionVo.setGenderList(Gender.toList());
        selectOptionVo.setDepartmentList(Department.toList());
        selectOptionVo.setRoleList(UserRole.toList());
        return selectOptionVo;
    }


    @Override
    @Transactional
    public List<User> getByUserIdList(List<String> sendIds) {
        List<User> users = new ArrayList<>();
        for(String id:sendIds){
            User user = userRepository.findOne(id);
            users.add(user);
        }
        return users;
    }
}
