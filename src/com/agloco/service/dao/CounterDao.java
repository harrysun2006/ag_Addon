package com.agloco.service.dao;

import com.agloco.model.AGCounter;

/**
 * 
 * @author terry_zhao
 *
 */
public interface CounterDao {

	/**
	 * 
	 * @param className AGMember or AGMemberTemp class name 
	 * @return
	 */
	public String generateMemberCode(String className);
	public String generateUserId();
	
	//add at 2007-03-05
	public String generateAdminMemberCode();
	public String generateAdminUserId();
	public AGCounter getAGCounter(String pk);
}
