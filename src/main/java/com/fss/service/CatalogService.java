package com.fss.service;


import com.fss.controller.vo.CatalogVO;
import com.fss.controller.vo.JsonResultVO;

import java.util.List;
import java.util.Map;

public interface CatalogService {
    public JsonResultVO addOrUpdate(CatalogVO catalogVo);

    public JsonResultVO deleteCatalogById(String catalogId);

    public CatalogVO getCatalogInfo(String catalogId);

    public Map<String,Map<String,String>> getAllCatalog();

	public List<CatalogVO> getCatalogList();
}
