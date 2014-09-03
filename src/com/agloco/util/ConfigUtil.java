package com.agloco.util;

import com.agloco.model.AGConfig;
import com.agloco.service.util.CommonServiceUtil;

/**
 * 
 * @author terry_zhao
 *
 */
public class ConfigUtil {
	
	public static String getConfig(String configName, String companyId){

		return getConfig(CommonServiceUtil.getAGConfig(configName, companyId));
	} 
	
	protected static String getConfig(AGConfig agConfig){
		
		if(agConfig == null){
			return null;
		}
		
		if(AGConfig.AG_CONFIG_TYPE_SHORT_TEXT.equals(agConfig.getType())){
			return agConfig.getValue();
		}
		
		if(AGConfig.AG_CONFIG_TYPE_TEXT.equals(agConfig.getType())){
			return agConfig.getContent();
		}
		
		return null;
		
	} 

	

}
