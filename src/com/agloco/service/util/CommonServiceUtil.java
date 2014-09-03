package com.agloco.service.util;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import com.agloco.model.AGBadword;
import com.agloco.model.AGConfig;
import com.agloco.model.AGDictionary;
import com.agloco.model.AGDictionaryDetail;
import com.agloco.model.AGMailMessage;
import com.agloco.model.AdminLogConfig;
import com.agloco.service.CommonService;
import com.liferay.portal.spring.util.SpringUtil;
import com.liferay.portal.util.ClusterPool;
import com.liferay.util.StringPool;
import com.opensymphony.oscache.base.NeedsRefreshException;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;

/**
 * 
 * @author terry_zhao
 *
 */
public class CommonServiceUtil {

	private CommonService commonService; 
	//add at 08/02/2007
	private static GeneralCacheAdministrator _cache = ClusterPool.getCache();
	public final static String CACHE_ADMIN_LOG_CONFIG = "admin.log.config";
	public static final String GROUP_NAME = CommonServiceUtil.class.getName();
	public static final String[] GROUP_NAME_ARRAY = new String[] {GROUP_NAME};
	
	//add at 2007-03-08
	public final static Log log = LogFactory.getLog(CommonServiceUtil.class);
	
	public static void addAGBadword(AGBadword agBadword) {		
		getCommonService().addAGBadword(agBadword);
	}

	public static void addAGConfig(AGConfig agConfig) {		
		getCommonService().addAGConfig(agConfig);
	}

	public static void addAGDictionary(AGDictionary agDictionary) {		
		getCommonService().addAGDictionary(agDictionary);
	}

	public static void addAGDictionaryDetail(AGDictionaryDetail agDictionaryDetail) {		
		getCommonService().addAGDictionaryDetail(agDictionaryDetail);
	}

	public static void deleteAGBadword(AGBadword agBadword) {		
		getCommonService().deleteAGBadword(agBadword);
	}

	public static void deleteAGConfig(AGConfig agConfig) {	
		getCommonService().deleteAGConfig(agConfig);
	}

	public static void deleteAGDictionary(AGDictionary agDictionary) {
		getCommonService().deleteAGDictionary(agDictionary);
	}

	public static void deleteAGDictionaryDetail(AGDictionaryDetail agDictionaryDetail) {
		getCommonService().deleteAGDictionaryDetail(agDictionaryDetail);
	}

	public static AGBadword getAGBadword(Long badwordId) {
		return getCommonService().getAGBadword(badwordId);
	}

	public static AGConfig getAGConfig(String configName, String companyId) {
		return getCommonService().getAGConfig(configName, companyId);
	}

	public static AGDictionary getAGDictionary(Long dictionaryId) {
		return getCommonService().getAGDictionary(dictionaryId);
	}

	public static AGDictionaryDetail getAGDictionaryDetail(Long dictionaryDetailId) {
		return getCommonService().getAGDictionaryDetail(dictionaryDetailId);
	}

	public static void updateAGBadword(AGBadword agBadword) {
		getCommonService().updateAGBadword(agBadword);
	}

	public static void updateAGConfig(AGConfig agConfig) {
		getCommonService().updateAGConfig(agConfig);
	}

	public static void updateAGDictionary(AGDictionary agDictionary) {
		getCommonService().updateAGDictionary(agDictionary);
	}

	public static void updateAGDictionaryDetail(AGDictionaryDetail agDictionaryDetail) {
		getCommonService().updateAGDictionaryDetail(agDictionaryDetail);
	}
	
	public static List listAGBadword(){
		return getCommonService().listAGBadword();
	}
	
	public static List listAGConfig(){
		return getCommonService().listAGConfig();
	}
	
	public static List listAGDictionary(){
		return getCommonService().listAGDictionary();
	}
	
	public static List listAGDictionary(String code){
		return getCommonService().listAGDictionary(code);
	}
	
	public static List listAGDictionaryModel(String code,String language,String country){
		return getCommonService().listAGDictionaryModel(code, language, country);
	}
	
	public static List listAGDictionaryText(String code){
		return getCommonService().listAGDictionaryText(code);
	}
	
	public static List listAGDictionaryText(String code,String language,String country){
		return getCommonService().listAGDictionaryText(code, language, country);
	}
	
	//add at 17/01/2007 by Terry
	public static void addAGMailMessage(AGMailMessage msg){
		getCommonService().addAGMailMessage(msg);
	}
	public static void deleteAGMailMessage(AGMailMessage msg){
		getCommonService().deleteAGMailMessage(msg);	
	}
	public static List listAGMailMessage(int pageSize){
		return getCommonService().listAGMailMessage(pageSize);
	}
	public static int getAGMailMessageNumber(){
		return getCommonService().getAGMailMessageNumber();	
	}
	//add at 29/01/2007 by Terry
	public static List listAGMailMessage(int pageNumber,int pageSize){
		return getCommonService().listAGMailMessage(pageNumber, pageSize);
	}
	public static AGMailMessage getAGMailMessage(Long id){
		return getCommonService().getAGMailMessage(id);
	}
	
	//add at 31/01/2007 by Terry
	/**
	 * ids: AGMailMessage ids[]
	 */
	public static void deleteAGMailMessage(Long[] ids){
		getCommonService().deleteAGMailMessage(ids);
	}
	
	//	add at 12/02/2007
	public static void addAdminLogConfig(AdminLogConfig cfg){
		getCommonService().addAdminLogConfig(cfg);
	}
	public static void updateAdminLogConfig(AdminLogConfig cfg){
		getCommonService().updateAdminLogConfig(cfg);
	}
	public static void deleteAdminLogConfig(AdminLogConfig cfg){
		getCommonService().deleteAdminLogConfig(cfg);
	}
	
	public AdminLogConfig getAdminLogConfig(Long id){
		return getCommonService().getAdminLogConfig(id);
	}
	
	public static AdminLogConfig getAdminLogConfig(String className, String method){
		
		String key = encodeKey(className+method);
		AdminLogConfig cfg = null;
		try {
			cfg = (AdminLogConfig)_cache.getFromCache(key);
		} catch (NeedsRefreshException e) {
			cfg = getCommonService().getAdminLogConfig(className,method);
			if(cfg == null){
				cfg = getPatternAdminLogConfig(className, method);
			}
			_cache.putInCache(key, cfg, GROUP_NAME_ARRAY);
		}
		finally{
			if(cfg == null){
				_cache.cancelUpdate(key);
			}
		}
		
		if(cfg != null && AdminLogConfig.ACTIVE.equals(cfg.getActive())){
			return cfg;
		}
		
		return null;	

	}

	private static AdminLogConfig getPatternAdminLogConfig(String className, String method) {
		List list = listAdminLogConfigMethods(className);
		if(list == null || list.size() < 1){
			return null;
		}
		
		for(Iterator it = list.iterator(); it.hasNext();){
			String t = (String)it.next();
			if(method.equals(t)){
				continue;
			}
			try{
				Pattern p = Pattern.compile(t);
				Matcher m = p.matcher(method);
				if(m.matches()){
					return getAdminLogConfig(className,t);
				}	
			}
			catch(Exception e){
				if(log.isErrorEnabled()){
					log.error("pattern error", e);
				}
			}
		}
		return null;
	}

	public static List listAdminLogConfigMethods(String className){
		String key = encodeKey(CACHE_ADMIN_LOG_CONFIG);
		List list = null;
		try {
			list = (List)_cache.getFromCache(key);
		} catch (NeedsRefreshException e) {
			list = getCommonService().listAdminLogConfigMethods(className);
			_cache.putInCache(key, list, GROUP_NAME_ARRAY);
		}
		finally{
			if(list == null){
				_cache.cancelUpdate(key);
			}
		}
		return list;
	}
	public static void clearCommonServiceUtilPool() {
		_cache.flushGroup(GROUP_NAME);
	}

	public static void clearCommonServiceUtilPool(String key) {
		key = encodeKey(key);
		_cache.flushEntry(key);
	}	
	
	private static String encodeKey(String key) {
		return GROUP_NAME + StringPool.POUND + key;
	}
	
	public static CommonService getCommonService(){
		ApplicationContext ctx = SpringUtil.getContext();
		CommonServiceUtil util = (CommonServiceUtil)ctx.getBean(CommonServiceUtil.class.getName());
		return util.commonService;
	} 
	
	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}
	
}
