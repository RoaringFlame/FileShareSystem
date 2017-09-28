package com.fss.dao.repositories;

import com.fss.dao.domain.User;
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
     * @param name              用户姓名
     * @return                  用户对象
     */
    User findByName(String name);

    /**
     * 通过邮箱查用户
     * @param email             邮箱
     * @return                  用户对象
     */
    User findByEmail(String email);


}
