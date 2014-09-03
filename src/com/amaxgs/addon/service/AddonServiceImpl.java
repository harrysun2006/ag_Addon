package com.amaxgs.addon.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

import com.agloco.model.MessageObject;

/**
 * 
 * @author terry_zhao
 * 
 */

public class AddonServiceImpl implements AddonService {

	private AddonDaoHibernate addonDao;

	public Long getTotalNumber(String hql, String[] parameters, Object[] values) {
		return getTotalNumber(hql, parameters, values, true);
	}

	public Long getTotalNumber(String hql, String[] parameters, Object[] values, boolean bHql) {
		return addonDao.getTotalNumber(hql, parameters, values, bHql);
	}

	public List listObject(String hql, String[] parameters, Object[] values, Long pageNumber, Long pageSize) {
		// TODO list object by the parameter:hql
		return listObject(hql, parameters, values, pageNumber, pageSize, true, null);
	}

	public List listObject(String hql, String[] parameters, Object[] values,
			Long pageNumber, Long pageSize, boolean bHql, Class clazz) {
		return addonDao.listObject(hql, parameters, values, pageNumber, pageSize, bHql, clazz);
	}

	public List getMemberInfo(String conditions) {
		return addonDao.getMemberInfo(conditions);
	}

	public List getMemberAuditInfo(String conditions) {
		return addonDao.getMemberAuditInfo(conditions);
	}

	public List getLogInfo(String tableName, String conditions) {
		return addonDao.getLogInfo(tableName, conditions);
	}

	public StringBuffer getLogInfo(List tableNames, String conditions) {
		StringBuffer sb = new StringBuffer();
		DateFormat datef = new SimpleDateFormat("yyyy-MM-dd hh:mm:SS");
		if (tableNames != null && tableNames.size() > 0) {
			for (Iterator iter = tableNames.iterator(); iter.hasNext();) {
				String tbName = (String) iter.next();
				List tempList = getLogInfo(tbName, conditions);
				for (int i = 0; i < tempList.size(); i++) {
					MessageObject temp = (MessageObject) tempList.get(i);
					sb.append(datef.format(temp.getCreateDate().getTime())).append(",");
					sb.append(temp.getUserId()).append(",");
					sb.append(temp.getEmailAddress()).append(",");
					sb.append(temp.getMemberCode()).append(",");
					sb.append(temp.getReferralCode()).append(",");
					sb.append(temp.getOperate()).append(",");
					sb.append(temp.getIp()).append(",");
					sb.append(temp.getServerIp()).append(",");
					sb.append(temp.getSessionId()).append(",");
					sb.append(temp.getThread()).append(",");
					sb.append(temp.getPriority()).append(",");
					sb.append(temp.getCategory()).append(",");
					sb.append(temp.getMessage()).append(",");
					sb.append(temp.getException()).append(",");
					sb.append(temp.getUserAgent()).append(",");
					sb.append(temp.getDescription()).append(",");
					sb.append("\n");
				}
				// sb.append(getLogInfo(tbName,conditions));
			}
		}
		return sb;
	}

	public List listLogTableNames() {
		return addonDao.listLogTableNames();
	}

	public void setAddonDao(AddonDaoHibernate addonDao) {
		this.addonDao = addonDao;
	}

}
