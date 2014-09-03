package com.agloco.model;

import java.io.Serializable;

/**
 * 
 * @author terry_zhao
 *
 */
public class AGMemberCount implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1011841834593737534L;
	
	private Long memberId;
	private Long count;
	
	public Long getCount() {
		return count;
	}
	public void setCount(Long count) {
		this.count = count;
	}
	public Long getMemberId() {
		return memberId;
	}
	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}
	
}
