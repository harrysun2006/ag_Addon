package com.agloco.service.impl;

import java.util.List;

import com.agloco.model.AGBadword;
import com.agloco.model.AGConfig;
import com.agloco.model.AGDictionary;
import com.agloco.model.AGDictionaryDetail;
import com.agloco.model.AGMailMessage;
import com.agloco.model.AdminLogConfig;
import com.agloco.service.CommonService;
import com.agloco.service.dao.util.CommonDaoUtil;

/**
 * 
 * @author terry_zhao
 *
 */
public class CommonServiceImpl implements CommonService {

	public void addAGBadword(AGBadword agBadword) {
		CommonDaoUtil.addAGBadword(agBadword);
	}

	public void addAGConfig(AGConfig agConfig) {
		CommonDaoUtil.addAGConfig(agConfig);
	}

	public void addAGDictionary(AGDictionary agDictionary) {
		CommonDaoUtil.addAGDictionary(agDictionary);
	}

	public void addAGDictionaryDetail(AGDictionaryDetail agDictionaryDetail) {
		CommonDaoUtil.addAGDictionaryDetail(agDictionaryDetail);
	}

	public void deleteAGBadword(AGBadword agBadword) {
		CommonDaoUtil.deleteAGBadword(agBadword);
	}

	public void deleteAGConfig(AGConfig agConfig) {
		CommonDaoUtil.deleteAGConfig(agConfig);
	}

	public void deleteAGDictionary(AGDictionary agDictionary) {
		CommonDaoUtil.deleteAGDictionary(agDictionary);
	}

	public void deleteAGDictionaryDetail(AGDictionaryDetail agDictionaryDetail) {
		CommonDaoUtil.deleteAGDictionaryDetail(agDictionaryDetail);
	}

	public AGBadword getAGBadword(Long badwordId) {
		return CommonDaoUtil.getAGBadword(badwordId);
	}

	public AGConfig getAGConfig(String configName, String companyId) {
		return CommonDaoUtil.getAGConfig(configName, companyId);
	}

	public AGDictionary getAGDictionary(Long dictionaryId) {
		return CommonDaoUtil.getAGDictionary(dictionaryId);
	}

	public AGDictionaryDetail getAGDictionaryDetail(Long dictionaryDetailId) {
		return CommonDaoUtil.getAGDictionaryDetail(dictionaryDetailId);
	}

	public void updateAGBadword(AGBadword agBadword) {
		CommonDaoUtil.updateAGBadword(agBadword);
	}

	public void updateAGConfig(AGConfig agConfig) {
		CommonDaoUtil.updateAGConfig(agConfig);
	}

	public void updateAGDictionary(AGDictionary agDictionary) {
		CommonDaoUtil.updateAGDictionary(agDictionary);
	}

	public void updateAGDictionaryDetail(AGDictionaryDetail agDictionaryDetail) {
		CommonDaoUtil.updateAGDictionaryDetail(agDictionaryDetail);
	}
	
	public List listAGBadword(){
		return CommonDaoUtil.listAGBadword();
	}
	
	public List listAGConfig(){
		return CommonDaoUtil.listAGConfig();
	}
	
	public List listAGDictionary(){
		return CommonDaoUtil.listAGDictionary();
	}
	
	public List listAGDictionary(String code){
		return CommonDaoUtil.listAGDictionary(code);
	}
	
	public List listAGDictionaryModel(String code,String language,String country){
		return CommonDaoUtil.listAGDictionaryModel(code, language, country);
	}
	
	public List listAGDictionaryText(String code){
		return CommonDaoUtil.listAGDictionaryText(code);
	}
	
	public List listAGDictionaryText(String code,String language,String country){
		return CommonDaoUtil.listAGDictionaryText(code, language, country);
	}
	//add at 17/01/2007 by Terry
	public void addAGMailMessage(AGMailMessage msg){
		CommonDaoUtil.addAGMailMessage(msg);
	}
	public void deleteAGMailMessage(AGMailMessage msg){
		CommonDaoUtil.deleteAGMailMessage(msg);
	}
	public List listAGMailMessage(int pageSize){
		return CommonDaoUtil.listAGMailMessage(pageSize);
	}
	public int getAGMailMessageNumber(){
		return CommonDaoUtil.getAGMailMessageNumber();
	}
	//add at 29/01/2007 by Terry
	public List listAGMailMessage(int pageNumber,int pageSize){
		return CommonDaoUtil.listAGMailMessage(pageNumber, pageSize);
	}
	public AGMailMessage getAGMailMessage(Long id){
		return CommonDaoUtil.getAGMailMessage(id);
	}
	public void deleteAGMailMessage(Long[] ids){
		if(ids == null){
			return;
		}
		
		for(int i = 0; i < ids.length; i++){
			AGMailMessage msg = getAGMailMessage(ids[i]);
			if(msg != null){
				deleteAGMailMessage(msg);
			}
		}
	}
	
//	add at 12/02/2007
	public void addAdminLogConfig(AdminLogConfig cfg){
		CommonDaoUtil.addAdminLogConfig(cfg);
	}
	public void updateAdminLogConfig(AdminLogConfig cfg){
		CommonDaoUtil.updateAdminLogConfig(cfg);
	}
	public void deleteAdminLogConfig(AdminLogConfig cfg){
		CommonDaoUtil.deleteAdminLogConfig(cfg);
	}
	public AdminLogConfig getAdminLogConfig(Long id){
		return CommonDaoUtil.getAdminLogConfig(id);
	}
	public AdminLogConfig getAdminLogConfig(String className, String method){
		return CommonDaoUtil.getAdminLogConfig(className, method);
	}
	public List listAdminLogConfigMethods(String className){
		return CommonDaoUtil.listAdminLogConfigMethods(className);
	}
}
