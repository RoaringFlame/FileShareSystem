package com.fss.service;


import com.fss.controller.vo.CatalogVo;
import com.fss.controller.vo.JsonResultVo;

import java.util.List;
import java.util.Map;

public interface ICatalogService {
    public JsonResultVo addOrUpdate(CatalogVo catalogVo);

    public JsonResultVo deleteCatalogById(String catalogId);

    public CatalogVo getCatalogInfo(String catalogId);

    public Map<String,Map<String,String>> getAllCatalog();

	public List<CatalogVo> getCatalogList();
}
