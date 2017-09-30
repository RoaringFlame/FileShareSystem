package com.fss.controller.vo;


import com.fss.dao.domain.User;
import com.fss.dao.enums.UserRole;
import com.fss.util.VoUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 返回用户信息所用数据类
 */
public class UserInfoVO {
    private String id;
    private String name;
    private String username;
    private String email;
    private String rolename;
    private String departmentname;
    private Integer gender;
    private Integer department;
    private Integer role;
    private List<String> operation;

    public static UserInfoVO generateBy(User user, UserRole searchRole) {
        UserInfoVO userInfoVO = VoUtil.copyBasic(UserInfoVO.class, user);
        assert userInfoVO != null;
        
        userInfoVO.setGender(user.getGender().ordinal());
        userInfoVO.setDepartment(user.getDepartment().ordinal());
        userInfoVO.setRole(user.getRole().ordinal());
        userInfoVO.setDepartmentname(user.getDepartment().getText());
        userInfoVO.setRolename(user.getRole().getText());
        userInfoVO.setOperation(getOperationList(searchRole,user.getRole()));
        return userInfoVO;
    }

    public static List<UserInfoVO> generateBy(List<User> userList, UserRole searchRole) {
        List<UserInfoVO> list = new ArrayList<>();
        for (User u : userList) {
            list.add(generateBy(u, searchRole));
        }
        return list;
    }
    
    public static List<String> getOperationList(UserRole searchRole,UserRole userRole) {
        List<String> operation1 = new ArrayList<>();
        if (searchRole == UserRole.MANAGER) {
            operation1.add("edit");
            operation1.add("delete");
            operation1.add("reset");
        }
        if (searchRole == UserRole.SENIOR) {
            if(userRole == UserRole.MANAGER) {
            }
            else if(userRole == UserRole.SENIOR){
                operation1.add("edit");
                operation1.add("reset");
            }
            else{
                operation1.add("edit");
                operation1.add("delete");
                operation1.add("reset");
            }
        }
        if (searchRole == UserRole.ORDINARY) {
        }
        return operation1;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

	public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

    public Integer getDepartment() {
		return department;
	}

	public void setDepartment(Integer department) {
		this.department = department;
	}

	public Integer getRole() {
		return role;
	}

	public void setRole(Integer role) {
		this.role = role;
	}

	public String getRolename() {
		return rolename;
	}

	public void setRolename(String rolename) {
		this.rolename = rolename;
	}

	public String getDepartmentname() {
		return departmentname;
	}

	public void setDepartmentname(String departmentname) {
		this.departmentname = departmentname;
	}

	public List<String> getOperation() {
        return operation;
    }

    public void setOperation(List<String> operation) {
        this.operation = operation;
    }
}
