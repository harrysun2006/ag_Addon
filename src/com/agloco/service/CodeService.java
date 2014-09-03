package com.agloco.service;

import com.agloco.model.AGCounter;

/**
 * 
 * @see SequenceCodeServiceImpl
 *
 */
public interface CodeService {

	public String generateCode(String code);
	public String generateUserId();
	//add at 2007-03-05
	public String generateAdminMemberCode();
	public String generateAdminUserId();
	public AGCounter getAGCounter(String pk);
}
