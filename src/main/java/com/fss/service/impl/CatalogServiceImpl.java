package com.fss.service.impl;


import com.fss.controller.vo.CatalogVO;
import com.fss.controller.vo.JsonResultVO;
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
    public JsonResultVO addOrUpdate(CatalogVO catalogVO) {
    	Catalog catalog = new Catalog();
        Catalog oCatalog = catalogDao.findByName(catalogVO.getName());
        if(catalogVO.getId() != null) {		//修改目录
        	catalog = catalogDao.findOne(catalogVO.getId());
        	catalog.setName(catalogVO.getName());
        	catalog.setParentId(catalogVO.getParentId());
        	catalogVO.setDescription(catalogVO.getDescription());
        }
        else {		//添加新目录
        	if (oCatalog == null) {		//目录名不存在
        		catalog.setParentId(catalogVO.getParentId());
                catalog.setDescription(catalogVO.getDescription());
                catalog.setName(catalogVO.getName());
                catalog.setUsable(true);
        	}
        	else if(!oCatalog.getUsable()) {		//目录名已存在
        		oCatalog.setParentId(catalogVO.getParentId());
        		oCatalog.setDescription(catalogVO.getDescription());
        		oCatalog.setUsable(true);
        		catalogDao.update(oCatalog);
        		return new JsonResultVO(JsonResultVO.SUCCESS, "操作成功！");
        	}
        	else {
        		return new JsonResultVO(JsonResultVO.FAILURE, "目录名重复！");
			}
        }
		boolean flag = catalogDao.saveOrUpdate(catalog);
		if (flag)
		    return new JsonResultVO(JsonResultVO.SUCCESS, "操作成功！");
		else
			return new JsonResultVO(JsonResultVO.FAILURE, "操作失败！");
    }

    @Override public JsonResultVO deleteCatalogById(String catalogId) {
        boolean flag = catalogDao.deleteCatalog(catalogId);
        if (flag) {
            return new JsonResultVO(JsonResultVO.SUCCESS, "删除成功！");
        }
        return new JsonResultVO(JsonResultVO.FAILURE, "删除失败！");
    }

    @Override public CatalogVO getCatalogInfo(String catalogId) {
        Catalog catalog = catalogDao.findOne(catalogId);
        if (catalog != null) {
            return CatalogVO.generateBy(catalog);
        }
        return null;
    }

    @Override
	public List<CatalogVO> getCatalogList() {
    	List<Catalog> catalogList = catalogDao.getAll();
    	List<CatalogVO> catalogAllList = CatalogVO.generateBy(catalogList);
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
