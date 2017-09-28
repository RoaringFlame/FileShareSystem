package com.fss.dao.repositories;

import com.eshore.fss.sysmgr.pojo.Catalog;
import com.eshore.khala.core.data.api.dao.IBaseDao;

import java.util.List;

public interface CatalogDao extends IBaseDao<Catalog> {

    boolean saveOrUpdate(Catalog catalog);

    boolean deleteCatalog(String catalogId);

    Catalog findOne(String catalogId);

    Catalog findByName(String name);

    List<Catalog> getAll();
    
    List<Catalog> getCatalogListBy(String catalogId);
}
