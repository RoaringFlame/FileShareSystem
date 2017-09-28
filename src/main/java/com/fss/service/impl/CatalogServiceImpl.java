package com.fss.service.impl;


import com.fss.controller.vo.CatalogVo;
import com.fss.controller.vo.JsonResultVo;
import com.fss.dao.domain.Catalog;
import com.fss.dao.repositories.CatalogDao;
import com.fss.service.ICatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service public class CatalogServiceImpl implements ICatalogService {
    @Autowired private CatalogDao catalogDao;

    @Override
    @Transactional
    public JsonResultVo addOrUpdate(CatalogVo catalogVo) {
    	Catalog catalog = new Catalog();
        Catalog oCatalog = catalogDao.findByName(catalogVo.getName());
        if(catalogVo.getId() != null) {		//修改目录
        	catalog = catalogDao.findOne(catalogVo.getId());
        	catalog.setName(catalogVo.getName());
        	catalog.setParentId(catalogVo.getParentId());
        	catalogVo.setDescription(catalogVo.getDescription());
        }
        else {		//添加新目录
        	if (oCatalog == null) {		//目录名不存在
        		catalog.setParentId(catalogVo.getParentId());
                catalog.setDescription(catalogVo.getDescription());
                catalog.setName(catalogVo.getName());
                catalog.setUsable(true);
        	}
        	else if(!oCatalog.getUsable()) {		//目录名已存在
        		oCatalog.setParentId(catalogVo.getParentId());
        		oCatalog.setDescription(catalogVo.getDescription());
        		oCatalog.setUsable(true);
        		catalogDao.update(oCatalog);
        		return new JsonResultVo(JsonResultVo.SUCCESS, "操作成功！");
        	}
        	else {
        		return new JsonResultVo(JsonResultVo.FAILURE, "目录名重复！");
			}
        }
		boolean flag = catalogDao.saveOrUpdate(catalog);
		if (flag)
		    return new JsonResultVo(JsonResultVo.SUCCESS, "操作成功！");
		else
			return new JsonResultVo(JsonResultVo.FAILURE, "操作失败！");
    }

    @Override public JsonResultVo deleteCatalogById(String catalogId) {
        boolean flag = catalogDao.deleteCatalog(catalogId);
        if (flag) {
            return new JsonResultVo(JsonResultVo.SUCCESS, "删除成功！");
        }
        return new JsonResultVo(JsonResultVo.FAILURE, "删除失败！");
    }

    @Override public CatalogVo getCatalogInfo(String catalogId) {
        Catalog catalog = catalogDao.findOne(catalogId);
        if (catalog != null) {
            return CatalogVo.generateBy(catalog);
        }
        return null;
    }

    @Override
	public List<CatalogVo> getCatalogList() {
    	List<Catalog> catalogList = catalogDao.getAll();
    	List<CatalogVo> catalogAllList = CatalogVo.generateBy(catalogList);
		return catalogAllList;
	}
    
    @Override public Map<String, Map<String, String>> getAllCatalog() {
        List<Catalog> catalogList = catalogDao.getAll();
        Map<String, Map<String, String>> catalogMap = new HashMap<>();
        for (Catalog catalog : catalogList) {
            String id = catalog.getId();
            String parentId = catalog.getParentId();
            String name = catalog.getName();
            if (catalogMap.containsKey(parentId)) {
                Map<String, String> sonMap = catalogMap.get(parentId);
                sonMap.put(id, name);
            } else {
                catalogMap.put(id, new HashMap<String, String>());
            }
        }
        return catalogMap;
    }
}
