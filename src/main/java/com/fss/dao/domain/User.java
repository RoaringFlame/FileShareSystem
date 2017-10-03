package com.fss.dao.domain;

/**
 * 用户实体类
 * 不使用逻辑删除
 * 方便添加修改信息判断账户是否存在等问题
 * 其他实体类均使用逻辑删除
 */

import com.fss.dao.enums.Department;
import com.fss.dao.enums.Gender;
import com.fss.dao.enums.UserRole;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "t_user", catalog = "")
public class User extends BaseEntity implements Serializable {
    private String username;
    private String password;
    private String name;
    private Gender gender;
    private String email;
    private Department department;
    private UserRole role;

    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "gender")
    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "department")
    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    @Column(name = "role")
    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
