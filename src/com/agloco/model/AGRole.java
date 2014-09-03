package com.agloco.model;

import java.io.Serializable;

public class AGRole implements Serializable{
	
	private String roleId;
	private String companyId;
	private String className;
	private String classPK;
	private String name;
	private String description;
	
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getClassPK() {
		return classPK;
	}
	public void setClassPK(String classPK) {
		this.classPK = classPK;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	

}
