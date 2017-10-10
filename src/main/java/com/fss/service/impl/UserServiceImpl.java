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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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

    @Override public JsonResultVO addOrUpdate(UserVO userVO) {
        return null;
    }

    @Override public JsonResultVO deleteUserById(String userId) {
        return null;
    }

    @Override public UserVO showUserInfoById(String userId) {
        return null;
    }

    @Override public PageVO<UserInfoVO> getUserInfoList(UserSearchKeys userSearchKeys, PageConfig pageConfig) {
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
                predicates.add(cb.equal(root.get("usable").as(Boolean.class),true));
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

    @Override public JsonResultVO changePwd(String userId, String oldPwd, String newPwd) {
        return null;
    }

    @Override public JsonResultVO resetPwd(String userId) {
        return null;
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
    public List<User> getByUserIdList(List<String> sendIds) {
        List<User> users = new ArrayList<>();
        for(String id:sendIds){
            User user = userRepository.findOne(id);
            users.add(user);
        }
        return users;
    }
}
