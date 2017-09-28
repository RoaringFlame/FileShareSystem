package com.fss.util.alibaba.message;

import com.fss.util.alibaba.message.smsBean.TextMessage;

public class ChangePasswordMes extends TextMessage{

    public ChangePasswordMes(){
        super.setTemplate("changePassword");
    }

    private String code;
    private String name;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getContent() {
        return "{code:\'"+ this.getCode()+"\',name:\'"+ this.getName()+"\'}";
    }
}
