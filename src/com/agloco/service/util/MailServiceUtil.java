package com.agloco.service.util;

import java.rmi.RemoteException;
import java.util.Locale;

import org.springframework.context.ApplicationContext;

import com.agloco.Constants;
import com.agloco.mail.MailMessage;
import com.agloco.model.AGMember;
import com.agloco.model.AGMemberTemp;
import com.agloco.service.MailService;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.spring.util.SpringUtil;

public class MailServiceUtil {

	private MailService mailService;

	/**
	 * send a first signin mail to member(signin confirm email)
	 * @param m
	 * @throws PortalException
	 * @throws SystemException
	 */
	public static void sendFirstSigninMail(AGMember m) throws PortalException, SystemException {
		getMailService().sendFirstSigninMail(m, null, Constants.ARTICLEID_AGS_FIRST_SIGNIN_EMAIL);
	}

	public static void sendFirstSigninMail(AGMember m, Locale locale) throws PortalException, SystemException {
		getMailService().sendFirstSigninMail(m, locale, Constants.ARTICLEID_AGS_FIRST_SIGNIN_EMAIL);
	}

	public static void sendFirstSigninMail(AGMember m, String article) throws PortalException, SystemException {
		getMailService().sendFirstSigninMail(m, null, article);
	}

	public static void sendFirstSigninMail(AGMember m, Locale locale, String article) throws PortalException, SystemException {
		getMailService().sendFirstSigninMail(m, locale, article);
	}

	/**
	 * send a signup email to temparory member
	 * @param mt
	 * @throws PortalException
	 * @throws SystemException
	 */
	public static void sendSignupMail(AGMemberTemp mt) throws PortalException, SystemException {
		getMailService().sendSignupMail(mt, null, Constants.ARTICLEID_AGS_SIGNUP_EMAIL);
	}

	public static void sendSignupMail(AGMemberTemp mt, Locale locale) throws PortalException, SystemException {
		getMailService().sendSignupMail(mt, locale, Constants.ARTICLEID_AGS_SIGNUP_EMAIL);
	}

	public static void sendSignupMail(AGMemberTemp mt, String article) throws PortalException, SystemException {
		getMailService().sendSignupMail(mt, null, article);
	}

	public static void sendSignupMail(AGMemberTemp mt, Locale locale, String article) throws PortalException, SystemException {
		getMailService().sendSignupMail(mt, locale, article);
	}

	/**
	 * send a signup email to member
	 * @param m
	 * @throws PortalException
	 * @throws SystemException
	 */
	public static void sendSignupMail(AGMember m) throws PortalException, SystemException {
		getMailService().sendSignupMail(m, null, Constants.ARTICLEID_AGS_SIGNUP_EMAIL);
	}
	
	public static void sendSignupMail(AGMember m, Locale locale) throws PortalException, SystemException{
		getMailService().sendSignupMail(m, locale, Constants.ARTICLEID_AGS_SIGNUP_EMAIL);
	}

	public static void sendSignupMail(AGMember m, String article) throws PortalException, SystemException {
		getMailService().sendSignupMail(m, null, article);
	}

	public static void sendSignupMail(AGMember m, Locale locale, String article) throws PortalException, SystemException{
		getMailService().sendSignupMail(m, locale, article);
	}

	/**
	 * send a forgot member code email to member
	 * @param m
	 * @throws PortalException
	 * @throws SystemException
	 */
	public static void sendForgotMemberCodeMail(AGMember m) throws PortalException, SystemException {
		getMailService().sendForgotMemberCodeMail(m, null, Constants.ARTICLEID_AGS_FORGOT_MEMBERCODE_EMAIL);
	}

	public static void sendForgotMemberCodeMail(AGMember m, Locale locale) throws PortalException, SystemException {
		getMailService().sendForgotMemberCodeMail(m, locale, Constants.ARTICLEID_AGS_FORGOT_MEMBERCODE_EMAIL);
	}

	public static void sendForgotMemberCodeMail(AGMember m, String article) throws PortalException, SystemException {
		getMailService().sendForgotMemberCodeMail(m, null, article);
	}

	public static void sendForgotMemberCodeMail(AGMember m, Locale locale, String article) throws PortalException, SystemException {
		getMailService().sendForgotMemberCodeMail(m, locale, article);
	}

	/**
	 * send a forgot password email to member
	 * @param m
	 * @throws PortalException
	 * @throws SystemException
	 */
	public static void sendForgotPasswordMail(AGMember m) throws PortalException, SystemException {
		getMailService().sendForgotPasswordMail(m, null, Constants.ARTICLEID_AGS_FORGOT_PASSWORD_EMAIL);
	}

	public static void sendForgotPasswordMail(AGMember m, Locale locale) throws PortalException, SystemException {
		getMailService().sendForgotPasswordMail(m, locale, Constants.ARTICLEID_AGS_FORGOT_PASSWORD_EMAIL);
	}

	public static void sendForgotPasswordMail(AGMember m, String article) throws PortalException, SystemException {
		getMailService().sendForgotPasswordMail(m, null, article);
	}

	public static void sendForgotPasswordMail(AGMember m, Locale locale, String article) throws PortalException, SystemException {
		getMailService().sendForgotPasswordMail(m, locale, article);
	}

	/**
	 * send an email to member
	 * @param m
	 * @param article
	 * @param names
	 * @param values
	 * @throws PortalException
	 * @throws SystemException
	 */
	public static void sendMail(AGMember m, String article) throws PortalException, SystemException {
		getMailService().sendMail(m, null, article, null, null);
	}

	public static void sendMail(AGMember m, Locale locale, String article) throws PortalException, SystemException {
		getMailService().sendMail(m, locale, article, null, null);
	}

	public static void sendMail(AGMember m, String article, String[] names, Object[] values) throws PortalException, SystemException {
		getMailService().sendMail(m, null, article, names, values);
	}

	public static void sendMail(AGMember m, Locale locale, String article, String[] names, Object values[]) throws PortalException, SystemException {
		getMailService().sendMail(m, locale, article, names, values);
	}

	public static void sendMail(MailMessage mailMessage) throws RemoteException, SystemException {
		getMailService().sendMail(mailMessage);
	}

	public static MailService getMailService(){
		ApplicationContext ctx = SpringUtil.getContext();
		MailServiceUtil util = (MailServiceUtil)ctx.getBean(MailServiceUtil.class.getName());
		return util.mailService;
	}
	
	public void setMailService(MailService mailService) {
		this.mailService = mailService;
	}
}
