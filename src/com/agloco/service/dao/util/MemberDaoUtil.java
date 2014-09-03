package com.agloco.service.dao.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.context.ApplicationContext;

import com.agloco.form.MemberQueryForm;
import com.agloco.model.AGMember;
import com.agloco.model.AGMemberAudit;
import com.agloco.model.AGMemberCount;
import com.agloco.model.AGMemberTemp;
import com.agloco.model.AGUser;
import com.agloco.service.dao.MemberDao;
import com.liferay.portal.PortalException;
import com.liferay.portal.spring.util.SpringUtil;
/**
 * 
 * @author terry_zhao
 *
 */
public class MemberDaoUtil {
	
	private MemberDao memberDao;

	public static void addAGMemberTemp(AGMemberTemp mt) throws PortalException{
//		if(getAGMemberTempByEmail(mt.getEmailAddress()) != null || getAGMemberByEmail(mt.getEmailAddress()) != null){
//			throw new DuplicateUserEmailAddressException();
//		}
		getMemberDao().addAGMemberTemp(mt);
	}

	public static void updateAGMemberTemp(AGMemberTemp mt){
//		if(getAGMemberTempByEmail(mt.getEmailAddress()) != null){
//			throw new DuplicateEmailAddressException();
//		}
		getMemberDao().updateAGMemberTemp(mt);	
	}

	public static AGMemberTemp getAGMemberTemp(Long memberId){
		return getMemberDao().getAGMemberTemp(memberId);
	}

	public static void deleteAgMemberTemp(AGMemberTemp mt){
		getMemberDao().deleteAgMemberTemp(mt);
	}

	public static List listAgMemberTemp(){
		return getMemberDao().listAgMemberTemp();
	}

	public static List listAgMemberTemp(Date begin, Date end) {
		return getMemberDao().listAgMemberTemp(begin, end);
	}

	public static List listAgMemberTempSendEmail(int maxMailCount,int intervalDay,Calendar time){
		return getMemberDao().listAgMemberTempSendEmail(maxMailCount, intervalDay,time);
	}
	
	public static List listNewAgMemberTempSendEmail(int maxMailCount,int interval,int ago,Calendar time){
		return getMemberDao().listNewAgMemberTempSendEmail(maxMailCount, interval ,ago,time);
	}

	public static AGMemberTemp getAGMemberTempByEmail(String emailAddress){
		return getMemberDao().getAGMemberTempByEmail(emailAddress);
	}

	public static AGMemberTemp getAGMemberTempByCode(String memberCode){
		return getMemberDao().getAGMemberTempByCode(memberCode);
	}

	public static AGMemberTemp getAGMemberTempByBoth(String keyword){
		return getMemberDao().getAGMemberTempByBoth(keyword);
	}

	public static AGMemberTemp getAGMemberTempByUserId(String userId){
		return getMemberDao().getAGMemberTempByUserId(userId);
	}
	
	public static void addAGMember(AGMember member){
		getMemberDao().addAGMember(member);
	}

	public static void addAGMemberAdmin(AGMember member){
		getMemberDao().addAGMemberAdmin(member);
	}
	
	public static void updateAGMember(AGMember member){
		getMemberDao().updateAGMember(member);
	}

	public static AGMember getAGMember(Long memberId){
		return getMemberDao().getAGMember(memberId);
	}

	public static void deleteAgMember(AGMember member){
		getMemberDao().deleteAgMember(member);
	}

	public static List listAgMember(){
		return getMemberDao().listAgMember();
	}

	public static List listAgMember(MemberQueryForm mqf){
		return getMemberDao().listAgMember(mqf);
	}

	public static List listAgMember(String sql, Object[] params, int pageNum, int pageSize)
	{
		return getMemberDao().listAgMember(sql, params, pageNum, pageSize);
	}

	//this method only use for the members we import form excel and not login
	public static List listAgMemberNotLogin(){
		return getMemberDao().listAgMemberNotLogin();
	}

	public static AGMember getAGMemberByEmail(String emailAddress){
		return getMemberDao().getAGMemberByEmail(emailAddress);
	}

	public static AGMember getAGMemberByCode(String memberCode){
		return getMemberDao().getAGMemberByCode(memberCode);
	}

	public static AGMember getAGMemberByBoth(String keyword){
		return getMemberDao().getAGMemberByBoth(keyword);
	}

	public static AGMember getAGMemberByUserId(String userId){
		return getMemberDao().getAGMemberByUserId(userId);
	}

	public static void addAGUser(AGUser user){
		getMemberDao().addAGUser(user);
	}

	public static void updateAGUser(AGUser user){
		getMemberDao().updateAGUser(user);	
	}

	public static AGUser getAGUser(String userId){
		return getMemberDao().getAGUser(userId);
	}
	
	public static void addAGMemberAudit(AGMemberAudit ma){
		getMemberDao().addAGMemberAudit(ma);
	}
	
	public static void addAGMemberAudit(String operatorId,Long memberId) throws IllegalAccessException, InvocationTargetException{
		getMemberDao().addAGMemberAudit(operatorId, memberId);
	}

	public static void addAGMemberTempAudit(String operatorId,AGMemberTemp agMemberTemp) throws IllegalAccessException, InvocationTargetException{
		getMemberDao().addAGMemberTempAudit(operatorId, agMemberTemp);
	}
	
	public static void deleteAGMemberAudit(AGMemberAudit ma){
		getMemberDao().deleteAGMemberAudit(ma);
	}
	
	public static AGMemberAudit getAGMemberAudit(Long auditId){
		return getMemberDao().getAGMemberAudit(auditId);
	}
	
	public static void updateAGMemberAudit(AGMemberAudit ma){
		
	}
	
	public static List listAGMemberAuditByUserId(String userId){
		return getMemberDao().listAGMemberAuditByUserId(userId);
	}
	
	public static List listAGMemberAuditByMemberCode(String memberCode){
		return getMemberDao().listAGMemberAuditByMemberCode(memberCode);
	}
	
	public static List listAGMemberAuditByEmailAddress(String emailAddress){
		return getMemberDao().listAGMemberAuditByEmailAddress(emailAddress);
	}
	
	/**
	 * 
	 * add at 12/05/06 by terry
	 */
	public static AGMemberCount getAGMemberCount(Long memberId){
		return getMemberDao().getAGMemberCount(memberId);	
	}
	public static void addAGMemberCount(AGMemberCount agmc){
		getMemberDao().addAGMemberCount(agmc);
	}
	public static void updateAGMemberCount(AGMemberCount agmc,Long level,String status){
		getMemberDao().updateAGMemberCount(agmc, level, status);
	}
	/**
	 * 
	 * @return list 0f AGMemberCount
	 */
	public static List listAGMemberCount(){
		return getMemberDao().listAGMemberCount();
	}
	/**
	 * 
	 * @return list 0f the property:count
	 */
	public static List listAGMemberCountNumber(){
		return getMemberDao().listAGMemberCountNumber();
	}
	public static int getAGMemberCountNumber(){
		return getMemberDao().getAGMemberCountNumber();
	}
	public static List listAGMemberCount(int pageSize,int pageNumber){
		return getMemberDao().listAGMemberCount(pageSize, pageNumber);
	}
	
	/**
	 * add at 12/06/06
	 */
	
	/**
	 * return the total active member number
	 */
	public static int getAGMemberNumber(){
		return getMemberDao().getAGMemberNumber();
	}
	
	/**
	 * add at 12/06/06 by Erick Kong
	 */
	public static int getAGMemberNumber(String sql, Object[] params){
		return getMemberDao().getAGMemberNumber(sql,params);
	}
	
	/**
	 * return a list of active AGMember
	 */
	public static List listAGMemeber(int pageSize,int pageNumber){
		return getMemberDao().listAGMemeber(pageSize, pageNumber);
	}
	public static void updateAGMemberCountTask(Long level,String status){
		getMemberDao().updateAGMemberCountTask(level, status);
	}
	/**
	 * 
	 * @param memberId
	 * @return the member count order(desc)
	 */
	public static int getAGMemberCountOrder(Long memberId){
		return getMemberDao().getAGMemberCountOrder(memberId);
	}
	/**
	 * add at 08/02/2007
	 * @param count
	 * @return
	 */
	public static int getAGMemberCountOrder(int count){
		return getMemberDao().getAGMemberCountOrder(count);
	}
	
	/**
	 * add at 12/08/06
	 */
	public static List listAGMemberTempByEmailSuffix(String emailAddress){
		return getMemberDao().listAGMemberTempByEmailSuffix(emailAddress);
	}
	
	/**
	 * add at 12/13/06
	 * @return the count of member with at least one referrals
	 */
	public static int getAGMemberCountWithReferrals(){
		return getMemberDao().getAGMemberCountWithReferrals();
	}
	/**
	 * add at 12/28/06 
	 */
	public static List listAGMemberByEmail(String[] emailAddress){
		return getMemberDao().listAGMemberByEmail(emailAddress);
	}
	public static void suspendAGMember(List list){
		getMemberDao().suspendAGMember(list);
	}
	public static void enableAGMember(List list){
		getMemberDao().enableAGMember(list);
	}
	public static List listAGMemberTempByEmail(String[] emailAddress){
		return getMemberDao().listAGMemberTempByEmail(emailAddress);
	}
	public static void suspendAGMemberTemp(List list){
		getMemberDao().suspendAGMemberTemp(list);
	}
	public static void enableAGMemberTemp(List list){
		getMemberDao().enableAGMemberTemp(list);
	}
	/**
	 * modified at 12/02/2007
	 * @param ids userId
	 * @param status 
	 * @param bMember AGMember:true;AGMemberTemp:false
	 */
	public static void updateMemberStatus(String[] ids,String status,boolean bMember){
		getMemberDao().updateMemberStatus(ids, status, bMember);
	}
	
	//add at 2007-03-06
	public static int getAdminUserCount(){
		return getMemberDao().getAdminUserCount();
	}
	public static List listAdminUser(int pageNumber,int pageSize){
		return getMemberDao().listAdminUser(pageNumber, pageSize);
	}
	
	//add at 2007-04-23
	public static int getSurfTime(String tableName,Long memberId){
		return getMemberDao().getSurfTime(tableName, memberId);
	}
	public static int getSurfTimeofReferral(String tableName,Long memberId,int selfTime,int startLevel,int endLevel){
		return getMemberDao().getSurfTimeofReferral(tableName, memberId, selfTime, startLevel, endLevel);
	}
	
	
	public static MemberDao getMemberDao(){
		ApplicationContext ctx = SpringUtil.getContext();
		MemberDaoUtil util = (MemberDaoUtil) ctx.getBean(MemberDaoUtil.class.getName());
		return util.memberDao;
	}
	
	public void setMemberDao(MemberDao memberDao) {
		this.memberDao = memberDao;
	}
	
}
