package com.agloco.service.dao.hibernate;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.agloco.Constants;
import com.agloco.exception.DataAccessException;
import com.agloco.exception.DateFormatInvalidException;
import com.agloco.exception.DuplicateEmailAddressException;
import com.agloco.exception.EmptyMemberCodeException;
import com.agloco.exception.GenerateMemberCodeException;
import com.agloco.form.MemberQueryForm;
import com.agloco.model.AGMember;
import com.agloco.model.AGMemberAudit;
import com.agloco.model.AGMemberCount;
import com.agloco.model.AGMemberTemp;
import com.agloco.model.AGUser;
import com.agloco.service.dao.MemberDao;
import com.agloco.util.Generator;
import com.agloco.util.HibernateUtil;
import com.agloco.util.StringUtil;
import com.liferay.util.StringPool;

/**
 * 
 * @author terry_zhao
 *
 */
public class MemberDaoHibernate extends HibernateDaoSupport implements MemberDao{

	public void addAGMember(AGMember member) {
		member.setStatus(AGMember.MEMBER_STATUS_ACTIVE);
		
		//add at 12/14/06
		try {
			member.setMemberCode(Generator.generateMemberCode(AGMemberTemp.class.getName()));	
			
		} catch (Exception e) {
			throw new GenerateMemberCodeException();
		}
		
		if(StringUtils.isBlank(member.getMemberCode())){
			throw new EmptyMemberCodeException();
		}
		
		member.encrypt();
		getHibernateTemplate().save(member);
	}
	
	public void addAGMemberAdmin(AGMember member){
		member.setStatus(AGMember.MEMBER_STATUS_ACTIVE);
		
		if(StringUtils.isBlank(member.getMemberCode())){
			member.setMemberCode(Generator.generateAdminMemberCode());
		}
		
		if(StringUtils.isBlank(member.getMemberCode())){
			throw new EmptyMemberCodeException();
		}
		
		member.encrypt();
		getHibernateTemplate().save(member);
	}
	public void addAGMemberAudit(AGMemberAudit ma){
		ma.encrypt();
		getHibernateTemplate().save(ma);
	}

	public void addAGMemberAudit(String operatorId, Long memberId) throws IllegalAccessException, InvocationTargetException{
		AGMember member = getAGMember(memberId);
		getSession().evict(member);
		AGMemberAudit memberAudit = new AGMemberAudit();
		BeanUtils.copyProperties(memberAudit, member);
		memberAudit.setOperator(operatorId);
		memberAudit.setOperation(AGMemberAudit.OPERATION_EDIT);
		memberAudit.setOperateDate(Calendar.getInstance());
		memberAudit.setUserId(member.getUserId());
		memberAudit.setMemberId(member.getMemberId());
		memberAudit.encrypt();
		getHibernateTemplate().save(memberAudit);
	}
	
	public void addAGMemberAudit(String operatorId, AGMember member) throws IllegalAccessException, InvocationTargetException{
		getSession().evict(member);
		AGMemberAudit memberAudit = new AGMemberAudit();
		BeanUtils.copyProperties(memberAudit, member);
		memberAudit.setOperator(operatorId);
		memberAudit.setOperation(AGMemberAudit.OPERATION_EDIT);
		memberAudit.setOperateDate(Calendar.getInstance());
		memberAudit.setUserId(member.getUserId());
		memberAudit.setMemberId(member.getMemberId());
		memberAudit.encrypt();
		getHibernateTemplate().save(memberAudit);
	}
	
	public void addAGMemberTempAudit(String operatorId, AGMemberTemp agMemberTemp) throws IllegalAccessException, InvocationTargetException{
		AGMember member = new AGMember();
		BeanUtils.copyProperties(member, agMemberTemp);
		addAGMemberAudit(operatorId,member);
	}
	
	public void addAGMemberTemp(AGMemberTemp mt) {
		
		mt.setStatus(AGMemberTemp.DEFAULT_MEMBER_TEMP_STATUS_ACTIVE);
		
		/*12/14/06  remove this part to addAGMember method, which do not waste memberCode.
		try {
			mt.setMemberCode(Generator.generateMemberCode(AGMemberTemp.class.getName()));
		} catch (Exception e) {
			throw new GenerateMemberCodeException();
		}
		
		if(StringUtils.isBlank(mt.getMemberCode())){
			throw new EmptyMemberCodeException();
		}
		*/
		mt.encrypt();
		getHibernateTemplate().save(mt);
		
	}

	public void addAGUser(AGUser user) {
	
		getHibernateTemplate().save(user);
	}

	public void deleteAgMember(AGMember member) {
	
		getHibernateTemplate().delete(member);
	}
	
	public void deleteAGMemberAudit(AGMemberAudit ma){
		getHibernateTemplate().delete(ma);
	}

	public void deleteAgMemberTemp(AGMemberTemp mt) {
	
		getHibernateTemplate().delete(mt);
	}

	public AGMember getAGMember(Long memberId) {
		return (AGMember) getHibernateTemplate().get(AGMember.class, memberId);
	}

	public AGMemberAudit getAGMemberAudit(Long auditId){
		return (AGMemberAudit)getAGMemberAudit(auditId);
	}
	
	public AGMember getAGMemberByBoth(String keyword) {
		if(StringUtils.isBlank(keyword)){
			return null;
		}
		if(keyword.indexOf("@") > 0){
			return getAGMemberByEmail(keyword);
		}
		return getAGMemberByCode(keyword);
		
		//modified at 07/01/2007
		/*
		List list = getHibernateTemplate().find(
					"from AGMember agm where agm.memberCode=?" +
									    " or agm.emailAddress=?", new Object[]{keyword,keyword});
		if(list != null && list.size() > 0){
			return (AGMember) list.iterator().next();
		}
		return null;
		*/
	}

	public AGMember getAGMemberByCode(String memberCode) {
	
		List list = getHibernateTemplate().find("from AGMember agm where agm.memberCode=?",memberCode);
		if(list != null && list.size() > 0){
			return (AGMember) list.iterator().next();
		}
		return null;
	}

	public AGMember getAGMemberByEmail(String emailAddress) {
	
		List list = getHibernateTemplate().find("from AGMember agm where agm.emailAddress=?", emailAddress);
		if(list != null && list.size() > 0){
			return (AGMember) list.iterator().next();
		}
		return null;
	}
	public AGMember getAGMemberByUserId(String userId){
		List list = getHibernateTemplate().find("from AGMember agm where agm.userId=?", userId);
		if(list != null && list.size() > 0){
			return (AGMember) list.iterator().next();
		}
		return null;
	}

	public AGMemberTemp getAGMemberTemp(Long memberId) {
		return (AGMemberTemp)getHibernateTemplate().get(AGMemberTemp.class,memberId);
	}

	public AGMemberTemp getAGMemberTempByBoth(String keyword) {
		if(StringUtils.isBlank(keyword)){
			return null;
		}
		if(keyword.indexOf("@") > 0){
			return getAGMemberTempByEmail(keyword);
		}
		return getAGMemberTempByCode(keyword);
		
		/*
		List list = getHibernateTemplate().find(
				"from com.agloco.model.AGMemberTemp agm where agm.memberCode=?" +
								    " or agm.emailAddress=?", new Object[]{keyword,keyword});
		if(list != null && list.size() > 0){
			return (AGMemberTemp) list.iterator().next();
		}
		return null;
		*/
	}

	public AGMemberTemp getAGMemberTempByCode(String memberCode) {
		
		List list = getHibernateTemplate().find("from AGMemberTemp agm where agm.memberCode=?",memberCode);
		if(list != null && list.size() > 0){
			return (AGMemberTemp) list.iterator().next();
		}
		return null;
	}

	public AGMemberTemp getAGMemberTempByEmail(String emailAddress) {
		
		List list = getHibernateTemplate().find("from AGMemberTemp agm where agm.emailAddress=?",emailAddress);
		if(list != null && list.size() > 0){
			return (AGMemberTemp) list.iterator().next();
		}
		return null;
	}
	
	public AGMemberTemp getAGMemberTempByUserId(String userId){
		List list = getHibernateTemplate().find("from AGMemberTemp agm where agm.userId=?", userId);
		if(list != null && list.size() > 0){
			return (AGMemberTemp) list.iterator().next();
		}
		return null;
	}

	public AGUser getAGUser(String userId) {
		return (AGUser)getHibernateTemplate().get(AGUser.class, userId);
	}
	
	public List listAgMember() {
		return getHibernateTemplate().find("from AGMember agm where agm.status <>?",AGMember.MEMBER_STATUS_INACTIVE);
	}

	public List listAgMember(final String sql, final Object[] params, final int pageNum, final int pageSize) {
		return getHibernateTemplate().executeFind(new HibernateCallback(){

			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query q = getSession().createQuery(sql);

				if (params != null) {
					for (int i = 0; i < params.length; i++) {
						q.setParameter(i, params[i]);
					}
				}
				if(pageSize!=0)
				{
					q.setMaxResults(pageSize);
					q.setFirstResult(pageNum * pageSize);
				}
				
				return q.list();
			}
			
		});

	}

	public List listAgMemberNotLogin(){
		return getHibernateTemplate().find(
				"select m " +
				"from AGMember m,com.liferay.portal.model.User u " +
				"where m.userId = u.userId " +
				"and u.loginIP='' and m.status <>?",AGMember.MEMBER_STATUS_INACTIVE);
	}
	
	public List listAGMemberAuditByUserId(String userId){
		return getHibernateTemplate().find("from AGMemberAudit agma where agma.userId", userId);
	}
	
	public List listAGMemberAuditByMemberCode(String memberCode){
		return getHibernateTemplate().find("from AGMemberAudit agma where agma.memberCode", memberCode);
	}
	
	public List listAGMemberAuditByEmailAddress(String emailAddress){
		return getHibernateTemplate().find("from AGMemberAudit agma where agma.emailAddress", emailAddress);
	}

	public List listAgMemberTemp() {
		return getHibernateTemplate().find("from AGMemberTemp agmt where agmt.status<>?",AGMemberTemp.DEFAULT_MEMBER_TEMP_STATUS_INACTIVE);
	}

	public List listAgMemberTemp(final Date begin, final Date end) {
		return getHibernateTemplate().executeFind(new HibernateCallback(){

			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer();
				hql.append("select agmt from AGMemberTemp agmt where 1 = 1");
				if(begin != null) hql.append(" and agmt.createDate >= :begin");
				if(end != null) hql.append(" and agmt.createDate <= :end");
				
				hql.append(" and agmt.status <> :activeStatus");
				
				Query q = getSession().createQuery(hql.toString());
				if(begin != null) q.setParameter("begin", begin);
				if(end != null) q.setParameter("end", end);
				
				q.setParameter("activeStatus", AGMemberTemp.DEFAULT_MEMBER_TEMP_STATUS_INACTIVE);
				return q.list();
			}
			
		});

	}
	
	public List listAgMemberTempSendEmail(final int maxMailCount,final int intervalDay,final Calendar time){
		return getHibernateTemplate().executeFind(new HibernateCallback(){

			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Calendar c = time;
//				intervalDay = 0 - intervalDay;
				c.add(Calendar.DAY_OF_YEAR, 0-intervalDay);
				String hql = "from AGMemberTemp agmt where agmt.mailCount < :mailCount and agmt.lastMailTime < :lastMailTime and agmt.status <> :activeStatus";
				Query q = getSession().createQuery(hql);
				q.setParameter("mailCount", new Integer(maxMailCount));
				q.setParameter("lastMailTime", c);
				q.setParameter("activeStatus", AGMemberTemp.DEFAULT_MEMBER_TEMP_STATUS_INACTIVE);
				q.setMaxResults(20);
				return q.list();
			}
			
		});

	}
	
	public List listNewAgMemberTempSendEmail(final int lesMailCount,final int interval, final int ago,final Calendar time){
		return getHibernateTemplate().executeFind(new HibernateCallback(){

			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Calendar beginTime = time;
				Calendar endTime = time;
				beginTime.add(Calendar.SECOND, 0-interval-ago);
				endTime.add(Calendar.SECOND, 0-ago);
				String hql = "from AGMemberTemp agmt where agmt.mailCount <= :mailCount and agmt.lastMailTime >= :beginTime and agmt.lastMailTime < :endTime and agmt.status <> :activeStatus";
				Query q = getSession().createQuery(hql);
				q.setParameter("mailCount", new Integer(lesMailCount));
				q.setParameter("beginTime", beginTime);
				q.setParameter("endTime", endTime);
				q.setParameter("activeStatus", AGMemberTemp.DEFAULT_MEMBER_TEMP_STATUS_INACTIVE);
				q.setMaxResults(20);
				return q.list();
			}
			
		});

	}
	
	public void updateAGMember(AGMember member) {
		
		if(member == null){
			return;
		}
		getSession().evict(member);
		
		AGMember agMember = getAGMemberByEmail(member.getEmailAddress());
		if(agMember != null && member.getMemberId().compareTo(agMember.getMemberId()) != 0){
			throw new DuplicateEmailAddressException();
		}
		getSession().evict(agMember);
		
		AGMemberTemp agMemberTemp = getAGMemberTempByEmail(member.getEmailAddress());
		if(agMemberTemp != null){
			throw new DuplicateEmailAddressException();
		}
		getSession().evict(agMemberTemp);
		
		member.setModifiedDate(Calendar.getInstance());
		member.encrypt();
		getHibernateTemplate().saveOrUpdate(member);
		getSession().flush();
	}

	public void updateAGMemberAudit(AGMemberAudit ma){
		ma.encrypt();
		getHibernateTemplate().saveOrUpdate(ma);
	}
	
	public void updateAGMemberTemp(AGMemberTemp mt) {
		if(mt == null){
			return;
		}
		getSession().evict(mt);
		
		AGMember m = getAGMemberByEmail(mt.getEmailAddress());
		if(m != null ){
			throw new DuplicateEmailAddressException();
		}
		getSession().evict(m);
		
		AGMemberTemp temp = getAGMemberTempByEmail(mt.getEmailAddress());
		if(temp != null && temp.getMemberId().compareTo(mt.getMemberId()) != 0){
			throw new DuplicateEmailAddressException();
		}
		getSession().evict(temp);
		
		mt.setModifiedDate(Calendar.getInstance());
		mt.encrypt();
		getHibernateTemplate().saveOrUpdate(mt);
		getSession().flush();
	}

	public void updateAGUser(AGUser user) {
		getHibernateTemplate().saveOrUpdate(user);
	}

	

	/**
	 * 
	 * add at 12/05/06 by terry
	 */
	public AGMemberCount getAGMemberCount(Long memberId){
		return (AGMemberCount) getHibernateTemplate().get(AGMemberCount.class,memberId);
	}
	
	public void addAGMemberCount(AGMemberCount agmc){
		agmc.setCount(new Long(0));
		getHibernateTemplate().save(agmc);
	}
	
	public void updateAGMemberCount(final AGMemberCount agmc,final Long level,final String status){
		getHibernateTemplate().execute(new HibernateCallback(){

			public Object doInHibernate(Session arg0) throws HibernateException, SQLException {

				if(agmc == null){
					return null;
				}
				StringBuffer sb = new StringBuffer(64);
				sb.append("update AG_M_Count set count_=")
				  .append("(select count(*) from AG_M_Tree ")
				  .append("where memberId = :memberId and ")
				  .append("level_ <= :level and ")
				  .append("status = :status) ")
				  .append("where AG_M_Count.memberId = :memberId2");
				
				Query q = getSession().createSQLQuery(sb.toString());
				q.setParameter("memberId", agmc.getMemberId());
				q.setParameter("level", level);
				q.setParameter("status", status);
				q.setParameter("memberId2", agmc.getMemberId());
				q.executeUpdate();
				return null;
				
//				StringBuffer sb = new StringBuffer(64);
//				sb.append("update AGMemberCount set count=")
//				  .append("(select count(*) from AGMemberTree agmt ")
//				  .append("where agmt.memberId = :memberId and ")
//				  .append("agmt.level <= :level and ")
//				  .append("agmt.status = : status")
//				  .append(")");
//				 
//				System.out.println("SQL:"+sb.toString());
//				Query q = getSession().createQuery(sb.toString());
//				q.setLong("memberId", 20);
//				q.setLong("level", 5);
//				q.setString("status", "N");
//				int a = q.executeUpdate();
//				return null;
			}
			
		});
	}
	
	public List listAGMemberCount(){
		return getHibernateTemplate().find("from AGMemberCount order by memberId");
	}
	public List listAGMemberCountNumber(){
		return getHibernateTemplate().find("select agmc.count from AGMemberCount agmc order by agmc.count desc");
	}
	
	public int getAGMemberCountNumber(){
				
		Long number =  (Long)getHibernateTemplate().execute(new HibernateCallback(){

			public Object doInHibernate(Session arg0) throws HibernateException, SQLException {
	
				Query q = getSession().createQuery("select count(*) from AGMemberCount");
				return q.uniqueResult();
				
			}
			
		});
		
		return number == null ? 0 : (new Integer(number.toString())).intValue();
	}
	public List listAGMemberCount(final int pageSize,final int pageNumber){
		return getHibernateTemplate().executeFind(new HibernateCallback(){

			public Object doInHibernate(Session arg0) throws HibernateException, SQLException {
				
				String hql = "from AGMemberCount order by count desc";
				Query q = getSession().createQuery(hql);
				q.setFirstResult(pageNumber*pageSize);
				q.setMaxResults(pageSize);
				return q.list();
			}
			
		});
	}

	/**
	 * add at 12/06/06
	 */
	public int getAGMemberNumber(){
		Long number =  (Long)getHibernateTemplate().execute(new HibernateCallback(){

			public Object doInHibernate(Session arg0) throws HibernateException, SQLException {
				
				Query q = getSession().createQuery("select count(*) from AGMember");
				return q.uniqueResult();
				
			}
			
		});
		return number == null ? 0 : (new Integer(number.toString())).intValue();
	}

	public int getAGMemberNumber(final String sql, final Object[] params){
		Long number = (Long)getHibernateTemplate().execute(new HibernateCallback(){

			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				// TODO Auto-generated method stub
				Query q = getSession().createQuery(sql);

				if (params != null) {
					for (int i = 0; i < params.length; i++) {
						q.setParameter(i, params[i]);
					}
				}
				return q.uniqueResult();
			}
			
		});
		return number == null ? 0 : (new Integer(number.toString())).intValue();
	}
	
	/**
	 * return list of AGMember
	 */
	public List listAGMemeber(final int pageSize,final int pageNumber){
		return getHibernateTemplate().executeFind(new HibernateCallback(){

			public Object doInHibernate(Session arg0) throws HibernateException, SQLException {
				
				String hql = "from AGMemberCount order by memberId";
				Query q = getSession().createQuery(hql);
				q.setFirstResult(pageNumber*pageSize);
				q.setMaxResults(pageSize);
				return q.list();
			}
			
		});
	}
	
	public void updateAGMemberCountTask(final Long level,final String status){
		// TODO update table: AG_M_Count  according to table:AG_M_Tree
		getHibernateTemplate().execute(new HibernateCallback(){

			public Object doInHibernate(Session arg0) throws HibernateException, SQLException {
				

				StringBuffer sb = new StringBuffer(64);
				sb.append("update AG_M_Count set count_=")
				  .append("(select count(*) from AG_M_Tree ")
				  .append("where AG_M_Tree.memberId = AG_M_Count.memberId and ")
				  .append("level_ <= :level and ")
				  .append("status = :status) ");
				
				Query q = getSession().createSQLQuery(sb.toString());

				q.setParameter("level", level);
				q.setParameter("status", status);

				q.executeUpdate();
				return null;

			}
			
		});
	}
	
	/**
	 * modified at 12/07/06 by Terry
	 */
	public int getAGMemberCountOrder(final Long memberId){
		Long order = (Long)getHibernateTemplate().execute(new HibernateCallback(){

			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer sb = new StringBuffer(64);
				sb.append("select count(*) from AGMemberCount agmc ")
				  .append("where agmc.count > ")
				  .append("(select c.count from AGMemberCount c ")
				  .append("where c.memberId = :memberId")
				  .append(")");
				Query q = getSession().createQuery(sb.toString());
				q.setParameter("memberId", memberId);
				return q.uniqueResult();
				
			}
			
		});
		return order == null ? 1 : (new Integer(order.toString())).intValue()+1;
	}
	public int getAGMemberCountOrder(final int count){
		Long order = (Long)getHibernateTemplate().execute(new HibernateCallback(){

			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer sb = new StringBuffer(64);
				sb.append("select count(*) from AGMemberCount agmc ")
				  .append("where agmc.count > :count");
				Query q = getSession().createQuery(sb.toString());
				q.setParameter("count", new Integer(count));
				return q.uniqueResult();
			}
			
		});
		return order == null ? 1 : (new Integer(order.toString())).intValue()+1;
	}
	/**
	 * add at 12/08/06
	 */
	public List listAGMemberTempByEmailSuffix(final String emailAddress){
		return getHibernateTemplate().executeFind(new HibernateCallback(){

			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				String hql = "from AGMemberTemp agmt where agmt.emailAddress like :emailAddress and agmt.mailCount < :mailCount" ;
				Query q = getSession().createQuery(hql);
				if(emailAddress != null){
					q.setParameter("emailAddress", "%"+emailAddress);
				}
				q.setParameter("mailCount", new Integer(4));
				q.setMaxResults(50);
				return q.list();
			}
			
		});
	}
	
	/**
	 * add at 12/13/06
	 * @return the count of member with at least one referrals
	 */
	public int getAGMemberCountWithReferrals(){
		Long number = (Long)getHibernateTemplate().execute(new HibernateCallback(){
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				//this more accurate exclude suspend member
//				Query q = getSession().createQuery("select count(distinct(memberId)) from AGMemberTree where status='N'");
				//this can not exclude suspend member
				Query q = getSession().createQuery("select count(*) from AGMemberCount agmc where agmc.count > 0");
				return q.uniqueResult();
			}
		});
		
		if(number == null){
			return 0;
		}
		return new Integer(number.toString()).intValue();
	}
	
	/**
	 * add at 12/28/06  by Terry
	 */
	public List listAGMemberByEmail(final String[] emailAddress){
		return listMemberByEmail(emailAddress, true);
	}
	public void suspendAGMember(List list){
		updateMemberStatus(getUserId(list,true), AGMember.MEMBER_STATUS_INACTIVE,true);
	}
	public void enableAGMember(List list){
		updateMemberStatus(getUserId(list,true), AGMember.MEMBER_STATUS_ACTIVE,true);
	}
	
	public List listAGMemberTempByEmail(final String[] emailAddress){
		return listMemberByEmail(emailAddress, false);
	}
	public void suspendAGMemberTemp(List list){
		updateMemberStatus(getUserId(list,false), AGMemberTemp.DEFAULT_MEMBER_TEMP_STATUS_INACTIVE, false);
	}
	public void enableAGMemberTemp(List list){
		updateMemberStatus(getUserId(list,false), AGMemberTemp.DEFAULT_MEMBER_TEMP_STATUS_ACTIVE, false);
	}

//	private Long[] getMemberId(List list,boolean bMember){
//		if(list == null || list.size() < 1){
//			return null;
//		}	
//		Long[] ids = new Long[list.size()];
//		int i = 0;
//		if(bMember){
//			for(Iterator it = list.iterator(); it.hasNext();){
//				ids[i++] = ((AGMember)it.next()).getMemberId();
//			}
//		}
//		else{
//			for(Iterator it = list.iterator(); it.hasNext();){
//				ids[i++] = ((AGMemberTemp)it.next()).getMemberId();
//			}
//		}
//
//		return ids;
//	}
	
	private String[] getUserId(List list,boolean bMember){
		if(list == null || list.size() < 1){
			return null;
		}	
		String[] ids = new String[list.size()];
		int i = 0;
		if(bMember){
			for(Iterator it = list.iterator(); it.hasNext();){
				ids[i++] = ((AGMember)it.next()).getUserId();
			}
		}
		else{
			for(Iterator it = list.iterator(); it.hasNext();){
				ids[i++] = ((AGMemberTemp)it.next()).getUserId();
			}
		}

		return ids;
	}
	
//	private String[] getMemberEmail(List list,boolean bMember){
//		if(list == null || list.size() < 1){
//			return null;
//		}	
//		String[] email = new String[list.size()];
//		int i = 0;
//		if(bMember){
//			for(Iterator it = list.iterator(); it.hasNext();){
//				email[i++] = ((AGMember)it.next()).getEmailAddress();
//			}
//		}
//		else{
//			for(Iterator it = list.iterator(); it.hasNext();){
//				email[i++] = ((AGMemberTemp)it.next()).getEmailAddress();
//			}
//		}
//
//		return email;
//	}
	private List listMemberByEmail(final String[] emailAddress, final boolean bMember){
		if(emailAddress == null || emailAddress.length < 1){
			return null;	
		}
		
		if(emailAddress.length <= Constants.BATCH_NUMBER){
			return batchListMemberByEmail(emailAddress, bMember);	
		}
		
		List list = new ArrayList();
		List l = null;
		int n = 0;
		while(n < emailAddress.length){
			int size = emailAddress.length - n > Constants.BATCH_NUMBER ? Constants.BATCH_NUMBER : emailAddress.length - n; 
			String[] temp = new String[size];
			for(int i = 0; i < Constants.BATCH_NUMBER && n < emailAddress.length; i++,n++){
				temp[i] = emailAddress[n];
			}
			l = batchListMemberByEmail(temp, bMember);
			if(l != null && l.size() > 0){
				list.addAll(l);	
			}
		}
		return list;
		
	}
	private List batchListMemberByEmail(final String[] emailAddress, final boolean bMember){
		
		if(emailAddress == null || emailAddress.length < 1){
			return null;	
		}
		
		return getHibernateTemplate().executeFind(new HibernateCallback(){

			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				// TODO list AGMember by email
				//build hql
				StringBuffer sb = new StringBuffer("from");
				if(bMember){
					sb.append(" AGMember m");
				}
				else{
					sb.append(" AGMemberTemp m");
				}
				sb.append(" where");
				String[] param = new String[emailAddress.length];
				String p = "";
				for(int i = 0; i < emailAddress.length; i++){
					p = "email"+i;
					param[i] = p;
					sb.append(" m.emailAddress= ").append(":").append(p).append(" or");
				}
				
				//filter hql
				String hql = sb.toString();
				if(hql.endsWith("or")){
					hql = hql.substring(0, hql.length()-3);
				}
				if(hql.endsWith("where")){
					return null;
				}
				
				//set parameters
				Query q = getSession().createQuery(hql);
				for(int i = 0; i < emailAddress.length; i++){
					q.setParameter(param[i], emailAddress[i]);
				}
				return q.list();
			}
			
		});
	}
	
//	private void updateMemberStatus(final String[] emailAddress,final String status,final boolean bMember){
//		
//		if(emailAddress == null || emailAddress.length < 1){
//			return;
//		}
//		
//		Connection con = null;
//		Statement stmt = null;
//		
//		try{
//			con = getSession().connection();
//			stmt = con.createStatement();
//			
//			//build hql
//			StringBuffer sb = new StringBuffer("update");
//			if(bMember){
//				sb.append(" AG_Member m");
//			}
//			else{
//				sb.append(" AG_M_Temp m");
//			}
//			sb.append(" set m.status='").append(status).append("' where");
//						
//			
//			for(int i = 0; i < emailAddress.length; i++){
//				sb.append(" AES_DECRYPT(m.emailAddress, 'JC2aadv11')='").append(emailAddress[i]).append("' or");
//			}
//			
//			//filter sql
//			String sql = sb.toString();
//			if(sql.endsWith("or")){
//				sql = sql.substring(0, sql.length()-3);
//			}
//			if(sql.endsWith("where")){
//				return ;
//			}				
//			stmt.executeUpdate(sql);
//			
//		}
//		catch(Exception e){
//			throw new HibernateException(e);
//		}
//		finally{
//			try {
//				getSession().flush();
//				if(stmt != null){
//					stmt.close();
//				}
//				
//			} catch (SQLException e) {
//				throw new HibernateException(e);
//			}
//		}
//	}
//	
	public void updateMemberStatus(final String[] ids,final String status,final boolean bMember){
		if(ids == null || ids.length < 1){
			return;
		}
		if(ids.length <= Constants.BATCH_NUMBER){
			batchUpdateMemberStatus(ids, status, bMember);	
			return;
		}
		
		int n = 0;
		while(n < ids.length){
			int size = ids.length - n > Constants.BATCH_NUMBER ? Constants.BATCH_NUMBER : ids.length - n;
			String[] temp = new String[size];
			for(int i = 0; i < Constants.BATCH_NUMBER && n < ids.length; i++,n++){
				temp[i] = ids[n];
			}
			batchUpdateMemberStatus(temp, status, bMember);
		}
		
	}
	
	private void batchUpdateMemberStatus(final String[] ids,final String status,final boolean bMember){
		
		if(ids == null || ids.length < 1){
			return;
		}
		
		Connection con = null;
		Statement stmt = null;
		
		try{
			con = getSession().connection();
			stmt = con.createStatement();
			
			//build hql
			StringBuffer sb = new StringBuffer("update");
			if(bMember){
				sb.append(" AG_Member m");
			}
			else{
				sb.append(" AG_M_Temp m");
			}
			sb.append(" set m.status='").append(status).append("' where");
						
			
			for(int i = 0; i < ids.length; i++){
				sb.append(" m.userId='").append(ids[i]).append("' or");
			}
			
			//filter sql
			String sql = sb.toString();
			if(sql.endsWith("or")){
				sql = sql.substring(0, sql.length()-3);
			}
			if(sql.endsWith("where")){
				return ;
			}				
			stmt.executeUpdate(sql);
			
		}
		catch(Exception e){
			throw new HibernateException(e);
		}
		finally{
			try {
				getSession().flush();
				if(stmt != null){
					stmt.close();
				}
				
			} catch (SQLException e) {
				throw new HibernateException(e);
			}
		}
	}
	
//	public void updateMemberStatus(final Long[] ids,final String status,final boolean bMember){
//		if(ids == null || ids.length < 1){
//			return;
//		}
//		if(ids.length <= Constants.BATCH_NUMBER){
//			batchUpdateMemberStatus(ids, status, bMember);	
//			return;
//		}
//		
//		int n = 0;
//		while(n < ids.length){
//			int size = ids.length - n > Constants.BATCH_NUMBER ? Constants.BATCH_NUMBER : ids.length - n;
//			Long[] temp = new Long[size];
//			for(int i = 0; i < Constants.BATCH_NUMBER && n < ids.length; i++,n++){
//				temp[i] = ids[n];
//			}
//			batchUpdateMemberStatus(temp, status, bMember);
//		}
//		
//	}
//	
//	private void batchUpdateMemberStatus(final Long[] ids,final String status,final boolean bMember){
//		
//		if(ids == null || ids.length < 1){
//			return;
//		}
//		
//		Connection con = null;
//		Statement stmt = null;
//		
//		try{
//			con = getSession().connection();
//			stmt = con.createStatement();
//			
//			//build hql
//			StringBuffer sb = new StringBuffer("update");
//			if(bMember){
//				sb.append(" AG_Member m");
//			}
//			else{
//				sb.append(" AG_M_Temp m");
//			}
//			sb.append(" set m.status='").append(status).append("' where");
//						
//			
//			for(int i = 0; i < ids.length; i++){
//				sb.append(" m.memberId=").append(ids[i]).append(" or");
//			}
//			
//			//filter sql
//			String sql = sb.toString();
//			if(sql.endsWith("or")){
//				sql = sql.substring(0, sql.length()-3);
//			}
//			if(sql.endsWith("where")){
//				return ;
//			}				
//			stmt.executeUpdate(sql);
//			
//		}
//		catch(Exception e){
//			throw new HibernateException(e);
//		}
//		finally{
//			try {
//				getSession().flush();
//				if(stmt != null){
//					stmt.close();
//				}
//				
//			} catch (SQLException e) {
//				throw new HibernateException(e);
//			}
//		}
//	}
	
	public List listAgMember(MemberQueryForm mqf)
	{
		List paramList = new ArrayList();
		String sql = getQuerySQL(mqf,paramList);
		String countSql = "select count(*) " + sql;
		Query q = getSession().createQuery(sql);
		Object[] params = paramList.toArray();
		int pageSize = mqf.getPageSize().intValue();
		int pageNum = mqf.getPageNum().intValue();

		if (params != null)
		{
			for (int i = 0; i < params.length; i++)
			{
				q.setParameter(i, params[i]);
			}
		}
		if (pageSize != 0)
		{
			q.setMaxResults(pageSize);
			q.setFirstResult(pageNum * pageSize);
		}
		
		int count = getAGMemberNumber(countSql, params);
		mqf.setTotalResult(count);

		return q.list();
	}
	
	private String getQuerySQL(MemberQueryForm mqf,List params)
	{
		String sql = "from "+mqf.getMemberType()+" agm where 1=1";
		String memberCodeOp = mqf.getMemberCodeOp();
		String memberCode = mqf.getMemberCode();
		if (StringUtil.isNotEmpty(memberCode))
		{
			sql = sql
					+ " and agm.memberCode "
					+ concat(memberCodeOp, memberCode.toUpperCase(), params);
		}
		String emailAddress = mqf.getEmailAddress();
		String emailAddressOp = mqf.getEmailAddressOp();
		if (StringUtil.isNotEmpty(emailAddress))
		{
			sql = sql
					+ " and agm.emailAddress "
					+ concat(emailAddressOp, emailAddress.toLowerCase(),
							params);
		}

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String timezone = mqf.getTimezone();
		String beginDateStr = mqf.getBeginDate();
		String endDateStr = mqf.getEndDate();
		df.setTimeZone(TimeZone.getTimeZone(timezone));
		Date beginDate = null;
		Date endDate = null;
		if (StringUtil.isNotEmpty(beginDateStr))
		{
			try
			{
				beginDate = df.parse(beginDateStr);
				sql = sql + " and agm.createDate > ?";
				params.add(beginDate);
			}
			catch (ParseException e)
			{
				throw new DateFormatInvalidException();
			}
		}
		if (StringUtil.isNotEmpty(endDateStr))
		{
			try
			{
				endDate = df.parse(endDateStr);
				sql = sql + " and agm.createDate <= ?";
				params.add(endDate);
			}
			catch (ParseException e)
			{
				throw new DateFormatInvalidException();
			}
		}

		String country = mqf.getCountry();
		if (StringUtil.isNotEmpty(country))
		{
			sql = sql + " and agm.country = ?";
			params.add(country);
		}

		String status = mqf.getStatus();
		if (StringUtil.isNotEmpty(status))
		{
			sql = sql + " and agm.status = ?";
			params.add(status);
		}

		return sql;
	}
	
	private String concat(String operate, Object limit, List list)
	{
		if(operate.equals("like"))
		{
			list.add("%"+limit+"%");
			return " "+operate+" ?";
		}
		else if(operate.equals("="))
		{
			list.add(limit);
			return " "+operate+" ?";
		}
		else
		{
			list.add(limit);
			return " "+operate+" ?";
		}
	}
	
	//add at 2007-03-06
	public int getAdminUserCount(){
		Long count = (Long)getHibernateTemplate().execute(new HibernateCallback(){

			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				
				String hql = "select count(*) from AGMember agm where agm.memberCode like :memberCode";
				Query q = getSession().createQuery(hql);
				q.setParameter("memberCode", Constants.ADMIN_USER_MEMBER_CODE_PRE+"%");
				return q.uniqueResult();
			}
			
		});
		
		if(count == null){
			return 0;
		}
		return Integer.parseInt(count.toString());
	}
	
	public List listAdminUser(final int pageNumber,final int pageSize){
		return getHibernateTemplate().executeFind(new HibernateCallback(){

			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				
				String hql = "from AGMember agm where agm.memberCode like :memberCode order by agm.memberId";
				Query q = getSession().createQuery(hql);
				q.setParameter("memberCode", Constants.ADMIN_USER_MEMBER_CODE_PRE+"%");
				if(pageSize>0)
				{
					q.setFirstResult((pageNumber-1)*pageSize);
					q.setMaxResults(pageSize);
				}
				return q.list();
			}
			
		});
		
	}
	
	//add at 2007-04-23
	public int getSurfTime(String tableName,Long memberId){
		
		int surfTime = 0;
		if(memberId == null || StringUtils.isBlank(tableName)){
			return surfTime;
		}
		
		try {
			
			StringBuffer sb = new StringBuffer();
			sb.append("select if(sum(second_) > ").append(Constants.MAX_SURF_TIME)
			  .append(",").append(Constants.MAX_SURF_TIME).append(",").append("sum(second_)").append(")")
			  .append(" from")
			  .append(" ").append(HibernateUtil.getViewbarDBName()).append(StringPool.PERIOD).append(tableName)
			  .append(" where memberId=?");
			
//			System.out.println(sb.toString());
			Connection conn = getSession().connection();
			PreparedStatement ps = conn.prepareStatement(sb.toString());
			ps.setLong(1, memberId.longValue());
			ps.execute();
			ResultSet rs = ps.getResultSet();
			while(rs != null && rs.next()){
				surfTime = rs.getInt(1);
			}
			
		} catch (Exception e) {
			throw new DataAccessException(e);
		}
		
		return surfTime;
	}
	public int getSurfTimeofReferral(String tableName,Long memberId,int selfTime,int startLevel,int endLevel){
		
		int surfTime = 0;
		if(memberId == null || StringUtils.isBlank(tableName)){
			return surfTime;
		}
		int standardTime = selfTime < Constants.MAX_SURF_TIME ? selfTime : Constants.MAX_SURF_TIME;
		try {
			
			StringBuffer sb = new StringBuffer();
			sb.append("select if(sum(vbst.second_) > ").append(standardTime)
			  .append(",").append(standardTime).append(",").append("sum(vbst.second_)").append(")")
			  .append(" from")
			  .append(" ").append(HibernateUtil.getViewbarDBName()).append(StringPool.PERIOD).append(tableName).append(" vbst")
			  .append(",").append(HibernateUtil.getAglocoDBName()) .append(StringPool.PERIOD).append(Constants.TB_AG_M_TREE).append(" agmt")
			  .append(" where vbst.memberId=agmt.descendantId")
			  .append(" and agmt.level_ >= ").append(startLevel)
			  .append(" and agmt.level_ <= ").append(endLevel)
			  .append(" and agmt.memberId=?")
			  .append(" group by vbst.memberId");
			
//			System.out.println(sb.toString());
			Connection conn = getSession().connection();
			PreparedStatement ps = conn.prepareStatement(sb.toString());
			ps.setLong(1, memberId.longValue());
			ps.execute();
			ResultSet rs = ps.getResultSet();
			while(rs != null && rs.next()){
				surfTime += rs.getInt(1);
			}
			
		} catch (Exception e) {
			throw new DataAccessException(e);
		}
		
		return surfTime;
	}

}
