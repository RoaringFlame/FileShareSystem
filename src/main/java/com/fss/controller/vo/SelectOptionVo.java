package com.fss.controller.vo;

import com.fss.util.Selector;

import java.util.List;

public class SelectOptionVO {
	private List<Selector> genderList;
	private List<Selector> departmentList;
	private List<Selector> roleList;
	
	
	public List<Selector> getGenderList() {
		return genderList;
	}
	public void setGenderList(List<Selector> genderList) {
		this.genderList = genderList;
	}
	public List<Selector> getDepartmentList() {
		return departmentList;
	}
	public void setDepartmentList(List<Selector> departmentList) {
		this.departmentList = departmentList;
	}
	
	public List<Selector> getRoleList() {
		return roleList;
	}
	public void setRoleList(List<Selector> roleList) {
		this.roleList = roleList;
	}
}
