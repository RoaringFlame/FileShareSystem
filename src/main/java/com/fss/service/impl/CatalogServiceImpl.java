package com.fss.service.impl;

import com.fss.controller.vo.CatalogVO;
import com.fss.controller.vo.JsonResultVO;
import com.fss.dao.domain.Catalog;
import com.fss.dao.repositories.CatalogRepository;
import com.fss.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CatalogServiceImpl implements CatalogService {

    @Autowired
    private CatalogRepository catalogRepository;

    @Override
    public JsonResultVO addOrUpdate(CatalogVO catalogVo) {
        Catalog catalog = new Catalog();
        Catalog oCatalog = catalogRepository.findByName(catalogVo.getName());
        Catalog parentCatalog = catalogRepository.findOne(catalogVo.getParentId());
        if (catalogVo.getId() != null) {        //修改目录
            catalog = catalogRepository.findOne(catalogVo.getId());
            catalog.setName(catalogVo.getName());
            catalog.setParentCatalog(parentCatalog);
            catalogVo.setDescription(catalogVo.getDescription());
        } else {        //添加新目录
            if (oCatalog == null) {        //目录名不存在
                catalog.setParentCatalog(parentCatalog);
                catalog.setDescription(catalogVo.getDescription());
                catalog.setName(catalogVo.getName());
                catalog.setUsable(true);
            } else if (!oCatalog.isUsable()) {        //目录名已存在
                oCatalog.setParentCatalog(parentCatalog);
                oCatalog.setDescription(catalogVo.getDescription());
                oCatalog.setUsable(true);
                catalogRepository.saveAndFlush(oCatalog);
                return new JsonResultVO(JsonResultVO.SUCCESS, "操作成功！");
            } else {
                return new JsonResultVO(JsonResultVO.FAILURE, "目录名重复！");
            }
        }
        try {
            catalogRepository.saveAndFlush(catalog);
            return new JsonResultVO(JsonResultVO.SUCCESS, "操作成功！");
        } catch (Exception e) {
            return new JsonResultVO(JsonResultVO.FAILURE, "操作失败！");
        }
    }

    @Override
    public JsonResultVO deleteCatalogById(String catalogId) {
        try {
            catalogRepository.delete(catalogId);
            return new JsonResultVO(JsonResultVO.SUCCESS, "删除成功！");
        } catch (Exception e) {
            return new JsonResultVO(JsonResultVO.FAILURE, "删除失败！");
        }
    }

    @Override
    public CatalogVO getCatalogInfo(String catalogId) {
        Catalog catalog = catalogRepository.findOne(catalogId);
        if (catalog != null) {
            return CatalogVO.generateBy(catalog);
        }
        return null;
    }

    @Override
    public Map<String, Map<String, String>> getAllCatalog() {
        List<Catalog> catalogList = catalogRepository.findAll();
        Map<String, Map<String, String>> catalogMap = new HashMap<>();
        for (Catalog catalog : catalogList) {
            String id = catalog.getId();
            String parentId = catalog.getParentCatalog().getId();
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

    @Override
    public List<CatalogVO> getCatalogList() {
        List<Catalog> catalogList = catalogRepository.findAll();
        List<CatalogVO> catalogAllList = CatalogVO.generateBy(catalogList);
        return catalogAllList;
    }
}
