package com.fss.dao.repositories.impl;

import com.fss.dao.repositories.custom.UserRepositoryCustom;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
public class UserRepositoryImpl implements UserRepositoryCustom{
    @PersistenceContext
    private EntityManager em;

    /**    不使用JPA，自定义查询例子：  */

}
