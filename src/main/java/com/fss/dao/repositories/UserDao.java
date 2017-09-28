package com.fss.dao.repositories;

import com.eshore.fss.sysmgr.pojo.User;
import com.eshore.fss.vo.UserSearchKeys;
import com.eshore.khala.common.model.PageConfig;
import com.eshore.khala.core.data.api.dao.IBaseDao;

import java.util.List;

public interface UserDao extends IBaseDao<User> {
    User findByUsername(String username);

    User findOne(String id);

    boolean saveOrUpdate(User user);

    boolean deleteUser(String userId);

    List<User> queryUserPage(UserSearchKeys userSearchKeys, PageConfig pageConfig);

    List<User> getAll();
}
