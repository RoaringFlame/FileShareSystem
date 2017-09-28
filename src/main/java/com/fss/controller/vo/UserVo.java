package com.fss.controller.vo;

import com.fss.dao.domain.User;
import com.fss.dao.enums.Department;
import com.fss.dao.enums.Gender;
import com.fss.dao.enums.UserRole;
import com.fss.util.VoUtil;

/**
 * 增加，修改用户所用数据类
 */
public class UserVo {
    private String id;
    private String username;
    private String name;
    private Gender gender;
    private String email;
    private Department department;
    private UserRole role;
    private String genderId;
    private String departmentId;
    private String roleId;

    public static UserVo generateBy(User user){
        UserVo  userVo = VoUtil.copyBasic(UserVo.class,user);
        assert userVo!=null;
        userVo.setGender(user.getGender());
        userVo.setDepartment(user.getDepartment());
        userVo.setRole(user.getRole());
        return userVo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

	public String getGenderId() {
		return genderId;
	}

	public void setGenderId(String genderId) {
		this.genderId = genderId;
	}

	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public Department getDepartmentById() {
		int index = Integer.parseInt(this.getDepartmentId());
		Department u = null;
		for (Department v : Department.values()) {
            if (index == v.ordinal()) 
            	u = v;
        }
		return u;
	}

	public Gender getGenderById() {
		int index = Integer.parseInt(this.getGenderId());
		Gender u = null;
		for (Gender v : Gender.values()) {
            if (index == v.ordinal()) 
            	u = v;
        }
		return u;
	}

	public UserRole getRoleById() {
		int index = Integer.parseInt(this.getRoleId());
		UserRole u = null;
		for (UserRole v : UserRole.values()) {
            if (index == v.ordinal()) 
            	u = v;
        }
		return u;
	}
}
