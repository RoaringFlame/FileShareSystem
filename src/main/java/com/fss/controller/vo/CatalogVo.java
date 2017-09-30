package com.fss.controller.vo;


import com.fss.dao.domain.Catalog;
import com.fss.util.VoUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 目录信息类
 */
public class CatalogVO {
    private String id;
    private String parentId;
    private String name;
    private String description;

    public static CatalogVO generateBy(Catalog catalog) {
        CatalogVO catalogVO = VoUtil.copyBasic(CatalogVO.class, catalog);
        assert catalog != null;
        return catalogVO;
    }
    
    public static List<CatalogVO> generateBy(List<Catalog> catalogList) {
    	List<CatalogVO> list = new ArrayList<>();
        for (Catalog u : catalogList) {
            list.add(generateBy(u));
        }
        return list;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
