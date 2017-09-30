package com.fss.service;


import com.fss.controller.vo.CatalogVO;
import com.fss.controller.vo.JsonResultVO;

import java.util.List;
import java.util.Map;

public interface ICatalogService {
    JsonResultVO addOrUpdate(CatalogVO catalogVO);

    JsonResultVO deleteCatalogById(String catalogId);

    CatalogVO getCatalogInfo(String catalogId);

    Map<String,Map<String,String>> getAllCatalog();

	List<CatalogVO> getCatalogList();
}
