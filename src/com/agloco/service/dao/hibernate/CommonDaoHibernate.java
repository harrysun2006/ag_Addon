package com.agloco.service.dao.hibernate;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.agloco.model.AGBadword;
import com.agloco.model.AGConfig;
import com.agloco.model.AGConfigPK;
import com.agloco.model.AGDictionary;
import com.agloco.model.AGDictionaryDetail;
import com.agloco.model.AGMailMessage;
import com.agloco.model.AdminLogConfig;
import com.agloco.model.DictionaryModel;
import com.agloco.service.dao.CommonDao;
import com.agloco.util.CharsetUtil;

/**
 * 
 * @author terry_zhao
 *
 */
public class CommonDaoHibernate extends HibernateDaoSupport implements
		CommonDao {

	public void addAGBadword(AGBadword agBadword) {		
		getHibernateTemplate().save(agBadword);
	}

	public void addAGConfig(AGConfig agConfig) {		
		getHibernateTemplate().save(agConfig);
//		ConfigUtil.refresh();
	}

	public void addAGDictionary(AGDictionary agDictionary) {		
		getHibernateTemplate().save(agDictionary);
//		DictionaryUtil.refresh();
	}

	public void addAGDictionaryDetail(AGDictionaryDetail agDictionaryDetail) {		
		getHibernateTemplate().save(agDictionaryDetail);
//		DictionaryUtil.refresh();
	}

	public void deleteAGBadword(AGBadword agBadword) {		
		getHibernateTemplate().delete(agBadword);
	}

	public void deleteAGConfig(AGConfig agConfig) {	
		getHibernateTemplate().delete(agConfig);
//		ConfigUtil.refresh();
	}

	public void deleteAGDictionary(AGDictionary agDictionary) {
		getHibernateTemplate().delete(agDictionary);
//		DictionaryUtil.refresh();
	}

	public void deleteAGDictionaryDetail(AGDictionaryDetail agDictionaryDetail) {
		getHibernateTemplate().delete(agDictionaryDetail);
//		DictionaryUtil.refresh();
	}

	public AGBadword getAGBadword(Long badwordId) {
		return (AGBadword) getHibernateTemplate().get(AGBadword.class, badwordId);
	}

	public AGConfig getAGConfig(String configName, String companyId) {
		AGConfigPK primaryKey = new AGConfigPK(configName, companyId);
		return (AGConfig) getHibernateTemplate().get(AGConfig.class, primaryKey);
		/*
		List list = 
			getHibernateTemplate().find(
					"from AGConfig agc where " +
					"agc.primaryKey.name=? and " +
					"agc.primaryKey.companyId=?", 
					new Object[]{configName,companyId});
		if(list == null || list.size() < 1){
			return null;
		}
		return (AGConfig)list.iterator().next();
		*/
	}

	public AGDictionary getAGDictionary(Long dictionaryId) {
		return (AGDictionary) getHibernateTemplate().get(AGDictionary.class, dictionaryId);
	}

	public AGDictionaryDetail getAGDictionaryDetail(Long dictionaryDetailId) {
		return (AGDictionaryDetail)getHibernateTemplate().get(AGDictionaryDetail.class, dictionaryDetailId);
	}

	public void updateAGBadword(AGBadword agBadword) {
		getHibernateTemplate().saveOrUpdate(agBadword);
	}

	public void updateAGConfig(AGConfig agConfig) {
		getHibernateTemplate().saveOrUpdate(agConfig);
//		ConfigUtil.refresh();
	}

	public void updateAGDictionary(AGDictionary agDictionary) {
		getHibernateTemplate().saveOrUpdate(agDictionary);
//		DictionaryUtil.refresh();
	}

	public void updateAGDictionaryDetail(AGDictionaryDetail agDictionaryDetail) {
		getHibernateTemplate().saveOrUpdate(agDictionaryDetail);
//		DictionaryUtil.refresh();
	}

	public List listAGBadword() {
		return getHibernateTemplate().find("from AGBadword");
	}

	public List listAGConfig() {
		return getHibernateTemplate().find("from AGConfig");
	}

	public List listAGDictionary() {
		return getHibernateTemplate().find("from AGDictionary order by code");
	}
	
	public List listAGDictionary(String code){
		return getHibernateTemplate().find("from AGDictionary agd where agd.code=?", code);
	}
	
	public List listAGDictionaryModel(String code,String language,String country){
		List list = getHibernateTemplate().find(
				"select agd.value,agdd.text " +
				"from AGDictionary agd,AGDictionaryDetail agdd " +
				"where agdd.dictionaryId=agd.dictionaryId and " +
				"agd.active=1 and " +
				"agd.code=? and " +
				"agdd.language=? and " +
				"agdd.country=? " +
				"order by agd.code", 
				new Object[]{code,language,country});
		
		if(list == null || list.size() < 1){
			return null;
		}
		
		List modelList = new ArrayList();
		for(Iterator it = list.iterator(); it.hasNext();){
			Object[] str = (Object[])it.next();
			DictionaryModel dm = new DictionaryModel();
			dm.setValue((String)str[0]);
			dm.setText(CharsetUtil.Unicode2UTF8((String)str[1]));
			modelList.add(dm);
		}
		return modelList;
	}

	public List listAGDictionaryText(String code){
		return  getHibernateTemplate().find(
				"select agd.value,agdd.text,agdd.language,agdd.country " +
				"from AGDictionary agd,AGDictionaryDetail agdd " +
				"where agdd.dictionaryId=agd.dictionaryId and " +
				"agd.code=? " +
				"order by agd.code", code);
	}
	
	public List listAGDictionaryText(String code,String language,String country){
		return  getHibernateTemplate().find(
				"select agd.value,agdd.text " +
				"from AGDictionary agd,AGDictionaryDetail agdd " +
				"where agdd.dictionaryId=agd.dictionaryId and " +
				"agd.code=? and " +
				"agdd.language=? and " +
				"agdd.country=? " +
				"order by agd.code", 
				new Object[]{code,language,country});
	}
	
	//add at 17/01/2007 by Terry
	public void addAGMailMessage(AGMailMessage msg){
		getHibernateTemplate().save(msg);
	}
	public void deleteAGMailMessage(AGMailMessage msg){
		getHibernateTemplate().delete(msg);
	}
	public List listAGMailMessage(final int pageSize){
		return getHibernateTemplate().executeFind(new HibernateCallback(){

			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				String hql = "from AGMailMessage order by id";
				Query q = getSession().createQuery(hql);
				q.setMaxResults(pageSize);
				return q.list();
			}
			
		});
	}
	public int getAGMailMessageNumber(){
		Long number =  (Long)getHibernateTemplate().execute(new HibernateCallback(){
			
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				String hql = "select count(*) from AGMailMessage";
				Query q = getSession().createQuery(hql);
				return q.uniqueResult();
			}
			
		});
		return number == null ? 0 : new Integer(number.toString()).intValue();
	}
	
	//add at 29/01/2007 by Terry
	public List listAGMailMessage(final int pageNumber,final int pageSize){
		return getHibernateTemplate().executeFind(new HibernateCallback(){

			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				String hql = "from AGMailMessage order by id";
				Query q = getSession().createQuery(hql);
				q.setFirstResult((pageNumber-1)*pageSize);
				q.setMaxResults(pageSize);
				return q.list();
			}
			
		});
	}
	public AGMailMessage getAGMailMessage(Long id){
		return (AGMailMessage)getHibernateTemplate().get(AGMailMessage.class, id);
	}
	
	//add at 12/02/2007
	public void addAdminLogConfig(AdminLogConfig cfg){
		getHibernateTemplate().save(cfg);
	}
	public void updateAdminLogConfig(AdminLogConfig cfg){
		getHibernateTemplate().saveOrUpdate(cfg);
	}
	public void deleteAdminLogConfig(AdminLogConfig cfg){
		getHibernateTemplate().delete(cfg);
	}
	
	public AdminLogConfig getAdminLogConfig(Long id){
		return (AdminLogConfig) getHibernateTemplate().get(AdminLogConfig.class, id);
	}
	public AdminLogConfig getAdminLogConfig(String className, String method){
		List list = getHibernateTemplate().find("from AdminLogConfig cfg where cfg.className=? and cfg.method=?",
					new Object[]{className,method});
		if(list == null || list.size() < 1){
			return null;
		}
		return (AdminLogConfig)list.iterator().next();
	}
	
	public List listAdminLogConfigMethods(String className){
		return getHibernateTemplate().find("select cfg.method from AdminLogConfig cfg where cfg.className=?", className);
	}
}
