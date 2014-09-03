package com.agloco.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.agloco.Constants;
import com.agloco.exception.CannotCatchedException;
import com.agloco.exception.NoSuchUserExistsException;
import com.agloco.form.MemberQueryForm;
import com.agloco.model.AGMember;
import com.agloco.model.AGMemberAudit;
import com.agloco.model.AGMemberCount;
import com.agloco.model.AGMemberTemp;
import com.agloco.model.AGUser;
import com.agloco.model.MemberDetailInfo;
import com.agloco.service.MemberService;
import com.agloco.service.dao.util.CounterDaoUtil;
import com.agloco.service.dao.util.MemberDaoUtil;
import com.agloco.service.dao.util.MemberTreeDaoUtil;
import com.agloco.service.util.MailServiceUtil;
import com.agloco.service.util.MemberServiceUtil;
import com.agloco.util.Generator;
import com.liferay.portal.ContactBirthdayException;
import com.liferay.portal.ContactFirstNameException;
import com.liferay.portal.ContactLastNameException;
import com.liferay.portal.NoSuchGroupException;
import com.liferay.portal.NoSuchRoleException;
import com.liferay.portal.NoSuchUserException;
import com.liferay.portal.OrganizationParentException;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.UserPasswordException;
import com.liferay.portal.language.LanguageException;
import com.liferay.portal.language.LanguageUtil;
import com.liferay.portal.model.Company;
import com.liferay.portal.model.Contact;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Organization;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.User;
import com.liferay.portal.security.auth.PrincipalException;
import com.liferay.portal.security.auth.PrincipalFinder;
import com.liferay.portal.security.auth.PrincipalThreadLocal;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.security.permission.PermissionThreadLocal;
import com.liferay.portal.security.pwd.PwdToolkitUtil;
import com.liferay.portal.service.impl.UserLocalServiceImpl;
import com.liferay.portal.service.permission.UserPermission;
import com.liferay.portal.service.persistence.CompanyUtil;
import com.liferay.portal.service.persistence.ContactUtil;
import com.liferay.portal.service.persistence.GroupFinder;
import com.liferay.portal.service.persistence.OrganizationUtil;
import com.liferay.portal.service.persistence.RoleFinder;
import com.liferay.portal.service.persistence.UserUtil;
import com.liferay.portal.service.spring.GroupLocalServiceUtil;
import com.liferay.portal.service.spring.ResourceLocalServiceUtil;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.PrefsPropsUtil;
import com.liferay.portal.util.PropsUtil;
import com.liferay.portal.util.UserIdGenerator;
import com.liferay.util.GetterUtil;
import com.liferay.util.InstancePool;
import com.liferay.util.Time;
import com.liferay.util.Validator;

/**
 * 
 * @author terry_zhao
 * 
 */
public class MemberServiceImpl implements MemberService {

	public void addAGMember(AGMember member) {
		MemberDaoUtil.addAGMember(member);
	}

	public void addAGMemberTemp(AGMemberTemp mt) throws PortalException {
		MemberDaoUtil.addAGMemberTemp(mt);
	}

	public void addAGUser(AGUser user) {
		MemberDaoUtil.addAGUser(user);
	}

	public void deleteAgMember(AGMember member) {
		MemberDaoUtil.deleteAgMember(member);
	}

	public void deleteAgMemberTemp(AGMemberTemp mt) {
		MemberDaoUtil.deleteAgMemberTemp(mt);
	}

	public AGMember getAGMember(Long memberId) {
		return MemberDaoUtil.getAGMember(memberId);
	}

	public AGMember getAGMemberByBoth(String keyword) {
		return MemberDaoUtil.getAGMemberByBoth(keyword);
	}

	public AGMember getAGMemberByCode(String memberCode) {
		return MemberDaoUtil.getAGMemberByCode(memberCode);
	}

	public AGMember getAGMemberByEmail(String emailAddress) {
		return MemberDaoUtil.getAGMemberByEmail(emailAddress);
	}

	public AGMember getAGMemberByUserId(String userId) {
		return MemberDaoUtil.getAGMemberByUserId(userId);
	}

	public AGMemberTemp getAGMemberTemp(Long memberId) {
		return MemberDaoUtil.getAGMemberTemp(memberId);
	}

	public AGMemberTemp getAGMemberTempByBoth(String keyword) {
		return MemberDaoUtil.getAGMemberTempByBoth(keyword);
	}

	public AGMemberTemp getAGMemberTempByCode(String memberCode) {
		return MemberDaoUtil.getAGMemberTempByCode(memberCode);
	}

	public AGMemberTemp getAGMemberTempByEmail(String emailAddress) {
		return MemberDaoUtil.getAGMemberTempByEmail(emailAddress);
	}

	public AGMemberTemp getAGMemberTempByUserId(String userId) {
		return MemberDaoUtil.getAGMemberTempByUserId(userId);
	}

	public AGUser getAGUser(String userId) {
		return MemberDaoUtil.getAGUser(userId);
	}

	public List listAgMember() {
		return MemberDaoUtil.listAgMember();
	}

	public List listAgMember(MemberQueryForm mqf) {
		List result = new ArrayList();
		int totalCount = 0;
		String memberType = mqf.getMemberType();
		List list = MemberDaoUtil.listAgMember(mqf);
		if (list != null)
			for (int i = 0; i < list.size(); i++) {
				MemberDetailInfo md = new MemberDetailInfo();
				try {
					BeanUtils.copyProperties(md, list.get(i));
					result.add(md);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		totalCount = mqf.getTotalResult();
		mqf.setTotalResult(totalCount);

		return result;
	}

	public List listAgMember(String sql, Object[] params, int pageNum,
			int pageSize) {
		return MemberDaoUtil.listAgMember(sql, params, pageNum, pageSize);
	}

	// this method only use for the members we import form excel and not login
	public List listAgMemberNotLogin() {
		return MemberDaoUtil.listAgMemberNotLogin();
	}

	public List listAgMemberTemp() {
		return MemberDaoUtil.listAgMemberTemp();
	}

	public List listAgMemberTemp(Date begin, Date end) {
		return MemberDaoUtil.listAgMemberTemp(begin, end);
	}

	public List listAgMemberTempSendEmail(int maxMailCount, int intervalDay,
			Calendar time) {
		return MemberDaoUtil.listAgMemberTempSendEmail(maxMailCount,
				intervalDay, time);
	}

	public List listNewAgMemberTempSendEmail(int maxMailCount, int interval,
			int ago, Calendar time) {
		return MemberDaoUtil.listNewAgMemberTempSendEmail(maxMailCount,
				interval, ago, time);
	}

	public void updateAGMember(AGMember member) {
		MemberDaoUtil.updateAGMember(member);
	}

	public void updateAGMember(String operatorId, AGMember member)
			throws IllegalAccessException, InvocationTargetException {

		MemberDaoUtil.addAGMemberAudit(operatorId, member.getMemberId());
		MemberDaoUtil.updateAGMember(member);

	}

	public void updateAGMemberTemp(String operatorId, AGMemberTemp mt)
			throws IllegalAccessException, InvocationTargetException {
		MemberDaoUtil.addAGMemberTempAudit(operatorId, mt);
		MemberDaoUtil.updateAGMemberTemp(mt);
	}

	public void updateAGMemberTemp(AGMemberTemp mt) {
		MemberDaoUtil.updateAGMemberTemp(mt);
	}

	public void updateAGUser(AGUser user) {
		MemberDaoUtil.updateAGUser(user);
	}

	public String generateMemberCode(String className) {
		return CounterDaoUtil.generateMemberCode(className);
	}

	/**
	 * add a descendant member under memberId
	 * 
	 * @param member
	 *            the parent member(the referral)
	 * @param descendant
	 *            the sub member
	 */
	public void addAGMember(AGMember member, AGMember descendant) {
		MemberTreeDaoUtil.addAGMember(member, descendant);
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
	public List getAncestors(AGMember member, String status) {
		return MemberTreeDaoUtil.getAncestors(member, status);
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
	 * @param session
	 * @return
	 * @throws Exception
	 */
	public long getChildrenCount(AGMember member, long level, String status) {
		return MemberTreeDaoUtil.getChildrenCount(member, level, status);
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
	public List getChildrenCountByLevel(AGMember member, long level,
			String status) {
		return MemberTreeDaoUtil.getChildrenCountByLevel(member, level, status);
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
	 * @param session
	 *            hibernate session
	 * @return
	 * @throws Exception
	 */
	public List getChildren(AGMember member, long level, String status) {
		return MemberTreeDaoUtil.getChildren(member, level, status);
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
	 * @param session
	 *            hibernate session
	 * @return
	 * @throws Exception
	 */
	public List getChildrenOfLevel(AGMember member, long level, String status) {
		return MemberTreeDaoUtil.getChildrenOfLevel(member, level, status);
	}

	public User addUserMemberTemp(String companyId, boolean autoUserId,
			String userId, boolean autoPassword, String password1,
			String password2, boolean passwordReset, String emailAddress,
			Locale locale, String firstName, String middleName,
			String lastName, String nickName, String prefixId, String suffixId,
			boolean male, int birthdayMonth, int birthdayDay, int birthdayYear,
			String jobTitle, String organizationId, String locationId,
			boolean sendEmail, AGMemberTemp agMemberTemp)
			throws PortalException, SystemException {

		Company company = CompanyUtil.findByPrimaryKey(companyId);

		if (!company.isStrangers()) {
			checkPermission(userId, organizationId, locationId,
					ActionKeys.ADD_USER);
		}

		String creatorUserId = null;

		try {
			creatorUserId = getUserId();
		} catch (PrincipalException pe) {
		}

		// User

		userId = userId.trim().toLowerCase();
		emailAddress = emailAddress.trim().toLowerCase();
		Date now = new Date();

		boolean alwaysAutoUserId = GetterUtil.getBoolean(PropsUtil
				.get(PropsUtil.USERS_ID_ALWAYS_AUTOGENERATE));

		if (alwaysAutoUserId) {
			autoUserId = true;
		}

		validate(companyId, autoUserId, userId, autoPassword, password1,
				password2, emailAddress, firstName, lastName, organizationId,
				locationId);

		validateOrganizations(companyId, organizationId, locationId);

		if (autoUserId) {
			// UserIdGenerator userIdGenerator = (UserIdGenerator) InstancePool
			// .get(PropsUtil.get(PropsUtil.USERS_ID_GENERATOR));

			try {
				userId = Generator.generateUserId(companyId);
				// userId = userIdGenerator.generate(companyId);
			} catch (Exception e) {
				throw new SystemException(e);
			}
		}

		if (autoPassword) {
			password1 = PwdToolkitUtil.generate();
		}

		int passwordsLifespan = GetterUtil.getInteger(PropsUtil
				.get(PropsUtil.PASSWORDS_LIFESPAN));

		Date expirationDate = null;

		if (passwordsLifespan > 0) {
			expirationDate = new Date(System.currentTimeMillis() + Time.DAY
					* passwordsLifespan);
		}

		User defaultUser = getDefaultUser(companyId);

		String fullName = User.getFullName(firstName, middleName, lastName);

		String greeting = null;

		try {
			greeting = LanguageUtil.get(companyId, locale, "welcome") + ", "
					+ fullName + "!";
		} catch (LanguageException le) {
			greeting = "Welcome, " + fullName + "!";
		}

		User user = UserUtil.create(userId);

		user.setCompanyId(companyId);
		user.setCreateDate(now);
		user.setModifiedDate(now);
		// user.setPassword(PwdEncryptor.encrypt(password1));
		// user.setPasswordUnencrypted(password1);
		// user.setPasswordEncrypted(true);
		user.setPassword("none");
		user.setPasswordUnencrypted("");
		user.setPasswordEncrypted(false);
		user.setPasswordExpirationDate(expirationDate);
		user.setPasswordReset(passwordReset);
		user.setEmailAddress(emailAddress);
		user.setLanguageId(locale.toString());
		user.setTimeZoneId(defaultUser.getTimeZoneId());
		user.setGreeting(greeting);
		user.setResolution(defaultUser.getResolution());
		user.setActive(true);

		UserUtil.update(user);

		/*
		 * // Resources
		 * 
		 * if (creatorUserId == null) { creatorUserId = user.getUserId(); }
		 * 
		 * ResourceLocalServiceUtil.addResources(companyId, null, creatorUserId,
		 * User.class.getName(), user.getPrimaryKey().toString(), false, false,
		 * false);
		 *  // Mail
		 * 
		 * if (user.hasCompanyMx()) {
		 * com.liferay.mail.service.spring.MailServiceUtil.addUser(userId,
		 * password1, firstName, middleName, lastName, emailAddress); }
		 *  // Contact
		 * 
		 * Date birthday = PortalUtil.getDate(birthdayMonth, birthdayDay,
		 * birthdayYear, new ContactBirthdayException());
		 * 
		 * String contactId = userId;
		 * 
		 * Contact contact = ContactUtil.create(contactId);
		 * 
		 * contact.setCompanyId(user.getCompanyId());
		 * contact.setUserId(user.getUserId());
		 * contact.setUserName(User.getFullName(firstName, middleName,
		 * lastName)); contact.setCreateDate(now); contact.setModifiedDate(now);
		 * contact.setAccountId(user.getCompanyId());
		 * contact.setParentContactId(Contact.DEFAULT_PARENT_CONTACT_ID);
		 * contact.setFirstName(firstName); contact.setMiddleName(middleName);
		 * contact.setLastName(lastName); contact.setNickName(nickName);
		 * contact.setPrefixId(prefixId); contact.setSuffixId(suffixId);
		 * contact.setMale(male); contact.setBirthday(birthday);
		 * contact.setJobTitle(jobTitle);
		 * 
		 * ContactUtil.update(contact);
		 *  // Organization and location
		 * 
		 * UserUtil.clearOrganizations(userId);
		 * 
		 * if (Validator.isNotNull(organizationId)) {
		 * UserUtil.addOrganization(userId, organizationId); }
		 * 
		 * if (Validator.isNotNull(locationId)) {
		 * UserUtil.addOrganization(userId, locationId); }
		 *  // Group
		 * 
		 * GroupLocalServiceUtil.addGroup(user.getUserId(),
		 * User.class.getName(), user.getPrimaryKey().toString(), null, null,
		 * null, null);
		 *  // Default groups
		 * 
		 * List groups = new ArrayList();
		 * 
		 * String[] defaultGroupNames = PrefsPropsUtil.getStringArray(companyId,
		 * PropsUtil.ADMIN_DEFAULT_GROUP_NAMES);
		 * 
		 * for (int i = 0; i < defaultGroupNames.length; i++) { try { Group
		 * group = GroupFinder.findByC_N(companyId, defaultGroupNames[i]);
		 * 
		 * groups.add(group); } catch (NoSuchGroupException nsge) { } }
		 * 
		 * UserUtil.setGroups(userId, groups);
		 *  // Default roles
		 * 
		 * List roles = new ArrayList();
		 * 
		 * String[] defaultRoleNames = PrefsPropsUtil.getStringArray(companyId,
		 * PropsUtil.ADMIN_DEFAULT_ROLE_NAMES);
		 * 
		 * for (int i = 0; i < defaultRoleNames.length; i++) { try { Role role =
		 * RoleFinder .findByC_N(companyId, defaultRoleNames[i]);
		 * 
		 * roles.add(role); } catch (NoSuchRoleException nsge) { } }
		 * 
		 * UserUtil.setRoles(userId, roles);
		 * 
		 */

		// add memberTemp
		agMemberTemp.setUserId(user.getUserId());

		MemberDaoUtil.addAGMemberTemp(agMemberTemp);
		MailServiceUtil.sendSignupMail(agMemberTemp, locale);
		return user;
	}

	protected void validate(String companyId, boolean autoUserId,
			String userId, boolean autoPassword, String password1,
			String password2, String emailAddress, String firstName,
			String lastName, String organizationId, String locationId)
			throws PortalException, SystemException {

		// if (!autoUserId)
		// {
		// if (Validator.isNull(userId))
		// {
		// throw new UserIdException();
		// }
		//
		// UserIdValidator userIdValidator = (UserIdValidator) InstancePool
		// .get(PropsUtil.get(PropsUtil.USERS_ID_VALIDATOR));
		//
		// if (userIdValidator != null)
		// {
		// if (!userIdValidator.validate(userId, companyId))
		// {
		// throw new UserIdException();
		// }
		// }
		//
		// String[] anonymousNames = PrincipalSessionBean.ANONYMOUS_NAMES;
		//
		// for (int i = 0; i < anonymousNames.length; i++)
		// {
		// if (userId.equalsIgnoreCase(anonymousNames[i]))
		// {
		// throw new UserIdException();
		// }
		// }
		//
		// String[] companyIds = PortalInstances.getCompanyIds();
		//
		// for (int i = 0; i < companyIds.length; i++)
		// {
		// if (userId.indexOf(companyIds[i]) != -1)
		// {
		// throw new UserIdException();
		// }
		// }
		//
		// try
		// {
		// User user = UserUtil.findByPrimaryKey(userId);
		//
		// if (user != null)
		// {
		// throw new DuplicateUserIdException();
		// }
		// }
		// catch (NoSuchUserException nsue)
		// {
		// }
		//
		// String[] reservedUserIds = PrefsPropsUtil.getStringArray(companyId,
		// PropsUtil.ADMIN_RESERVED_USER_IDS);
		//
		// for (int i = 0; i < reservedUserIds.length; i++)
		// {
		// if (userId.equalsIgnoreCase(reservedUserIds[i]))
		// {
		// throw new ReservedUserIdException();
		// }
		// }
		// }

		if (!autoPassword) {
			if (!password1.equals(password2)) {
				throw new UserPasswordException(
						UserPasswordException.PASSWORDS_DO_NOT_MATCH);
			} else if (!PwdToolkitUtil.validate(password1)
					|| !PwdToolkitUtil.validate(password2)) {

				throw new UserPasswordException(
						UserPasswordException.PASSWORD_INVALID);
			}
		}

		// if (!Validator.isEmailAddress(emailAddress))
		// {
		// throw new UserEmailAddressException();
		// }
		// else {
		// try {
		// User user = UserUtil.findByC_EA(companyId, emailAddress);
		//
		// if (user != null) {
		// throw new DuplicateUserEmailAddressException();
		// }
		// }
		// catch (NoSuchUserException nsue) {
		// }
		//
		// String[] reservedEmailAddresses = PrefsPropsUtil.getStringArray(
		// companyId, PropsUtil.ADMIN_RESERVED_EMAIL_ADDRESSES);
		//
		// for (int i = 0; i < reservedEmailAddresses.length; i++) {
		// if (emailAddress.equalsIgnoreCase(reservedEmailAddresses[i])) {
		// throw new ReservedUserEmailAddressException();
		// }
		// }
		// }

		if (Validator.isNull(firstName)) {
			throw new ContactFirstNameException();
		} else if (Validator.isNull(lastName)) {
			throw new ContactLastNameException();
		}
	}

	protected void validateOrganizations(String companyId,
			String organizationId, String locationId) throws PortalException,
			SystemException {

		boolean organizationRequired = GetterUtil.getBoolean(PropsUtil
				.get(PropsUtil.ORGANIZATIONS_PARENT_ORGANIZATION_REQUIRED));

		boolean locationRequired = GetterUtil.getBoolean(PropsUtil
				.get(PropsUtil.ORGANIZATIONS_LOCATION_REQUIRED));

		if (locationRequired) {
			organizationRequired = true;
		}

		Organization organization = null;

		if (organizationRequired || Validator.isNotNull(organizationId)) {
			organization = OrganizationUtil.findByPrimaryKey(organizationId);
		}

		Organization location = null;

		if (locationRequired || Validator.isNotNull(locationId)) {
			location = OrganizationUtil.findByPrimaryKey(locationId);
		}

		if ((organization != null) && (location != null)) {
			if (!location.getParentOrganizationId().equals(
					organization.getOrganizationId())) {

				throw new OrganizationParentException();
			}
		}
	}

	protected User getDefaultUser(String companyId) throws PortalException,
			SystemException {

		return UserUtil.findByPrimaryKey(User.getDefaultUserId(companyId));
	}

	private static AGMember toAGMember(AGMemberTemp tm) throws PortalException,
			SystemException {
		AGMember member = null;
		try {
			if (tm != null) {
				member = new AGMember();
				BeanUtils.copyProperties(member, tm);
			}
		} catch (Exception e) {
			throw new PortalException(e);
		}
		return member;
	}

	public AGMember updateAgreedToTermsOfUse(String userId,
			boolean agreedToTermsOfUse) throws PortalException, SystemException {
		return updateAgreedToTermsOfUse(userId, agreedToTermsOfUse, "");
	}

	public AGMember updateAgreedToTermsOfUse(String userId,
			boolean agreedToTermsOfUse, String password)
			throws PortalException, SystemException {
		User user = UserUtil.findByPrimaryKey(userId);

		user.setAgreedToTermsOfUse(agreedToTermsOfUse);

		UserUtil.update(user);

		// Move the temp member information from AGMemberTemp to AGMember
		AGMemberTemp agMemberTemp = MemberServiceUtil
				.getAGMemberTempByUserId(userId);

		if (agMemberTemp == null) {
			_log.error("AGMemberTemp[" + userId
					+ "] is null. AgreedToTermsOfUse:"
					+ user.getAgreedToTermsOfUse());
			throw new CannotCatchedException();
		}
		try {

			MemberDaoUtil.addAGMemberTempAudit(userId, agMemberTemp);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			_log.error(e.getClass().getName(), e);
			throw new CannotCatchedException();
		}
		if (password != null && !password.equals("")) {
			agMemberTemp.setPassword(password);
		}

		AGMember agMember = toAGMember(agMemberTemp);
		agMember.setModifiedDate(Calendar.getInstance());
		MemberDaoUtil.addAGMember(agMember);
		MemberDaoUtil.deleteAgMemberTemp(agMemberTemp);

		// modified 12/09/06

		AGMemberCount agmc = new AGMemberCount();
		agmc.setMemberId(agMember.getMemberId());
		MemberDaoUtil.addAGMemberCount(agmc);

		// Associate the referral here
		// if(agMemberTemp.getReferral() != null &&
		// agMemberTemp.getReferral().getMemberId() != null &&
		// agMemberTemp.getReferral().getMemberId().longValue() > 0) {
		// AGMember referral = new AGMember();
		// referral.setMemberId(agMemberTemp.getReferral().getMemberId());
		// MemberTreeDaoUtil.addAGMember(referral, agMember);
		// }
		if (agMemberTemp.getReferralCode() != null
				&& agMemberTemp.getReferralCode().trim().length() > 0) {
			AGMember referral = getAGMemberByCode(agMemberTemp
					.getReferralCode());
			if (referral != null)
				MemberTreeDaoUtil.addAGMember(referral, agMember);
		}
		return agMember;
	}

	public void getInformation(String forgetType, String forgetTypeValue,
			Locale locale) throws PortalException, SystemException {
		AGMember agMember = MemberServiceUtil
				.getAGMemberByBoth(forgetTypeValue);
		if (agMember == null) {
			// If the user has registered but not signin, send he/she the signup
			// email
			AGMemberTemp agTemp = MemberServiceUtil
					.getAGMemberTempByBoth(forgetTypeValue);
			if (agTemp == null)
				throw new NoSuchUserExistsException();
			else
				MailServiceUtil.sendSignupMail(agTemp, locale);
		} else {
			// send a forgot password email
			if (forgetType.equals(Constants.PASSWORD)) {
				MailServiceUtil.sendForgotPasswordMail(agMember, locale);
			} else
			// send a forgot member code email
			{
				MailServiceUtil.sendForgotMemberCodeMail(agMember, locale);
			}
		}
	}

	protected static void checkPermission(String userId, String organizationId,
			String locationId, String actionId) throws PortalException,
			SystemException {

		UserPermission.check(getPermissionChecker(), userId, organizationId,
				locationId, actionId);
	}

	protected static PermissionChecker getPermissionChecker() {
		return PermissionThreadLocal.getPermissionChecker();
	}

	protected static String getUserId() throws PrincipalException {
		String name = PrincipalThreadLocal.getName();

		if (name == null) {
			throw new PrincipalException();
		}

		PrincipalFinder principalFinder = null;

		try {
			principalFinder = (PrincipalFinder) InstancePool.get(PropsUtil
					.get(PropsUtil.PRINCIPAL_FINDER));

			name = principalFinder.toLiferay(name);
		} catch (Exception e) {
		}

		if (Validator.isNull(name)) {
			throw new PrincipalException("Principal cannot be null");
		}

		return name;
	}

	public AGMember authenticate(String login, String password)
			throws Exception {
		// first query the formal member table
		AGMember agMember = getAGMemberByBoth(login);
		if (agMember == null) {
			// then query the temp member table
			AGMemberTemp agMemberTemp = getAGMemberTempByBoth(login);
			if (agMemberTemp == null) {
				throw new NoSuchUserException();
			}
			agMember = new AGMember();
			BeanUtils.copyProperties(agMember, agMemberTemp);
		}

		// changed by locker 20070607
		// if (!agMember.getPassword().equals(password))
		// {
		// throw new UserPasswordException(
		// UserPasswordException.PASSWORDS_DO_NOT_MATCH);
		// }
		return agMember;
	}

	public boolean authenticateForJAAS(String userId, String encPwd)
			throws PortalException, SystemException {
		try {
			userId = userId.trim().toLowerCase();

			// first query the formal member table
			AGMember agMember = getAGMemberByUserId(userId);
			if (agMember != null) {
				if (agMember.getPassword().equals(encPwd)) {
					return true;
				}
			} else {
				// then query the temp member table
				AGMemberTemp agMemberTemp = getAGMemberTempByUserId(userId);
				if (agMemberTemp != null) {
					if (agMemberTemp.getPassword().equals(encPwd)) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			_log.error(e);
		}

		return false;
	}

	public void addAGMemberAudit(AGMemberAudit ma) {
		MemberDaoUtil.addAGMemberAudit(ma);
	}

	public void deleteAGMemberAudit(AGMemberAudit ma) {
		MemberDaoUtil.deleteAGMemberAudit(ma);
	}

	public AGMemberAudit getAGMemberAudit(Long auditId) {
		return MemberDaoUtil.getAGMemberAudit(auditId);
	}

	public void updateAGMemberAudit(AGMemberAudit ma) {
		MemberDaoUtil.updateAGMemberAudit(ma);
	}

	public List listAGMemberAuditByUserId(String userId) {
		return MemberDaoUtil.listAGMemberAuditByUserId(userId);
	}

	public List listAGMemberAuditByMemberCode(String memberCode) {
		return MemberDaoUtil.listAGMemberAuditByMemberCode(memberCode);
	}

	public List listAGMemberAuditByEmailAddress(String emailAddress) {
		return MemberDaoUtil.listAGMemberAuditByEmailAddress(emailAddress);
	}

	public User importMemberFromExcel(String companyId, boolean autoUserId,
			String userId, boolean autoPassword, String password1,
			String password2, boolean passwordReset, String emailAddress,
			Locale locale, String firstName, String middleName,
			String lastName, String nickName, String prefixId, String suffixId,
			boolean male, int birthdayMonth, int birthdayDay, int birthdayYear,
			String jobTitle, String organizationId, String locationId,
			boolean sendEmail, AGMemberTemp agMemberTemp)
			throws PortalException, SystemException {

		Company company = CompanyUtil.findByPrimaryKey(companyId);

		if (!company.isStrangers()) {
			checkPermission(userId, organizationId, locationId,
					ActionKeys.ADD_USER);
		}

		String creatorUserId = null;

		try {
			creatorUserId = getUserId();
		} catch (PrincipalException pe) {
		}

		// User

		userId = userId.trim().toLowerCase();
		emailAddress = emailAddress.trim().toLowerCase();
		Date now = new Date();

		boolean alwaysAutoUserId = GetterUtil.getBoolean(PropsUtil
				.get(PropsUtil.USERS_ID_ALWAYS_AUTOGENERATE));

		if (alwaysAutoUserId) {
			autoUserId = true;
		}

		validate(companyId, autoUserId, userId, autoPassword, password1,
				password2, emailAddress, firstName, lastName, organizationId,
				locationId);

		validateOrganizations(companyId, organizationId, locationId);

		if (autoUserId) {
			UserIdGenerator userIdGenerator = (UserIdGenerator) InstancePool
					.get(PropsUtil.get(PropsUtil.USERS_ID_GENERATOR));

			try {
				userId = userIdGenerator.generate(companyId);
			} catch (Exception e) {
				throw new SystemException(e);
			}
		}

		if (autoPassword) {
			password1 = PwdToolkitUtil.generate();
		}

		int passwordsLifespan = GetterUtil.getInteger(PropsUtil
				.get(PropsUtil.PASSWORDS_LIFESPAN));

		Date expirationDate = null;

		if (passwordsLifespan > 0) {
			expirationDate = new Date(System.currentTimeMillis() + Time.DAY
					* passwordsLifespan);
		}

		User defaultUser = getDefaultUser(companyId);

		String fullName = User.getFullName(firstName, middleName, lastName);

		String greeting = null;

		try {
			greeting = LanguageUtil.get(companyId, locale, "welcome") + ", "
					+ fullName + "!";
		} catch (LanguageException le) {
			greeting = "Welcome, " + fullName + "!";
		}

		User user = UserUtil.create(userId);

		user.setCompanyId(companyId);
		user.setCreateDate(now);
		user.setModifiedDate(now);
		// user.setPassword(PwdEncryptor.encrypt(password1));
		// user.setPasswordUnencrypted(password1);
		// user.setPasswordEncrypted(true);
		user.setPassword("none");
		user.setPasswordUnencrypted("");
		user.setPasswordEncrypted(false);
		user.setPasswordExpirationDate(expirationDate);
		user.setPasswordReset(passwordReset);
		user.setEmailAddress(emailAddress);
		user.setLanguageId(locale.toString());
		user.setTimeZoneId(defaultUser.getTimeZoneId());
		user.setGreeting(greeting);
		user.setResolution(defaultUser.getResolution());
		user.setActive(true);

		UserUtil.update(user);

		// Resources

		if (creatorUserId == null) {
			creatorUserId = user.getUserId();
		}

		ResourceLocalServiceUtil.addResources(companyId, null, creatorUserId,
				User.class.getName(), user.getPrimaryKey().toString(), false,
				false, false);

		// Mail

		if (user.hasCompanyMx()) {
			com.liferay.mail.service.spring.MailServiceUtil.addUser(userId,
					password1, firstName, middleName, lastName, emailAddress);
		}

		// Contact

		Date birthday = PortalUtil.getDate(birthdayMonth, birthdayDay,
				birthdayYear, new ContactBirthdayException());

		String contactId = userId;

		Contact contact = ContactUtil.create(contactId);

		contact.setCompanyId(user.getCompanyId());
		contact.setUserId(user.getUserId());
		contact.setUserName(User.getFullName(firstName, middleName, lastName));
		contact.setCreateDate(now);
		contact.setModifiedDate(now);
		contact.setAccountId(user.getCompanyId());
		contact.setParentContactId(Contact.DEFAULT_PARENT_CONTACT_ID);
		contact.setFirstName(firstName);
		contact.setMiddleName(middleName);
		contact.setLastName(lastName);
		contact.setNickName(nickName);
		contact.setPrefixId(prefixId);
		contact.setSuffixId(suffixId);
		contact.setMale(male);
		contact.setBirthday(birthday);
		contact.setJobTitle(jobTitle);

		ContactUtil.update(contact);

		// Organization and location

		UserUtil.clearOrganizations(userId);

		if (Validator.isNotNull(organizationId)) {
			UserUtil.addOrganization(userId, organizationId);
		}

		if (Validator.isNotNull(locationId)) {
			UserUtil.addOrganization(userId, locationId);
		}

		// Group

		GroupLocalServiceUtil.addGroup(user.getUserId(), User.class.getName(),
				user.getPrimaryKey().toString(), null, null, null, null);

		// Default groups

		List groups = new ArrayList();

		String[] defaultGroupNames = PrefsPropsUtil.getStringArray(companyId,
				PropsUtil.ADMIN_DEFAULT_GROUP_NAMES);

		for (int i = 0; i < defaultGroupNames.length; i++) {
			try {
				Group group = GroupFinder.findByC_N(companyId,
						defaultGroupNames[i]);

				groups.add(group);
			} catch (NoSuchGroupException nsge) {
			}
		}

		UserUtil.setGroups(userId, groups);

		// Default roles

		List roles = new ArrayList();

		String[] defaultRoleNames = PrefsPropsUtil.getStringArray(companyId,
				PropsUtil.ADMIN_DEFAULT_ROLE_NAMES);

		for (int i = 0; i < defaultRoleNames.length; i++) {
			try {
				Role role = RoleFinder
						.findByC_N(companyId, defaultRoleNames[i]);

				roles.add(role);
			} catch (NoSuchRoleException nsge) {
			}
		}

		UserUtil.setRoles(userId, roles);

		// add memberTemp
		agMemberTemp.setUserId(user.getUserId());
		MemberDaoUtil.addAGMemberTemp(agMemberTemp);
		// MailServiceUtil.sendSignupMail(agMemberTemp, locale);
		return user;
	}

	public String disableMemberByEmail(String emailAddress) {

		if (StringUtils.isBlank(emailAddress)) {
			return Constants.NO_SUCH_EMALI;
		}

		AGMemberTemp agmt = getAGMemberTempByEmail(emailAddress);
		if (agmt != null) {
			disableMemberTemp(agmt);
			return Constants.IS_TEMP_MEMBER;
		}

		AGMember agm = getAGMemberByEmail(emailAddress);
		if (agm != null) {
			disableMember(agm);
			return Constants.IS_MEMBER;
		}

		return Constants.NO_SUCH_EMALI;
	}

	protected void disableMember(AGMember agm) {
		agm.setStatus(AGMember.MEMBER_STATUS_INACTIVE);
		updateAGMember(agm);
	}

	protected void disableMemberTemp(AGMemberTemp agmt) {
		agmt.setStatus(AGMemberTemp.DEFAULT_MEMBER_TEMP_STATUS_INACTIVE);
		updateAGMemberTemp(agmt);
	}

	/**
	 * 
	 * add at 12/05/06 by terry
	 */
	public AGMemberCount getAGMemberCount(Long memberId) {
		return MemberDaoUtil.getAGMemberCount(memberId);
	}

	public void addAGMemberCount(AGMemberCount agmc) {
		MemberDaoUtil.addAGMemberCount(agmc);
	}

	public void updateAGMemberCount(AGMemberCount agmc, Long level,
			String status) {
		MemberDaoUtil.updateAGMemberCount(agmc, level, status);
	}

	public List listAGMemberCount() {
		return MemberDaoUtil.listAGMemberCount();
	}

	public List listAGMemberCountNumber() {
		return MemberDaoUtil.listAGMemberCountNumber();
	}

	public int getAGMemberCountNumber() {
		return MemberDaoUtil.getAGMemberCountNumber();
	}

	public List listAGMemberCount(int pageSize, int pageNumber) {
		return MemberDaoUtil.listAGMemberCount(pageSize, pageNumber);
	}

	public void updateAGMemberCountTask(List list, Long level, String status) {

		if (list == null && list.size() < 1) {
			return;
		}

		// long count;
		for (Iterator it = list.iterator(); it.hasNext();) {
			AGMemberCount agmc = (AGMemberCount) it.next();
			// count = 0;

			// AGMember member = MemberDaoUtil.getAGMember(agmc.getMemberId());
			// if(member == null){
			// continue;
			// }
			// count = MemberTreeDaoUtil.getChildrenCount(member, level,
			// status);
			// agmc.setCount(new Long(count));
			MemberDaoUtil.updateAGMemberCount(agmc, level, status);

		}

	}

	/**
	 * add at 12/06/06 by Erick Kong
	 */
	public int getAGMemberNumber(String sql, Object[] params) {
		return MemberDaoUtil.getAGMemberNumber(sql, params);
	}

	/**
	 * add at 12/06/06
	 */

	public int getAGMemberNumber() {
		return MemberDaoUtil.getAGMemberNumber();
	}

	/**
	 * return a list of AGMember
	 */
	public List listAGMemeber(int pageSize, int pageNumber) {
		return MemberDaoUtil.listAGMemeber(pageSize, pageNumber);
	}

	public void updateAGMemberCountTask(Long level, String status) {
		MemberDaoUtil.updateAGMemberCountTask(level, status);
	}

	/**
	 * 
	 * @param memberId
	 * @return the member count order(desc)
	 */
	public int getAGMemberCountOrder(Long memberId) {
		return MemberDaoUtil.getAGMemberCountOrder(memberId);
	}

	/**
	 * add at 08/02/2007
	 * 
	 * @param count
	 * @return
	 */
	public int getAGMemberCountOrder(int count) {
		return MemberDaoUtil.getAGMemberCountOrder(count);
	}

	/**
	 * add at 12/08/06
	 */
	public List listAGMemberTempByEmailSuffix(String emailAddress) {
		return MemberDaoUtil.listAGMemberTempByEmailSuffix(emailAddress);
	}

	/**
	 * add at 12/13/06
	 * 
	 * @return the count of member with at least one referrals
	 */
	public int getAGMemberCountWithReferrals() {
		return MemberDaoUtil.getAGMemberCountWithReferrals();
	}

	/**
	 * add at 12/28/06
	 */
	public List listAGMemberByEmail(String[] emailAddress) {
		return MemberDaoUtil.listAGMemberByEmail(emailAddress);
	}

	public void suspendAGMember(List list) {
		MemberDaoUtil.suspendAGMember(list);
	}

	public void enableAGMember(List list) {
		MemberDaoUtil.enableAGMember(list);
	}

	public List listAGMemberTempByEmail(String[] emailAddress) {
		return MemberDaoUtil.listAGMemberTempByEmail(emailAddress);
	}

	public void suspendAGMemberTemp(List list) {
		MemberDaoUtil.suspendAGMemberTemp(list);
	}

	public void enableAGMemberTemp(List list) {
		MemberDaoUtil.enableAGMemberTemp(list);
	}

	/**
	 * modified at 12/02/2007
	 * 
	 * @param ids
	 *            userId
	 * @param status
	 * @param bMember
	 *            AGMember:true;AGMemberTemp:false
	 */
	public void updateMemberStatus(String[] ids, String status, boolean bMember) {
		MemberDaoUtil.updateMemberStatus(ids, status, bMember);
	}

	// add at 2007-03-06
	public User addAdminUserMember(String companyId, boolean autoUserId,
			String userId, boolean autoPassword, String password1,
			String password2, boolean passwordReset, String emailAddress,
			Locale locale, String firstName, String middleName,
			String lastName, String nickName, String prefixId, String suffixId,
			boolean male, int birthdayMonth, int birthdayDay, int birthdayYear,
			String jobTitle, String organizationId, String locationId,
			boolean sendEmail, AGMember agMember) throws PortalException,
			SystemException {

		Company company = CompanyUtil.findByPrimaryKey(companyId);

		if (!company.isStrangers()) {
			checkPermission(userId, organizationId, locationId,
					ActionKeys.ADD_USER);
		}

		String creatorUserId = null;

		try {
			creatorUserId = getUserId();
		} catch (PrincipalException pe) {
		}

		// User

		userId = userId.trim().toLowerCase();
		emailAddress = emailAddress.trim().toLowerCase();
		Date now = new Date();

		boolean alwaysAutoUserId = GetterUtil.getBoolean(PropsUtil
				.get(PropsUtil.USERS_ID_ALWAYS_AUTOGENERATE));

		if (alwaysAutoUserId) {
			autoUserId = true;
		}

		validate(companyId, autoUserId, userId, autoPassword, password1,
				password2, emailAddress, firstName, lastName, organizationId,
				locationId);

		validateOrganizations(companyId, organizationId, locationId);

		if (autoUserId) {
			try {
				userId = Generator.generateAdminUserId(companyId);
			} catch (Exception e) {
				throw new SystemException(e);
			}
		}

		if (autoPassword) {
			password1 = PwdToolkitUtil.generate();
		}

		int passwordsLifespan = GetterUtil.getInteger(PropsUtil
				.get(PropsUtil.PASSWORDS_LIFESPAN));

		Date expirationDate = null;

		if (passwordsLifespan > 0) {
			expirationDate = new Date(System.currentTimeMillis() + Time.DAY
					* passwordsLifespan);
		}

		User defaultUser = getDefaultUser(companyId);

		String fullName = User.getFullName(firstName, middleName, lastName);

		String greeting = null;

		try {
			greeting = LanguageUtil.get(companyId, locale, "welcome") + ", "
					+ fullName + "!";
		} catch (LanguageException le) {
			greeting = "Welcome, " + fullName + "!";
		}

		User user = UserUtil.create(userId);

		user.setCompanyId(companyId);
		user.setCreateDate(now);
		user.setModifiedDate(now);
		// user.setPassword(PwdEncryptor.encrypt(password1));
		// user.setPasswordUnencrypted(password1);
		// user.setPasswordEncrypted(true);
		user.setPassword("none");
		user.setPasswordUnencrypted("");
		user.setPasswordEncrypted(false);
		user.setPasswordExpirationDate(expirationDate);
		user.setPasswordReset(passwordReset);
		user.setEmailAddress(emailAddress);
		user.setLanguageId(locale.toString());
		user.setTimeZoneId(defaultUser.getTimeZoneId());
		user.setGreeting(greeting);
		user.setResolution(defaultUser.getResolution());
		user.setActive(true);
		user.setAgreedToTermsOfUse(true);

		UserUtil.update(user);

		// Resources

		if (creatorUserId == null) {
			creatorUserId = user.getUserId();
		}

		ResourceLocalServiceUtil.addResources(companyId, null, creatorUserId,
				User.class.getName(), user.getPrimaryKey().toString(), false,
				false, false);

		// Mail

		if (user.hasCompanyMx()) {
			com.liferay.mail.service.spring.MailServiceUtil.addUser(userId,
					password1, firstName, middleName, lastName, emailAddress);
		}

		// Contact

		Date birthday = PortalUtil.getDate(birthdayMonth, birthdayDay,
				birthdayYear, new ContactBirthdayException());

		String contactId = userId;

		Contact contact = ContactUtil.create(contactId);

		contact.setCompanyId(user.getCompanyId());
		contact.setUserId(user.getUserId());
		contact.setUserName(User.getFullName(firstName, middleName, lastName));
		contact.setCreateDate(now);
		contact.setModifiedDate(now);
		contact.setAccountId(user.getCompanyId());
		contact.setParentContactId(Contact.DEFAULT_PARENT_CONTACT_ID);
		contact.setFirstName(firstName);
		contact.setMiddleName(middleName);
		contact.setLastName(lastName);
		contact.setNickName(nickName);
		contact.setPrefixId(prefixId);
		contact.setSuffixId(suffixId);
		contact.setMale(male);
		contact.setBirthday(birthday);
		contact.setJobTitle(jobTitle);

		ContactUtil.update(contact);

		// Organization and location

		UserUtil.clearOrganizations(userId);

		if (Validator.isNotNull(organizationId)) {
			UserUtil.addOrganization(userId, organizationId);
		}

		if (Validator.isNotNull(locationId)) {
			UserUtil.addOrganization(userId, locationId);
		}

		// Group

		GroupLocalServiceUtil.addGroup(user.getUserId(), User.class.getName(),
				user.getPrimaryKey().toString(), null, null, null, null);

		// Default groups

		List groups = new ArrayList();

		String[] defaultGroupNames = PrefsPropsUtil.getStringArray(companyId,
				PropsUtil.ADMIN_DEFAULT_GROUP_NAMES);

		for (int i = 0; i < defaultGroupNames.length; i++) {
			try {
				Group group = GroupFinder.findByC_N(companyId,
						defaultGroupNames[i]);

				groups.add(group);
			} catch (NoSuchGroupException nsge) {
			}
		}

		UserUtil.setGroups(userId, groups);

		// Default roles

		List roles = new ArrayList();

		String[] defaultRoleNames = PrefsPropsUtil.getStringArray(companyId,
				PropsUtil.ADMIN_DEFAULT_ROLE_NAMES);

		for (int i = 0; i < defaultRoleNames.length; i++) {
			try {
				Role role = RoleFinder
						.findByC_N(companyId, defaultRoleNames[i]);

				roles.add(role);
			} catch (NoSuchRoleException nsge) {
			}
		}

		UserUtil.setRoles(userId, roles);

		// add admin member
		agMember.setUserId(user.getUserId());
		MemberDaoUtil.addAGMemberAdmin(agMember);
		// MailServiceUtil.sendSignupMail(agMember, locale);
		return user;
	}

	public void updateAdminUserMember(AGMember member, User user)
			throws SystemException {
		if (member == null) {
			return;
		}

		MemberDaoUtil.updateAGMember(member);
		if (StringUtils.isBlank(member.getUserId())) {
			return;
		}
		Contact contact = ContactUtil.fetchByPrimaryKey(member.getUserId());
		contact.setFirstName(member.getFirstName());
		contact.setMiddleName(member.getMiddleName());
		contact.setLastName(member.getLastName());
		ContactUtil.update(contact);
	}

	// add at 2007-03-06
	public int getAdminUserCount() {
		return MemberDaoUtil.getAdminUserCount();
	}

	public List listAdminUser(int pageNumber, int pageSize) {
		return MemberDaoUtil.listAdminUser(pageNumber, pageSize);
	}

	// add at 2007-04-23
	public int getSurfTime(String tableName, Long memberId) {
		return MemberDaoUtil.getSurfTime(tableName, memberId);
	}

	public int getSurfTimeofReferral(String tableName, Long memberId,
			int selfTime, int startLevel, int endLevel) {
		return MemberDaoUtil.getSurfTimeofReferral(tableName, memberId,
				selfTime, startLevel, endLevel);
	}

	private static Log _log = LogFactory.getLog(UserLocalServiceImpl.class);

}
