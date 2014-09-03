package com.amaxgs.addon.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.agloco.exception.DataAccessException;
import com.agloco.model.MessageObject;

/**
 * 
 * @author terry_zhao
 * 
 */

class AddonDaoHibernate extends HibernateDaoSupport {

	/**
	 * 
	 * @param hql
	 * @param parameters
	 * @param values
	 * @return
	 */
	public Long getTotalNumber(final String hql, final String[] parameters,
			final Object[] values, final boolean bHql) {
		Long number = (Long) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query q = null;
						if (bHql) {
							q = getSession().createQuery(hql);
						} else {
							q = getSession().createSQLQuery(hql);
						}
						if (parameters != null && values != null) {
							if (parameters.length != values.length) {
								throw new ParametersNotMatchException();
							}
							for (int i = 0; i < parameters.length; i++) {
								q.setParameter(parameters[i], values[i]);
							}
						}
						return q.uniqueResult();
					}

				});
		return number;
	}

	/**
	 * 
	 * @param hql
	 * @param parameters
	 * @param values
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	public List listObject(final String hql, final String[] parameters,
			final Object[] values, final Long pageNumber, final Long pageSize,
			final boolean bHql, final Class clazz) {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query q = null;
				if (bHql) {
					q = getSession().createQuery(hql);
				} else {
					if (clazz != null)
						q = getSession().createSQLQuery(hql).addEntity(clazz);
					else
						q = getSession().createSQLQuery(hql);
				}
				if (parameters != null && values != null) {
					if (parameters.length != values.length) {
						throw new ParametersNotMatchException();
					}
					for (int i = 0; i < parameters.length; i++) {
						q.setParameter(parameters[i], values[i]);
					}
				}

				if (pageSize != null) {
					q.setMaxResults(pageSize.intValue());
				}
				if (pageNumber != null && pageSize != null) {
					q.setFirstResult((pageNumber.intValue() - 1)
							* pageSize.intValue());
				}

				return q.list();
			}

		});
	}

	public List listLogTableNames() {

		List list = new ArrayList();
		String sql = "show tables like ?";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = getSession().connection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, "AG_Log%");
			rs = ps.executeQuery();
			while (rs != null && rs.next()) {
				list.add(rs.getString(1));
			}
		} catch (SQLException e) {
			if (log.isErrorEnabled()) {
				log.error("list log table names error", e);
			}
			throw new DataAccessException(e);
		}
		return list;
	}

	public List getMemberInfo(String conditions) {
		StringBuffer sql = new StringBuffer(
				"Select m From AGMember m where 1=1 and ");

		List list = null;
		try {
			if (StringUtils.isBlank(conditions)) {
				throw new DataAccessException("conditions can not be null");
			}
			sql.append(conditions);
			Query q = getSession().createQuery(sql.toString());
			list = q.list();
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error("get member info error", e);
			}
			throw new DataAccessException(e);
		}
		return list;
	}

	public List getMemberAuditInfo(String conditions) {
		StringBuffer sql = new StringBuffer(
				"select m from AGMemberAudit m where 1=1 and ");

		List list = null;
		try {
			if (StringUtils.isBlank(conditions)) {
				throw new DataAccessException("conditions can not be null");
			}
			sql.append(conditions);
			Query q = getSession().createQuery(sql.toString());
			list = q.list();
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error("get member audit info error", e);
			}
			throw new DataAccessException(e);
		}
		return list;
	}

	public List getLogInfo(String tableName, String conditions) {

		// StringBuffer sql = new StringBuffer(
		// "select m from MessageObject m where 1=1 and ");
		StringBuffer sql = new StringBuffer();
		sql.append("select logId,createDate,thread,priority,category,message");
		sql
				.append(",exception,userId,emailAddress,memberCode,referralCode,operate,ip,serverIp,sessionId,userAgent,description from ");
		sql.append(tableName).append(" m where 1=1 and  ");
		List list = null;

		try {
			if (StringUtils.isBlank(conditions)) {
				throw new DataAccessException("conditions can not be null");
			}
			sql.append(conditions);
			Query q = getSession().createSQLQuery(sql.toString()).addEntity(
					MessageObject.class);
			// Query q = getSession().createQuery(sql.toString());
			list = q.list();
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error("get Log info error", e);
			}
			throw new DataAccessException(e);
		}
		return list;
	}

	private static final Log log = LogFactory.getLog(AddonDaoHibernate.class);
}
