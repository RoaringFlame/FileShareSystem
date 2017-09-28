package com.fss.dao.repositories.impl;

import com.eshore.fss.sysmgr.dao.CatalogDao;
import com.eshore.fss.sysmgr.dao.FileDao;
import com.eshore.fss.sysmgr.pojo.Catalog;
import com.eshore.fss.sysmgr.pojo.File;
import com.eshore.khala.core.data.jpa.dao.impl.JpaDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository public class CatalogDaoImpl extends JpaDaoImpl<Catalog> implements CatalogDao {

	@Autowired
    private FileDao fileDao;
	
	@Override public @Transactional boolean saveOrUpdate(Catalog catalog) {
        try {
            if (catalog.getId() == null) {
                String catalogId = UUID.randomUUID().toString();
                catalog.setId(catalogId);
                this.save(catalog);
                return true;
            }
            this.update(catalog);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override @Transactional public boolean deleteCatalog(String catalogId) {
        try {
            Catalog catalog = this.get(catalogId);
            catalog.setUsable(false);
            this.update(catalog);
            
            List<Catalog> catalogsList = getCatalogListBy(catalogId);
            for(Catalog c : catalogsList) {
            	c.setUsable(false);
                this.update(c);
            }
            
            List<File> filesList = fileDao.getFileListBy(catalogId);
            for (File f : filesList) {
				f.setUsable(false);
				fileDao.update(f);
			}
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override public Catalog findOne(String catalogId) {
        String hql = "from Catalog c where c.id = ?";
        return this.getPojo(hql, new Object[] { catalogId });
    }

    @Override public Catalog findByName(String name) {
        String hql = "from Catalog c where c.name = ?";
        return this.getPojo(hql, new Object[] { name });
    }

    @Override public List<Catalog> getAll() {
        String hql = "from Catalog c where c.usable = true";
        return this.query(hql, new Object[] {});
    }

	@Override
	public List<Catalog> getCatalogListBy(String catalogId) {
		String hql = "from Catalog c where c.parentId = ?";
		return this.query(hql, new Object[]{catalogId});
	}
}
