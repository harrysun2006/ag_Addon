package com.agloco.service.util;

import org.springframework.context.ApplicationContext;

import com.agloco.model.AGCounter;
import com.agloco.service.CodeService;
import com.liferay.portal.spring.util.SpringUtil;

public class CodeServiceUtil {

	private CodeService codeService; 

	public static String generateCode(String code) {		
		return getCodeService().generateCode(code);
	}
	
	public static String generateUserId(){
		return getCodeService().generateUserId();
	}

	//add at 2007-03-05
	public static String generateAdminMemberCode(){
		return getCodeService().generateAdminMemberCode();
	}
	public static String generateAdminUserId(){
		return getCodeService().generateAdminUserId();
	}
	public static AGCounter getAGCounter(String pk){
		return getCodeService().getAGCounter(pk);
	}
	
	public static CodeService getCodeService(){
		ApplicationContext ctx = SpringUtil.getContext();
		CodeServiceUtil util = (CodeServiceUtil) ctx.getBean(CodeServiceUtil.class.getName());
		return util.codeService;
	} 
	
	public void setCodeService(CodeService codeService) {
		this.codeService = codeService;
	}
}
