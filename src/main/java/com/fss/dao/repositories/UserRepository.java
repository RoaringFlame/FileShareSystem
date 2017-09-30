package com.fss.dao.repositories;

import com.fss.controller.vo.UserSearchKeys;
import com.fss.dao.domain.User;
import com.fss.util.PageConfig;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends BaseRepository<User> {
    /**
     * 账号密码获取用户信息
     * @param username          账号
     * @param password          密码
     * @return                  用户对象
     */
    User findByUsernameAndPassword(String username, String password);

    /**
     * 用户姓名获取用户信息
     * SpringSecurity模块UserDetailsService接口使用
     * @param username              账号
     * @return                  用户对象
     */
    User findByUsername(String username);

    /**
     * 根据关键字查询用户
     * @param userSearchKeys
     * @param pageConfig
     * @return
     */
    // TODO: 2017/9/29 PageVO与PageConfig的使用有待改进，尽量用JPA自动查询
    Page<User> queryUserPage(UserSearchKeys userSearchKeys, PageConfig pageConfig);
}
