package com.fss.controller.vo;

import java.util.List;

public class UserAlertVO {
    private List<UserAlert> alertList;
    private int count;

    public List<UserAlert> getAlertList() {
        return alertList;
    }

    public void setAlertList(List<UserAlert> alertList) {
        this.alertList = alertList;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
