package com.fss.dao.repositories.impl;

import com.fss.dao.repositories.custom.FileRepositoryCustom;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
public class FileRepositoryImpl implements FileRepositoryCustom{
    @PersistenceContext
    private EntityManager em;

}
