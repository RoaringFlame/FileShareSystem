package com.fss.controller.vo;

import com.fss.dao.enums.UserRole;

public class UserSearchKeys {
    private String departmentKey;
    private String roleKey;
    private String nameKey;
    private String usernameKey;
    private UserRole searchRole;

    public String getDepartmentKey() {
        return departmentKey;
    }

    public void setDepartmentKey(String departmentKey) {
        this.departmentKey = departmentKey;
    }

    public String getRoleKey() {
        return roleKey;
    }

    public void setRoleKey(String roleKey) {
        this.roleKey = roleKey;
    }

    public String getNameKey() {
        return nameKey;
    }

    public void setNameKey(String nameKey) {
        this.nameKey = nameKey;
    }

    public String getUsernameKey() {
        return usernameKey;
    }

    public void setUsernameKey(String usernameKey) {
        this.usernameKey = usernameKey;
    }

	public UserRole getSearchRole() {
		return searchRole;
	}

	public void setSearchRole(UserRole searchRole) {
		this.searchRole = searchRole;
	}
}
