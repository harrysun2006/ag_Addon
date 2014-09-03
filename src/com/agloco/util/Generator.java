package com.agloco.util;

import com.agloco.Constants;
import com.agloco.exception.GenerateUserIdException;
import com.agloco.model.AGCounter;
import com.agloco.model.AGMemberTree;
import com.agloco.service.util.CodeServiceUtil;
import com.agloco.service.util.MemberServiceUtil;
import com.liferay.counter.service.spring.CounterServiceUtil;
import com.liferay.portal.SystemException;
import com.liferay.portal.security.pwd.PwdToolkitUtil;

/**
 * 
 * @author terry_zhao
 * 
 */
public class Generator {

	public static String generateMemberCode(String className)
			throws Exception {
		return MemberServiceUtil.generateMemberCode(className);
	}
	


	public static String generatePassword() {
//		return PwdGenerator.getPinNumber();
		return PwdToolkitUtil.generate();
	}

	public static String generateUserId(String companyId) throws GenerateUserIdException {
		return companyId + "." + CodeServiceUtil.generateUserId();
//		UserIdGenerator userIdGenerator = (UserIdGenerator) InstancePool
//				.get(PropsUtil.get(PropsUtil.USERS_ID_GENERATOR));
//		return userIdGenerator.generate(companyId);
	}

	public static long generateMemberTreeGroupId() throws SystemException {
		return CounterServiceUtil.increment(AGMemberTree.class.getName());
	}

	public static String generateAdminMemberCode(){
		return Constants.ADMIN_USER_MEMBER_CODE_PRE + CodeServiceUtil.generateAdminMemberCode();
	}
	
	public static String generateAdminUserId(String companyId){
		
		AGCounter counter = CodeServiceUtil.getAGCounter(Constants.ADMIN_USER_USER_ID_COUNTER);
		if(counter == null){
			return companyId + "." + CodeServiceUtil.generateAdminUserId();	
		}
		if(Integer.parseInt(counter.getValue()) < Constants.ADMIN_USER_USER_ID_MAX_NUMBER_VALUE){
			return companyId + "." + CodeServiceUtil.generateAdminUserId();
		}
		return companyId + "." + CodeServiceUtil.generateUserId();
		
	}
}
