package com.agloco.service.dao;

import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.agloco.form.MemberQueryForm;
import com.agloco.model.AGMember;
import com.agloco.model.AGMemberAudit;
import com.agloco.model.AGMemberCount;
import com.agloco.model.AGMemberTemp;
import com.agloco.model.AGUser;
import com.agloco.service.dao.hibernate.MemberDaoHibernate;

/**
 * 
 * @author terry_zhao
 * @see MemberDaoHibernate	
 */
public interface MemberDao{
	
	public void addAGMemberTemp(AGMemberTemp mt);
	public void updateAGMemberTemp(AGMemberTemp mt);//add at 2007-03-07
	public AGMemberTemp getAGMemberTemp(Long memberId);
	public void deleteAgMemberTemp(AGMemberTemp mt);
	public List listAgMemberTemp();
	public List listAgMemberTemp(Date begin, Date end);
	public List listNewAgMemberTempSendEmail(int maxMailCount,int interval,int ago,Calendar time);
	public List listAgMemberTempSendEmail(int maxMailCount,int intervalDay,Calendar time);
	public AGMemberTemp getAGMemberTempByEmail(String emailAddress);
	public AGMemberTemp getAGMemberTempByCode(String memberCode);
	/**
	 * 
	 * @param keyword may be emailAddress or memberCode
	 * @return
	 */
	public AGMemberTemp getAGMemberTempByBoth(String keyword);
	public AGMemberTemp getAGMemberTempByUserId(String userId);
	
	public void addAGMember(AGMember member);
	public void addAGMemberAdmin(AGMember member);
	public void updateAGMember(AGMember member);
	public AGMember getAGMember(Long memberId);
	public void deleteAgMember(AGMember member);
	public List listAgMember();
	public List listAgMember(MemberQueryForm mqf);
	public List listAgMember(String sql, Object[] params, int pageNum, int pageSize);
	public AGMember getAGMemberByEmail(String emailAddress);
	public AGMember getAGMemberByCode(String memberCode);
	//this method only use for the members we import form excel and not login
	public List listAgMemberNotLogin();
	/**
	 * 
	 * @param keyword may be emailAddress or memberCode
	 * @return
	 */
	public AGMember getAGMemberByBoth(String keyword);
	public AGMember getAGMemberByUserId(String userId);

	public void addAGUser(AGUser user);
	public void updateAGUser(AGUser user);
	public AGUser getAGUser(String userId);
	
	public void addAGMemberAudit(AGMemberAudit ma);
	public void addAGMemberAudit(String operatorId,Long memberId) throws IllegalAccessException, InvocationTargetException;
	public void addAGMemberAudit(String operatorId,AGMember member) throws IllegalAccessException, InvocationTargetException;
	public void addAGMemberTempAudit(String operatorId,AGMemberTemp agMemberTemp) throws IllegalAccessException, InvocationTargetException;
	public void deleteAGMemberAudit(AGMemberAudit ma);
	public AGMemberAudit getAGMemberAudit(Long auditId);
	public void updateAGMemberAudit(AGMemberAudit ma);
	public List listAGMemberAuditByUserId(String userId);
	public List listAGMemberAuditByMemberCode(String memberCode);
	public List listAGMemberAuditByEmailAddress(String emailAddress);
	
	/**
	 * 
	 * add at 12/05/06 by terry
	 */
	public AGMemberCount getAGMemberCount(Long memberId);
	public void addAGMemberCount(AGMemberCount agmc);
	public void updateAGMemberCount(AGMemberCount agmc,Long level,String status);
	/**
	 * 
	 * @return list of AGMemberCount
	 */
	public List listAGMemberCount();
	/**
	 * 
	 * @return list of the property:count
	 */
	public List listAGMemberCountNumber();
	public int getAGMemberCountNumber();
	public List listAGMemberCount(int pageSize,int pageNumber);
	
	/**
	 * add at 12/06/06
	 */
	
	/**
	 * return the total active member number
	 */
	public int getAGMemberNumber();

	/**
	 * add at 12/06/06 by Erick Kong
	 */
	public int getAGMemberNumber(String sql, Object[] params);

	/**
	 * return a list of active AGMember 
	 */
	public List listAGMemeber(int pageSize,int pageNumber);
	
	public void updateAGMemberCountTask(Long level,String status);
	/**
	 * 
	 * @param memberId
	 * @return the member count order(desc)
	 */
	public int getAGMemberCountOrder(Long memberId);
	/**
	 * add at 08/02/2007
	 * @param count
	 * @return
	 */
	public int getAGMemberCountOrder(int count);
	
	/**
	 * add at 12/08/06
	 */
	public List listAGMemberTempByEmailSuffix(String emailAddress);
	
	/**
	 * add at 12/13/06
	 * @return the count of member with at least one referrals
	 */
	public int getAGMemberCountWithReferrals();
	
	/**
	 * add at 12/28/06 by Terry
	 */
	public List listAGMemberByEmail(String[] emailAddress);
	public void suspendAGMember(List list);
	public void enableAGMember(List list);
	public List listAGMemberTempByEmail(String[] emailAddress);
	public void suspendAGMemberTemp(List list);
	public void enableAGMemberTemp(List list);
	/**
	 * modified at 12/02/2007
	 * @param ids userId
	 * @param status 
	 * @param bMember AGMember:true;AGMemberTemp:false
	 */
	public void updateMemberStatus(String[] ids,String status,boolean bMember);
	
	//add at 2007-03-06
	public int getAdminUserCount();
	public List listAdminUser(int pageNumber,int pageSize);
	
	//add at 2007-04-23
	public int getSurfTime(String tableName,Long memberId);
	public int getSurfTimeofReferral(String tableName,Long memberId,int selfTime,int startLevel,int endLevel);
}
