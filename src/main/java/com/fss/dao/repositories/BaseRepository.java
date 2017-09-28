package com.fss.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 数据基类
 * @author joe
 * Created by joe on 2015/12/1.
 */
@NoRepositoryBean
public interface BaseRepository<T> extends JpaRepository<T,Long>,JpaSpecificationExecutor<T> {}
