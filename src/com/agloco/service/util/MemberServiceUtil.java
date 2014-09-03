package com.agloco.service.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.context.ApplicationContext;

import com.agloco.Constants;
import com.agloco.form.MemberQueryForm;
import com.agloco.model.AGMember;
import com.agloco.model.AGMemberAudit;
import com.agloco.model.AGMemberCount;
import com.agloco.model.AGMemberTemp;
import com.agloco.model.AGMemberTree;
import com.agloco.model.AGUser;
import com.agloco.service.MemberService;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.model.User;
import com.liferay.portal.spring.util.SpringUtil;
import com.liferay.portal.util.ClusterPool;
import com.liferay.util.StringPool;
import com.opensymphony.oscache.base.NeedsRefreshException;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;

/**
 * 
 * @author terry_zhao
 * 
 */
public class MemberServiceUtil
{

	private MemberService memberService;
	
	//add at 08/02/2007
	private static GeneralCacheAdministrator _cache = ClusterPool.getCache();
	public final static String CACHE_MEMBER_COUNT_WITH_REFERRALS = "member.count.with.referrals";
	public static final String GROUP_NAME = MemberServiceUtil.class.getName();
	public static final String[] GROUP_NAME_ARRAY = new String[] {GROUP_NAME};

	public static void addAGMemberTemp(AGMemberTemp mt) throws PortalException
	{
		getMemberService().addAGMemberTemp(mt);
	}

	public static void updateAGMemberTemp(AGMemberTemp mt)
	{
		getMemberService().updateAGMemberTemp(mt);
	}

	public static void updateAGMemberTemp(String userId,AGMemberTemp mt)throws IllegalAccessException, InvocationTargetException
	{
		getMemberService().updateAGMemberTemp(userId,mt);
	}
	
	public static AGMemberTemp getAGMemberTemp(Long memberId)
	{
		return getMemberService().getAGMemberTemp(memberId);
	}

	public static void deleteAgMemberTemp(AGMemberTemp mt)
	{
		getMemberService().deleteAgMemberTemp(mt);
	}

	public static List listAgMemberTemp()
	{
		return getMemberService().listAgMemberTemp();
	}

	public static List listAgMemberTemp(Date begin, Date end)
	{
		return getMemberService().listAgMemberTemp(begin, end);
	}

	public static List listAgMemberTempSendEmail(int maxMailCount,
			int intervalDay,Calendar time)
	{
		return getMemberService().listAgMemberTempSendEmail(maxMailCount,
				intervalDay,time);
	}
	
	public static List listNewAgMemberTempSendEmail(int maxMailCount,
			int interval,int ago,Calendar time)
	{
		return getMemberService().listNewAgMemberTempSendEmail(maxMailCount,
				interval,ago,time);
	}

	public static AGMemberTemp getAGMemberTempByEmail(String emailAddress)
	{
		return getMemberService().getAGMemberTempByEmail(emailAddress);
	}

	public static AGMemberTemp getAGMemberTempByCode(String memberCode)
	{
		return getMemberService().getAGMemberTempByCode(memberCode);
	}

	/**
	 * 
	 * @param keyword
	 *            may be emailAddress or memberCode
	 * @return
	 */
	public static AGMemberTemp getAGMemberTempByBoth(String keyword)
	{
		return getMemberService().getAGMemberTempByBoth(keyword);
	}

	public static AGMemberTemp getAGMemberTempByUserId(String userId)
	{
		return getMemberService().getAGMemberTempByUserId(userId);
	}

	public static void addAGMember(AGMember member)
	{
		getMemberService().addAGMember(member);
	}

	private static void updateAGMember(AGMember member)
	{
		getMemberService().updateAGMember(member);
	}

	public static void updateAGMember(String operatorId, AGMember member)
			throws IllegalAccessException, InvocationTargetException
	{
		getMemberService().updateAGMember(operatorId, member);
	}

	public static AGMember getAGMember(Long memberId)
	{
		return getMemberService().getAGMember(memberId);

	}

	public static void deleteAgMember(AGMember member)
	{
		getMemberService().deleteAgMember(member);
	}

	public static List listAgMember()
	{
		return getMemberService().listAgMember();
	}

	public static List listAgMember(String sql, Object[] params, int pageNum, int pageSize)
	{
		return getMemberService().listAgMember(sql, params, pageNum, pageSize);
	}

	public static List listAgMember(MemberQueryForm mqf)
	{
		return getMemberService().listAgMember(mqf);
	}

	// this method only use for the members we import form excel and not login
	public static List listAgMemberNotLogin()
	{
		return getMemberService().listAgMemberNotLogin();
	}

	public static AGMember getAGMemberByEmail(String emailAddress)
	{
		return getMemberService().getAGMemberByEmail(emailAddress);
	}

	public static AGMember getAGMemberByCode(String memberCode)
	{
		return getMemberService().getAGMemberByCode(memberCode);
	}

	/**
	 * 
	 * @param keyword
	 *            may be emailAddress or memberCode
	 * @return
	 */
	public static AGMember getAGMemberByBoth(String keyword)
	{
		return getMemberService().getAGMemberByBoth(keyword);
	}

	public static AGMember getAGMemberByUserId(String userId)
	{
		return getMemberService().getAGMemberByUserId(userId);
	}

	public static void addAGUser(AGUser user)
	{
		getMemberService().addAGUser(user);
	}

	public static void updateAGUser(AGUser user)
	{
		getMemberService().updateAGUser(user);
	}

	public static AGUser getAGUser(String userId)
	{
		return getMemberService().getAGUser(userId);

	}

	public static String generateMemberCode(String className)
	{
		return getMemberService().generateMemberCode(className);
	}

	public static AGMember authenticate(String login, String password)
			throws Exception
	{

		return getMemberService().authenticate(login, password);
	}

	public static boolean authenticateForJAAS(String userId, String encPwd)
			throws Exception
	{
		return getMemberService().authenticateForJAAS(userId, encPwd);
	}

	/**
	 * add a descendant member under memberId
	 * 
	 * @param member
	 *            the parent member(the referral)
	 * @param descendant
	 *            the sub member
	 */
	public static void addAGMember(AGMember member, AGMember descendant)
	{
		getMemberService().addAGMember(member, descendant);
	}

	/**
	 * get all ancestors of the given member
	 * 
	 * @param member
	 *            the given member
	 * @param status
	 *            criterion: ancestors' status
	 * @return
	 */
	public static List getAncestors(AGMember member, String status)
	{
		return getMemberService().getAncestors(member, status);
	}

	/**
	 * get the count of given member's children
	 * 
	 * @param member
	 *            the given member
	 * @param level
	 *            the max level
	 * @param children's
	 *            status
	 * @return
	 * @throws Exception
	 */
	public static long getChildrenCount(AGMember member, long level,
			String status)
	{
		return getMemberService().getChildrenCount(member, level, status);
	}

	public static long getChildrenCount(Long memberId)
	{
		AGMember member = new AGMember();
		member.setMemberId(memberId);
		return getChildrenCount(member, -1, AGMemberTree.STRING_AGMEMBER_STATUS_N);
	}

	/**
	 * get children count group by level, return a list like: {[1, 5], [2, 14],
	 * ... ... [5, 69]}
	 * 
	 * @param member
	 *            the given member
	 * @param children's
	 *            status
	 * @return
	 */
	public static List getChildrenCountByLevel(AGMember member, String status)
	{
		return getMemberService().getChildrenCountByLevel(member,
				Constants.DEF_REFERRAL_TREE_CALCLEVEL, status);
	}

	public static List getChildrenCountByLevel(AGMember member, long level,
			String status)
	{
		return getMemberService()
				.getChildrenCountByLevel(member, level, status);
	}

	/**
	 * get a partial subtree of the given member
	 * 
	 * @param member
	 *            the given member, use the follow componding criterions:
	 *            <li>memberId: parent's id, mandatory</li>
	 * @param level
	 *            the scope of the subtree, if <= 0, return max-scope subtree
	 *            else return 1~level subtree
	 * @param status
	 *            children's status criteria
	 * @return
	 * @throws Exception
	 */
	public static List getChildren(AGMember member, long level, String status)
	{
		return getMemberService().getChildren(member, level, status);
	}

	public static List getChildren(Long memberId)
	{
		AGMember member = new AGMember();
		member.setMemberId(memberId);
		return getChildren(member, -1, AGMemberTree.STRING_AGMEMBER_STATUS_N);
	}

	/**
	 * get level-subtree of the given member
	 * 
	 * @param member
	 *            the given member, use the follow componding criterions:
	 *            <li>memberId: parent's id, mandatory</li>
	 * @param level
	 *            the level of the children, must >= 0
	 * @param status
	 *            children's status criteria
	 * @return
	 * @throws Exception
	 */
	public static List getChildrenOfLevel(AGMember member, long level,
			String status)
	{
		return getMemberService().getChildrenOfLevel(member, level, status);
	}

	public static com.liferay.portal.model.User addUserMemberTemp(
			java.lang.String companyId, boolean autoUserId,
			java.lang.String userId, boolean autoPassword,
			java.lang.String password1, java.lang.String password2,
			boolean passwordReset, java.lang.String emailAddress,
			java.util.Locale locale, java.lang.String firstName,
			java.lang.String middleName, java.lang.String lastName,
			java.lang.String nickName, java.lang.String prefixId,
			java.lang.String suffixId, boolean male, int birthdayMonth,
			int birthdayDay, int birthdayYear, java.lang.String jobTitle,
			java.lang.String organizationId, java.lang.String locationId,
			boolean sendEmail, AGMemberTemp agMemberTemp)
			throws com.liferay.portal.PortalException,
			com.liferay.portal.SystemException
	{

		return getMemberService().addUserMemberTemp(companyId, autoUserId,
				userId, autoPassword, password1, password2, passwordReset,
				emailAddress, locale, firstName, middleName, lastName,
				nickName, prefixId, suffixId, male, birthdayMonth, birthdayDay,
				birthdayYear, jobTitle, organizationId, locationId, sendEmail,
				agMemberTemp);
	}

	public static AGMember updateAgreedToTermsOfUse(
			java.lang.String userId, boolean agreedToTermsOfUse, String password)
			throws com.liferay.portal.PortalException,
			com.liferay.portal.SystemException
	{
		return getMemberService().updateAgreedToTermsOfUse(userId,
				agreedToTermsOfUse, password);
	}

	public static AGMember updateAgreedToTermsOfUse(
			java.lang.String userId, boolean agreedToTermsOfUse)
			throws com.liferay.portal.PortalException,
			com.liferay.portal.SystemException
	{
		return getMemberService().updateAgreedToTermsOfUse(userId,
				agreedToTermsOfUse);
	}

	public static void getInfo(String forgetType, String forgetTypeValue, Locale locale)
			throws com.liferay.portal.PortalException,
			com.liferay.portal.SystemException
	{
		getMemberService().getInformation(forgetType,forgetTypeValue, locale);
	}

	public static void addAGMemberAudit(AGMemberAudit ma)
	{
		getMemberService().addAGMemberAudit(ma);
	}

	public static void deleteAGMemberAudit(AGMemberAudit ma)
	{
		getMemberService().deleteAGMemberAudit(ma);
	}

	public static AGMemberAudit getAGMemberAudit(Long auditId)
	{
		return getMemberService().getAGMemberAudit(auditId);
	}

	public static void updateAGMemberAudit(AGMemberAudit ma)
	{

	}

	public static List listAGMemberAuditByUserId(String userId)
	{
		return getMemberService().listAGMemberAuditByUserId(userId);
	}

	public static List listAGMemberAuditByMemberCode(String memberCode)
	{
		return getMemberService().listAGMemberAuditByMemberCode(memberCode);
	}

	public static List listAGMemberAuditByEmailAddress(String emailAddress)
	{
		return getMemberService().listAGMemberAuditByEmailAddress(emailAddress);
	}

	public static User importMemberFromExcel(String companyId,
			boolean autoUserId, String userId, boolean autoPassword,
			String password1, String password2, boolean passwordReset,
			String emailAddress, Locale locale, String firstName,
			String middleName, String lastName, String nickName,
			String prefixId, String suffixId, boolean male, int birthdayMonth,
			int birthdayDay, int birthdayYear, String jobTitle,
			String organizationId, String locationId, boolean sendEmail,
			AGMemberTemp agMemberTemp) throws PortalException, SystemException
	{

		return getMemberService().importMemberFromExcel(companyId, autoUserId,
				userId, autoPassword, password1, password2, passwordReset,
				emailAddress, locale, firstName, middleName, lastName,
				nickName, prefixId, suffixId, male, birthdayMonth, birthdayDay,
				birthdayYear, jobTitle, organizationId, locationId, sendEmail,
				agMemberTemp);
	}
	
	public static String disableMemberByEmail(String emailAddress){
		return getMemberService().disableMemberByEmail(emailAddress);
	}

	/**
	 * 
	 * add at 12/05/06 by terry
	 */
	public static AGMemberCount getAGMemberCount(Long memberId){
		return getMemberService().getAGMemberCount(memberId);	
	}
	public static void addAGMemberCount(AGMemberCount agmc){
		getMemberService().addAGMemberCount(agmc);
	}
	public static void updateAGMemberCount(AGMemberCount agmc,Long level,String status){
		getMemberService().updateAGMemberCount(agmc, level, status);
	}
	/**
	 * 
	 * @return list of AGMemberCount
	 */
	public static List listAGMemberCount(){
		return getMemberService().listAGMemberCount();
	}
	/**
	 * 
	 * @return list of the property:count
	 */
	public static List listAGMemberCountNumber(){
		return getMemberService().listAGMemberCountNumber();
	}
	public static int getAGMemberCountNumber(){
		return getMemberService().getAGMemberCountNumber();
	}
	public static List listAGMemberCount(int pageSize,int pageNumber){
		return getMemberService().listAGMemberCount(pageSize, pageNumber);
	}
	/**
	 * 
	 * @param list:AGMemberCount list
	 */
	public static void updateAGMemberCountTask(List list,Long level,String status){
		getMemberService().updateAGMemberCountTask(list, level, status);
	}
	/**
	 * 
	 * @param list:AGMemberCount list
	 */
	public static void updateAGMemberCountTask(List list){
		getMemberService().updateAGMemberCountTask(list,new Long(Constants.DEF_REFERRAL_TREE_CALCLEVEL), AGMemberTree.STRING_AGMEMBER_STATUS_N);
	}
	
	/**
	 * add at 12/06/06
	 */
	
	/**
	 * return the total active member number
	 */
	public static int getAGMemberNumber(){
		return getMemberService().getAGMemberNumber();
	}
	
	public static int getAGMemberNumber(String sql, Object[] params){
		return getMemberService().getAGMemberNumber(sql, params);
	}
	
	/**
	 * return a list of active AGMember
	 */
	public static List listAGMemeber(int pageSize,int pageNumber){
		return getMemberService().listAGMemeber(pageSize, pageNumber);
	}
	public static void updateAGMemberCountTask(Long level,String status){
		getMemberService().updateAGMemberCountTask(level, status);
	}
	public static void updateAGMemberCountTask(){
		getMemberService().updateAGMemberCountTask(new Long(Constants.DEF_REFERRAL_TREE_CALCLEVEL), AGMemberTree.STRING_AGMEMBER_STATUS_N);
	}
	/**
	 * 
	 * @param memberId
	 * @return the member count order(desc)
	 */
	public static int getAGMemberCountOrder(Long memberId){
		return getMemberService().getAGMemberCountOrder(memberId);
	}
	/**
	 * add at 08/02/2007
	 * @param count
	 * @return
	 */
	public static int getAGMemberCountOrder(int count){
		return getMemberService().getAGMemberCountOrder(count);
	}
	/**
	 * add at 12/08/06
	 */
	public static List listAGMemberTempByEmailSuffix(String emailAddress){
		return getMemberService().listAGMemberTempByEmailSuffix(emailAddress);
	}
	
	/**
	 * add at 12/13/06
	 * @return the count of member with at least one referrals
	 */
	public static int getAGMemberCountWithReferrals(){
		
		String key = _encodeKey(CACHE_MEMBER_COUNT_WITH_REFERRALS);
		Integer it = null;
		int count = 0;
		try{
			it = (Integer)_cache.getFromCache(key);
			if(it != null){
				count = it.intValue();
			}
		}
		catch(NeedsRefreshException e){
			count = getMemberService().getAGMemberCountWithReferrals();
			it = new Integer(count);
			_cache.putInCache(key,it, GROUP_NAME_ARRAY);
		}
		finally{
			if(it == null){
				_cache.cancelUpdate(key);
			}
		}
		return count;
	}
	

	/**
	 * add at 12/28/06 
	 */
	public static List listAGMemberByEmail(String[] emailAddress){
		return getMemberService().listAGMemberByEmail(emailAddress);
	}
	public static void suspendAGMember(List list){
		getMemberService().suspendAGMember(list);
	}
	public static void enableAGMember(List list){
		getMemberService().enableAGMember(list);
	}
	public static List listAGMemberTempByEmail(String[] emailAddress){
		return getMemberService().listAGMemberTempByEmail(emailAddress);
	}
	public static void suspendAGMemberTemp(List list){
		getMemberService().suspendAGMemberTemp(list);
	}
	public static void enableAGMemberTemp(List list){
		getMemberService().enableAGMemberTemp(list);
	}
	/**
	 * modified at 12/02/2007
	 * @param ids userId
	 * @param status 
	 * @param bMember AGMember:true;AGMemberTemp:false
	 */
	public static void updateMemberStatus(String[] ids,String status,boolean bMember){
		getMemberService().updateMemberStatus(ids, status, bMember);
	}
	
	/**
	 * add at 08/02/2007
	 *
	 */
	public static void clearMemberServiceUtilPool() {
		_cache.flushGroup(GROUP_NAME);
	}

	public static void clearMemberServiceUtilPool(String key) {
		key = _encodeKey(key);
		_cache.flushEntry(key);
	}	
	
	private static String _encodeKey(String key) {
		return GROUP_NAME + StringPool.POUND + key;
	}
	
	//add at 2007-03-06
	public static com.liferay.portal.model.User addAdminUserMember(
			java.lang.String companyId, boolean autoUserId,
			java.lang.String userId, boolean autoPassword,
			java.lang.String password1, java.lang.String password2,
			boolean passwordReset, java.lang.String emailAddress,
			java.util.Locale locale, java.lang.String firstName,
			java.lang.String middleName, java.lang.String lastName,
			java.lang.String nickName, java.lang.String prefixId,
			java.lang.String suffixId, boolean male, int birthdayMonth,
			int birthdayDay, int birthdayYear, java.lang.String jobTitle,
			java.lang.String organizationId, java.lang.String locationId,
			boolean sendEmail, AGMember agMember)
			throws com.liferay.portal.PortalException,
			com.liferay.portal.SystemException
	{

		return getMemberService().addAdminUserMember(companyId, autoUserId,
				userId, autoPassword, password1, password2, passwordReset,
				emailAddress, locale, agMember.getFirstName(), agMember.getMiddleName(), agMember.getLastName(),
				nickName, prefixId, suffixId, male, birthdayMonth, birthdayDay,
				birthdayYear, jobTitle, organizationId, locationId, sendEmail,
				agMember);
	}
	/**
	 * 
	 * @param member
	 * @param user reserved parameter,now it is no use, which may be used in future.
	 * @throws SystemException
	 */
	public static void updateAdminUserMember(AGMember member,User user) throws SystemException{
		getMemberService().updateAdminUserMember(member, user);
	}
	public static int getAdminUserCount(){
		return getMemberService().getAdminUserCount();
	}
	public static List listAdminUser(int pageNumber,int pageSize){
		return getMemberService().listAdminUser(pageNumber, pageSize);
	}
	
	//add at 2007-04-23
	public static int getSurfTime(String tableName,Long memberId){
		return getMemberService().getSurfTime(tableName, memberId);
	}
	public static int getSurfTimeofReferral(String tableName,Long memberId,int selfTime,int startLevel,int endLevel){
		return getMemberService().getSurfTimeofReferral(tableName, memberId, selfTime, startLevel, endLevel);
	} 
	
	public static MemberService getMemberService()
	{
		ApplicationContext ctx = SpringUtil.getContext();
		MemberServiceUtil util = (MemberServiceUtil) ctx
				.getBean(MemberServiceUtil.class.getName());
		return util.memberService;
	}

	public void setMemberService(MemberService memberService)
	{
		this.memberService = memberService;
	}

}