package com.agloco.service;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Locale;

import com.agloco.mail.MailMessage;
import com.agloco.mail.filter.MailFilter;
import com.agloco.model.AGMember;
import com.agloco.model.AGMemberTemp;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;

public interface MailService extends BaseService {

	public void sendSignupMail(AGMemberTemp mt, Locale locale, String article) throws PortalException, SystemException;
	public void sendSignupMail(AGMember m, Locale locale, String article) throws PortalException, SystemException;
	public void sendFirstSigninMail(AGMember m, Locale locale, String article) throws PortalException, SystemException;
	public void sendForgotMemberCodeMail(AGMember m, Locale locale, String article) throws PortalException, SystemException;
	public void sendForgotPasswordMail(AGMember m, Locale locale, String article) throws PortalException, SystemException;
	public void sendMail(AGMember m, Locale locale, String articleId, String[] names, Object values[]) throws PortalException, SystemException;
	public void sendMail(MailMessage mailMessage) throws RemoteException, SystemException;
	public void setExcluder(MailExcluder mailExcluder);
	public void setFilterList(List filterList);
	public void addFilter(MailFilter mailFilter);
	
}
