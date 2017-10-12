package com.fss.dao.repositories;

import com.fss.controller.vo.UserSearchKeys;
import com.fss.dao.domain.User;
import com.fss.dao.repositories.custom.UserRepositoryCustom;
import com.fss.util.PageConfig;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends BaseRepository<User>,UserRepositoryCustom {
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
}
