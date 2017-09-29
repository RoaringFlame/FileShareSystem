package com.fss.dao.repositories;

import com.fss.dao.domain.Catalog;

public interface CatalogRepository extends BaseRepository<Catalog>{

    Catalog findByName(String name);
}
