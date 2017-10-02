package com.fss.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 数据基类
 */
@NoRepositoryBean
public interface BaseRepository<T> extends JpaRepository<T,String>,JpaSpecificationExecutor<T> {}
