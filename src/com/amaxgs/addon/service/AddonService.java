package com.amaxgs.addon.service;

import java.util.List;

/**
 * 
 * @author terry_zhao
 * 
 */
public interface AddonService {

	/**
	 * 
	 * @param hql
	 * @param parameters
	 * @param values
	 * @return
	 */
	public Long getTotalNumber(String hql, String[] parameters, Object[] values);

	/**
	 * 
	 * @param hql
	 * @param parameters
	 * @param values
	 * @param preParameter
	 * @param postParameter
	 * @return
	 */
	public Long getTotalNumber(String hql, String[] parameters, Object[] values, boolean bHql);

	/**
	 * 
	 * @param hql
	 * @param parameters
	 * @param values
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	public List listObject(String hql, String[] parameters, Object[] values, Long pageNumber, Long pageSize);

	/**
	 * 
	 * @param hql
	 * @param parameters
	 * @param values
	 * @param preParameter
	 * @param postParameter
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	public List listObject(String hql, String[] parameters, Object[] values, Long pageNumber, Long pageSize, boolean bHql, Class clazz);

	public List listLogTableNames();

	public List getMemberInfo(String conditions);

	public List getMemberAuditInfo(String conditions);

	public List getLogInfo(String tableName, String conditions);

	public StringBuffer getLogInfo(List tableNames, String conditions);

}
