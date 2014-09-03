package com.agloco.model;

import java.io.Serializable;

import com.liferay.util.StringPool;

public class AGConfigPK implements Comparable, Serializable {

	public String name;
	public String companyId;

	public AGConfigPK() {
	}

	public AGConfigPK(String name, String companyId) {
		this.name = name;
		this.companyId = companyId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public int compareTo(Object obj) {
		if (obj == null) {
			return -1;
		}
		AGConfigPK pk = (AGConfigPK)obj;
		int value = 0;
		value = name.compareTo(pk.name);
		if (value != 0) {
			return value;
		}
		value = companyId.compareTo(pk.companyId);
		if (value != 0) {
			return value;
		}
		return 0;
	}

	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		AGConfigPK pk = null;

		try {
			pk = (AGConfigPK)obj;
		}
		catch (ClassCastException cce) {
			return false;
		}

		if ((name.equals(pk.name)) && (companyId.equals(pk.companyId))) {
			return true;
		}
		else {
			return false;
		}
	}

	public int hashCode() {
		return (name + companyId).hashCode();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(StringPool.OPEN_CURLY_BRACE);
		sb.append("layoutId");
		sb.append(StringPool.EQUAL);
		sb.append(name);
		sb.append(StringPool.COMMA);
		sb.append(StringPool.SPACE);
		sb.append("ownerId");
		sb.append(StringPool.EQUAL);
		sb.append(companyId);
		sb.append(StringPool.CLOSE_CURLY_BRACE);

		return sb.toString();
	}
}
