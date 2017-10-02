package com.fss.dao.repositories;

import com.fss.dao.domain.Catalog;
import org.springframework.stereotype.Repository;

@Repository
public interface CatalogRepository extends BaseRepository<Catalog>{

    Catalog findByName(String name);
}
