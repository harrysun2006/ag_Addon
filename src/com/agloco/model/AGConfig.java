package com.agloco.model;

import java.io.Serializable;

public class AGConfig implements Serializable {

	public static String AG_CONFIG_TYPE_TEXT = "T";
	public static String AG_CONFIG_TYPE_SHORT_TEXT = "S";
	public static String AG_CONFIG_TYPE_DISABLE = "X"; //disable the record 
	
	private AGConfigPK primaryKey;

	private String value;

	private String content;

	private String type;

	public AGConfigPK getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(AGConfigPK primaryKey) {
		this.primaryKey = primaryKey;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("PK").append(getPrimaryKey().toString());
		return sb.toString();
	}

	public boolean equals(Object other) {
		if (!(other instanceof AGConfig))
			return false;
		AGConfig castOther = (AGConfig) other;
		return castOther.getPrimaryKey().equals(primaryKey);
	}

	public int hashCode() {
		return getPrimaryKey().hashCode();
	}
}
