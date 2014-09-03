package com.agloco.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.agloco.form.MemberQueryForm;
import com.agloco.model.AGMember;
import com.agloco.model.AGMemberAudit;
import com.agloco.model.AGMemberCount;
import com.agloco.model.AGMemberTemp;
import com.agloco.model.AGUser;
import com.agloco.service.impl.MemberServiceImpl;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.model.User;

/**
 * 
 * @author terry_zhao
 * @see MemberServiceImpl
 */
public interface MemberService extends BaseService {

	public void addAGMemberTemp(AGMemberTemp mt) throws PortalException;
	public void updateAGMemberTemp(AGMemberTemp mt);
	public void updateAGMemberTemp(String userId,AGMemberTemp mt)throws IllegalAccessException, InvocationTargetException;
	public AGMemberTemp getAGMemberTemp(Long memberId);
	public void deleteAgMemberTemp(AGMemberTemp mt);
	public List listAgMemberTemp();
	public List listAgMemberTemp(Date begin, Date end);
	public List listAgMemberTempSendEmail(int maxMailCount,int intervalDay,Calendar time);
	public List listNewAgMemberTempSendEmail(int maxMailCount,int interval,int ago,Calendar time);
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
	public void updateAGMember(AGMember member);
	public void updateAGMember(String operatorId,AGMember member) throws IllegalAccessException, InvocationTargetException;
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
	
	public String generateMemberCode(String className);

	/**
	 * add a descendant member under memberId
	 * 
	 * @param member
	 *          the parent member(the referral)
	 * @param descendant
	 *          the sub member
	 */
	public void addAGMember(AGMember member, AGMember descendant);

	/**
	 * get all ancestors of the given member
	 * 
	 * @param member
	 *          the given member
	 * @param status
	 *          criterion: ancestors' status
	 * @return
	 */
	public List getAncestors(AGMember member, String status);

	/**
	 * get the count of given member's children
	 * 
	 * @param member
	 *          the given member
	 * @param level
	 *          the max level
	 * @param children's
	 *          status
	 * @return
	 * @throws Exception
	 */
	public long getChildrenCount(AGMember member, long level, String status);

	/**
	 * get children count group by level, return a list like: {[1, 5], [2, 14],
	 * ... ... [5, 69]}
	 * 
	 * @param member
	 *          the given member
	 * @param children's
	 *          status
	 * @return
	 */
	public List getChildrenCountByLevel(AGMember member, long level, String status);

	/**
	 * get a partial subtree of the given member
	 * 
	 * @param member
	 *          the given member, use the follow componding criterions:
	 *          <li>memberId: parent's id, mandatory</li>
	 * @param level
	 *          the scope of the subtree, if <= 0, return max-scope subtree else
	 *          return 1~level subtree
	 * @param status
	 *          children's status criteria
	 * @return
	 * @throws Exception
	 */
	public List getChildren(AGMember member, long level, String status);

	/**
	 * get level-subtree of the given member
	 * 
	 * @param member
	 *          the given member, use the follow componding criterions:
	 *          <li>memberId: parent's id, mandatory</li>
	 * @param level
	 *          the level of the children, must >= 0
	 * @param status
	 *          children's status criteria
	 * @return
	 * @throws Exception
	 */
	public List getChildrenOfLevel(AGMember member, long level, String status);

	public com.liferay.portal.model.User addUserMemberTemp(
			String companyId,
			boolean autoUserId, String userId, boolean autoPassword,
			String password1, String password2,
			boolean passwordReset, String emailAddress,
			Locale locale, String firstName,
			String middleName, String lastName,
			String nickName, String prefixId,
			String suffixId, boolean male, int birthdayMonth,
			int birthdayDay, int birthdayYear, String jobTitle,
			String organizationId, String locationId,
			boolean sendEmail,AGMemberTemp agMemberTemp)
			throws com.liferay.portal.PortalException, com.liferay.portal.SystemException;
	
	
	public AGMember updateAgreedToTermsOfUse(
			String userId, boolean agreedToTermsOfUse,String password)
		throws PortalException, SystemException;
	
	public AGMember updateAgreedToTermsOfUse(
			String userId, boolean agreedToTermsOfUse)
		throws PortalException, SystemException;
	
	public void getInformation(String forgetType, String forgetTypeValue, Locale locale) 
		throws PortalException, SystemException;

	public AGMember authenticate(String login,String password) throws Exception;
	
	public boolean authenticateForJAAS(String userId, String password) throws Exception;
	
	/*** member audit **/
	public void addAGMemberAudit(AGMemberAudit ma);
	public void deleteAGMemberAudit(AGMemberAudit ma);
	public AGMemberAudit getAGMemberAudit(Long auditId);
	public void updateAGMemberAudit(AGMemberAudit ma);
	public List listAGMemberAuditByUserId(String userId);
	public List listAGMemberAuditByMemberCode(String memberCode);
	public List listAGMemberAuditByEmailAddress(String emailAddress);
	
	public User importMemberFromExcel(
			String companyId, boolean autoUserId,
			String userId, boolean autoPassword, String password1,
			String password2, boolean passwordReset, String emailAddress,
			Locale locale, String firstName, String middleName, String lastName,
			String nickName, String prefixId, String suffixId, boolean male,
			int birthdayMonth, int birthdayDay, int birthdayYear,
			String jobTitle, String organizationId, String locationId,
			boolean sendEmail,AGMemberTemp agMemberTemp)throws PortalException, SystemException;


	public String disableMemberByEmail(String emailAddress);
	
	
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
	 * @return list 0f the property:count
	 */
	public List listAGMemberCountNumber();
	public int getAGMemberCountNumber();
	public List listAGMemberCount(int pageSize,int pageNumber);
	/**
	 * 
	 * @param list:AGMemberCount list
	 */
	public void updateAGMemberCountTask(List list,Long level,String status);
	
	/**
	 * add at 12/06/06
	 */
	/**
	 * return the total active member number
	 */
	public int getAGMemberNumber();

	
	/**
	 * add at 2006-12-28 by Erick Kong
	 * return the total member number by sql
	 */
	public int getAGMemberNumber(String sql, Object[] params);
	/**
	 * return a list of AGMember
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
	 * add at 12/28/06 
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
	public com.liferay.portal.model.User addAdminUserMember(
			String companyId,
			boolean autoUserId, String userId, boolean autoPassword,
			String password1, String password2,
			boolean passwordReset, String emailAddress,
			Locale locale, String firstName,
			String middleName, String lastName,
			String nickName, String prefixId,
			String suffixId, boolean male, int birthdayMonth,
			int birthdayDay, int birthdayYear, String jobTitle,
			String organizationId, String locationId,
			boolean sendEmail,AGMember agMember)
			throws com.liferay.portal.PortalException, com.liferay.portal.SystemException;
	
	/**
	 * 
	 * @param member
	 * @param user reserved parameter,now it is no use, which may be used in future.
	 * @throws SystemException
	 */
	public void updateAdminUserMember(AGMember member,User user) throws SystemException;
	public int getAdminUserCount();
	public List listAdminUser(int pageNumber,int pageSize);
	
	//add at 2007-04-23
	public int getSurfTime(String tableName,Long memberId);
	public int getSurfTimeofReferral(String tableName,Long memberId,int selfTime,int startLevel,int endLevel);
}
