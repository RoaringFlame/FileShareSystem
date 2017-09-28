package com.fss.dao.enums;


import com.fss.util.Selector;

import java.util.ArrayList;
import java.util.List;

public enum Department {
    HUMAN_RESOURCES("人力资源部"),ADMINISTRATION("行政部");

    private String text;

    Department(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    /**
     * 获取枚举的值(性别)
     *
     * @return 返回性别下拉列表中的值的集合
     */
    public static List<Selector> toList() {
        List<Selector> list = new ArrayList<Selector>();
        for (Department v : Department.values()) {
            list.add(new Selector(String.valueOf(v.ordinal()), v.getText()));
        }
        return list;
    }
}
