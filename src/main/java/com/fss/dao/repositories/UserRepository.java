package com.fss.dao.repositories;

import com.fss.dao.domain.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends BaseRepository<User>{

    /**
     * 用户姓名获取用户信息
     * SpringSecurity模块UserDetailsService接口使用
     * @param username              账号
     * @return                  用户对象
     */
    User findByUsername(String username);
}
