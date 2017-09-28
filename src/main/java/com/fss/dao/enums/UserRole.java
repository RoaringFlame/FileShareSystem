package com.fss.dao.enums;


import com.fss.dao.domain.User;
import com.fss.util.Selector;

import java.util.ArrayList;
import java.util.List;

public enum UserRole {

    MANAGER("部门经理"), SENIOR("资深员工"), ORDINARY("普通员工");

    private String text;

    UserRole(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    /**
     * @return 返回性别下拉列表中的值的集合
     */
    public static List<Selector> toList() {
        List<Selector> list = new ArrayList<Selector>();
        for (UserRole v : UserRole.values()) {
            list.add(new Selector(String.valueOf(v.ordinal()), v.getText()));
        }
        return list;
    }
    
    public static List<Selector> toList(User user) {
        List<Selector> list = new ArrayList<Selector>();
        for (UserRole v : UserRole.values()) {
            list.add(new Selector(String.valueOf(v.ordinal()), v.getText()));
        }
        if(user.getRole()==UserRole.MANAGER) {
        }
        if(user.getRole()==UserRole.SENIOR) {
        	list.remove(0);
        }
        return list;
    }
}
