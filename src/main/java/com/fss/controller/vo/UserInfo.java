package com.fss.controller.vo;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class UserInfo extends User {
    private String userId;
    private String name;
    private String email;
    private String department;
    private String pictureName;

    public UserInfo(String username, String password,Collection<GrantedAuthority> authorities)
            throws IllegalArgumentException {
        super(username,password,authorities);
    }

    public UserInfo(String username,String password,boolean usable,Collection<GrantedAuthority> authorities)
            throws IllegalArgumentException {
        super(username, password, usable, true, true, true, authorities);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPictureName() {
        return pictureName;
    }

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }
}
