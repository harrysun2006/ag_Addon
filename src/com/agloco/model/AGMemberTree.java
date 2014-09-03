package com.agloco.model;

import java.io.Serializable;


public class AGMemberTree implements Serializable {

	public static final String STRING_AGMEMBER_STATUS_N = "N";
	public static final String STRING_AGMEMBER_STATUS_F = "F";
	
	private AGMemberTreePK primaryKey;
	private Long level;
	private String status;

	public AGMemberTreePK getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(AGMemberTreePK primaryKey) {
		this.primaryKey = primaryKey;
	}

	public Long getLevel() {
		return level;
	}

	public void setLevel(Long level) {
		this.level = level;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
