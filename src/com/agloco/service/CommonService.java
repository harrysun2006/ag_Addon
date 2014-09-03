package com.agloco.service;

import java.util.List;

import com.agloco.model.AGBadword;
import com.agloco.model.AGConfig;
import com.agloco.model.AGDictionary;
import com.agloco.model.AGDictionaryDetail;
import com.agloco.model.AGMailMessage;
import com.agloco.model.AdminLogConfig;
import com.agloco.service.impl.CommonServiceImpl;

/**
 * 
 * @author terry_zhao
 * @see CommonServiceImpl
 */
public interface CommonService extends BaseService {

	public void addAGBadword(AGBadword agBadword);
	public AGBadword getAGBadword(Long badwordId);
	public void updateAGBadword(AGBadword agBadword);
	public void deleteAGBadword(AGBadword agBadword);
	
	
	public void addAGConfig(AGConfig agConfig);
	public AGConfig getAGConfig(String configName,String companyId);
	public void updateAGConfig(AGConfig agConfig);
	public void deleteAGConfig(AGConfig agConfig);
	
	public void addAGDictionary(AGDictionary agDictionary);
	public AGDictionary getAGDictionary(Long dictionaryId);
	public void updateAGDictionary(AGDictionary agDictionary);
	public void deleteAGDictionary(AGDictionary agDictionary);
	
	public void addAGDictionaryDetail(AGDictionaryDetail agDictionaryDetail);
	public AGDictionaryDetail getAGDictionaryDetail(Long dictionaryDetailId);
	public void updateAGDictionaryDetail(AGDictionaryDetail agDictionaryDetail);
	public void deleteAGDictionaryDetail(AGDictionaryDetail agDictionaryDetail);
	
	public List listAGBadword();
	public List listAGConfig();
	public List listAGDictionary();
	public List listAGDictionary(String code);
	public List listAGDictionaryModel(String code,String language,String country);
	public List listAGDictionaryText(String code);
	public List listAGDictionaryText(String code,String language,String country);
	
	//add at 17/01/2007 by Terry
	public void addAGMailMessage(AGMailMessage msg);
	public void deleteAGMailMessage(AGMailMessage msg);
	public List listAGMailMessage(int pageSize);
	public int getAGMailMessageNumber();
	//add at 29/01/2007 by Terry
	public List listAGMailMessage(int pageNumber,int pageSize);
	public AGMailMessage getAGMailMessage(Long id);
	public void deleteAGMailMessage(Long[] ids);
	
	//	add at 12/02/2007
	public void addAdminLogConfig(AdminLogConfig cfg);
	public void updateAdminLogConfig(AdminLogConfig cfg);
	public void deleteAdminLogConfig(AdminLogConfig cfg);
	public AdminLogConfig getAdminLogConfig(Long id);
	public AdminLogConfig getAdminLogConfig(String className, String method);
	public List listAdminLogConfigMethods(String className);
}
