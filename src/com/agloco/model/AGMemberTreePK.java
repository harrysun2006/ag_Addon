package com.agloco.model;

import java.io.Serializable;

import com.liferay.util.StringPool;

public class AGMemberTreePK implements Comparable, Serializable {

	private Long memberId;
	private Long descendantId;

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public Long getDescendantId() {
		return descendantId;
	}

	public void setDescendantId(Long descendantId) {
		this.descendantId = descendantId;
	}

	public int compareTo(Object obj) {
		if (obj == null) {
			return -1;
		}
		AGMemberTreePK pk = (AGMemberTreePK)obj;
		int value = 0;
		value = memberId.compareTo(pk.memberId);
		if (value != 0) {
			return value;
		}
		value = descendantId.compareTo(pk.descendantId);
		if (value != 0) {
			return value;
		}
		return 0;
	}

	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		AGMemberTreePK pk = null;

		try {
			pk = (AGMemberTreePK)obj;
		}
		catch (ClassCastException cce) {
			return false;
		}

		if ((memberId.equals(pk.memberId)) && (descendantId.equals(pk.descendantId))) {
			return true;
		}
		else {
			return false;
		}
	}

	public int hashCode() {
		return memberId.hashCode() + descendantId.hashCode();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(StringPool.OPEN_CURLY_BRACE);
		sb.append("memberId");
		sb.append(StringPool.EQUAL);
		sb.append(memberId);
		sb.append(StringPool.COMMA);
		sb.append(StringPool.SPACE);
		sb.append("descendantId");
		sb.append(StringPool.EQUAL);
		sb.append(descendantId);
		sb.append(StringPool.CLOSE_CURLY_BRACE);

		return sb.toString();
	}
}
