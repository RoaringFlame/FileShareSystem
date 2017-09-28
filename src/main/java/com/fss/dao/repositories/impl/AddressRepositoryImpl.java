package com.fss.dao.repositories.impl;


import com.fss.dao.repositories.custom.AddressRepositoryCustom;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
public class AddressRepositoryImpl implements AddressRepositoryCustom{
    @PersistenceContext
    private EntityManager em;

    /**    不使用JPA，自定义查询例子：  */

}
