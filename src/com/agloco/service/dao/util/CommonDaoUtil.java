package com.agloco.service.dao.util;

import java.util.List;

import org.springframework.context.ApplicationContext;

import com.agloco.model.AGBadword;
import com.agloco.model.AGConfig;
import com.agloco.model.AGDictionary;
import com.agloco.model.AGDictionaryDetail;
import com.agloco.model.AGMailMessage;
import com.agloco.model.AdminLogConfig;
import com.agloco.service.dao.CommonDao;
import com.liferay.portal.spring.util.SpringUtil;

/**
 * 
 * @author terry_zhao
 *
 */
public class CommonDaoUtil {

	private CommonDao commonDao; 
	

	public static void addAGBadword(AGBadword agBadword) {		
		getCommonDao().addAGBadword(agBadword);
	}

	public static void addAGConfig(AGConfig agConfig) {		
		getCommonDao().addAGConfig(agConfig);
	}

	public static void addAGDictionary(AGDictionary agDictionary) {		
		getCommonDao().addAGDictionary(agDictionary);
	}

	public static void addAGDictionaryDetail(AGDictionaryDetail agDictionaryDetail) {		
		getCommonDao().addAGDictionaryDetail(agDictionaryDetail);
	}

	public static void deleteAGBadword(AGBadword agBadword) {		
		getCommonDao().deleteAGBadword(agBadword);
	}

	public static void deleteAGConfig(AGConfig agConfig) {	
		getCommonDao().deleteAGConfig(agConfig);
	}

	public static void deleteAGDictionary(AGDictionary agDictionary) {
		getCommonDao().deleteAGDictionary(agDictionary);
	}

	public static void deleteAGDictionaryDetail(AGDictionaryDetail agDictionaryDetail) {
		getCommonDao().deleteAGDictionaryDetail(agDictionaryDetail);
	}

	public static AGBadword getAGBadword(Long badwordId) {
		return getCommonDao().getAGBadword(badwordId);
	}

	public static AGConfig getAGConfig(String configName, String companyId) {
		return getCommonDao().getAGConfig(configName, companyId);
	}

	public static AGDictionary getAGDictionary(Long dictionaryId) {
		return getCommonDao().getAGDictionary(dictionaryId);
	}

	public static AGDictionaryDetail getAGDictionaryDetail(Long dictionaryDetailId) {
		return getCommonDao().getAGDictionaryDetail(dictionaryDetailId);
	}

	public static void updateAGBadword(AGBadword agBadword) {
		getCommonDao().updateAGBadword(agBadword);
	}

	public static void updateAGConfig(AGConfig agConfig) {
		getCommonDao().updateAGConfig(agConfig);
	}

	public static void updateAGDictionary(AGDictionary agDictionary) {
		getCommonDao().updateAGDictionary(agDictionary);
	}

	public static void updateAGDictionaryDetail(AGDictionaryDetail agDictionaryDetail) {
		getCommonDao().updateAGDictionaryDetail(agDictionaryDetail);
	}
	
	public static List listAGBadword(){
		return getCommonDao().listAGBadword();
	}
	
	public static List listAGConfig(){
		return getCommonDao().listAGConfig();
	}
	
	public static List listAGDictionary(){
		return getCommonDao().listAGDictionary();
	}
	
	public static List listAGDictionary(String code){
		return getCommonDao().listAGDictionary(code);
	}
	
	public static List listAGDictionaryModel(String code,String language,String country){
		return getCommonDao().listAGDictionaryModel(code, language, country);
	}
	
	public static List listAGDictionaryText(String code){
		return getCommonDao().listAGDictionaryText(code);
	}
	
	public static List listAGDictionaryText(String code,String language,String country){
		return getCommonDao().listAGDictionaryText(code, language, country);
	}
	//add at 17/01/2007 by Terry
	public static void addAGMailMessage(AGMailMessage msg){
		getCommonDao().addAGMailMessage(msg);
	}
	public static void deleteAGMailMessage(AGMailMessage msg){
		getCommonDao().deleteAGMailMessage(msg);	
	}
	public static List listAGMailMessage(int pageSize){
		return getCommonDao().listAGMailMessage(pageSize);
	}
	public static int getAGMailMessageNumber(){
		return getCommonDao().getAGMailMessageNumber();	
	}
	//add at 29/01/2007 by Terry
	public static List listAGMailMessage(int pageNumber,int pageSize){
		return getCommonDao().listAGMailMessage(pageNumber, pageSize);
	}
	public static AGMailMessage getAGMailMessage(Long id){
		return getCommonDao().getAGMailMessage(id);
	}
	
	//	add at 12/02/2007
	public static void addAdminLogConfig(AdminLogConfig cfg){
		getCommonDao().addAdminLogConfig(cfg);
	}
	public static void updateAdminLogConfig(AdminLogConfig cfg){
		getCommonDao().updateAdminLogConfig(cfg);
	}
	public static void deleteAdminLogConfig(AdminLogConfig cfg){
		getCommonDao().deleteAdminLogConfig(cfg);
	}
	public static AdminLogConfig getAdminLogConfig(Long id){
		return getCommonDao().getAdminLogConfig(id);
	}
	public static AdminLogConfig getAdminLogConfig(String className, String method){
		return getCommonDao().getAdminLogConfig(className, method);
	}
	public static List listAdminLogConfigMethods(String className){
		return getCommonDao().listAdminLogConfigMethods(className);
	}
	public static CommonDao getCommonDao(){
		ApplicationContext ctx = SpringUtil.getContext();
		CommonDaoUtil util = (CommonDaoUtil)ctx.getBean(CommonDaoUtil.class.getName());
		return util.commonDao;
	} 
	
	public void setCommonDao(CommonDao commonDao) {
		this.commonDao = commonDao;
	}
}
