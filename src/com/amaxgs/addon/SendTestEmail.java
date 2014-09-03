package com.amaxgs.addon;

import java.io.File;
import java.net.InetAddress;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.mail.internet.InternetAddress;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.agloco.Constants;
import com.agloco.mail.MailMessage;
import com.agloco.mail.MailPart;
import com.agloco.mail.Part;
import com.agloco.model.AGMember;
import com.agloco.service.MailExcluder;
import com.agloco.service.MailService;
import com.agloco.service.util.MailServiceUtil;
import com.agloco.service.util.MemberServiceUtil;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.spring.util.SpringUtil;
import com.liferay.portal.util.PrefsPropsUtil;
import com.liferay.portal.util.PropsUtil;

class SendTestEmail {

	private static Log _log = LogFactory.getLog(SendTestEmail.class);
	private final static String TEST_EMAIL_TO_NAME = "test.email.to.name";
	private final static String TEST_EMAIL_TO_ADDRESS = "test.email.to.address";
	private final static String MAIL_SERVICE_EXCLUDER = "mail.service.excluder";
	private final static String DEFAULT_TEST_EMAIL_TO_NAME = "Test Receiver";
	private final static String DEFAULT_TEST_EMAIL_TO_ADDRESS = "harry_sun@amaxgs.com";
	private final static String DEFAULT_MAIL_SERVICE_EXCLUDER = "";
	private final static String TEST_EMAIL = "TEST";
	private static MailService mailService;

	private final static String[] ALL_EMAIL_ARTICLES = {
		Constants.ARTICLEID_AGS_CHANGE_EMAIL_ADDRESS_EMAIL,
		Constants.ARTICLEID_AGS_CHANGE_PASSWORD_EMAIL,
		Constants.ARTICLEID_AGS_FIRST_SIGNIN_EMAIL,
		Constants.ARTICLEID_AGS_FORGOT_MEMBERCODE_EMAIL,
		Constants.ARTICLEID_AGS_FORGOT_PASSWORD_EMAIL,
		Constants.ARTICLEID_AGS_SIGNUP_EMAIL,
		Constants.ARTICLEID_AGS_SIGNUP_EMAIL_TIMING,
		TEST_EMAIL,
	};

	private final static String[] DEF_EMAIL_ARTICLES = {
		TEST_EMAIL,
	};

	private static void help() {
		for(int i = 0; i < ALL_EMAIL_ARTICLES.length; i++) {
			System.out.println((i + 1) + ": article " + ALL_EMAIL_ARTICLES[i]);
		}
		System.out.println("a: all articles");
		System.out.println("q: exit or quit!");
		System.out.print("choose article(multi choice: 1, 3, 5): ");
	}

	public static void sendEmail() throws PortalException, SystemException {
		mailService = MailServiceUtil.getMailService();
		try {
			String excluder = Agloco.ADDON_PROPERTIES.getProperty(MAIL_SERVICE_EXCLUDER);
			if(excluder == null || excluder.trim().length() == 0) excluder = DEFAULT_MAIL_SERVICE_EXCLUDER;
			if(excluder.trim().length() > 0) {
				MailExcluder mailExcluder = (MailExcluder)SpringUtil.getContext().getBean(excluder);
				mailService.setExcluder(mailExcluder);
			} else {
				mailService.setExcluder(null);
			}
		} catch(Exception e) {
			_log.warn("can NOT set mail excluder!", e);
		}
		try {
			while(true) {
				help();
				String choice = Agloco.in.readLine();
				String[] articles;
				if(choice == null || choice.trim().length() == 0) articles = DEF_EMAIL_ARTICLES;
				else if("a".equalsIgnoreCase(choice)) articles = ALL_EMAIL_ARTICLES;
				else if("q".equalsIgnoreCase(choice)) return;
				else {
					String[] items = choice.split(",");
					String item;
					int index;
					Set set = new HashSet();
					for(int i = 0; i < items.length; i++) {
						item = items[i];
						try {
							index = Integer.parseInt(item.trim());
						} catch(Exception e) {
							index = -1;
						}
						index--;
						if(index >= 0 && index < ALL_EMAIL_ARTICLES.length) set.add(ALL_EMAIL_ARTICLES[index]);
						else if(index < 0) set.add(item);
					}
					articles = new String[set.size()];
					Iterator it = set.iterator();
					for(int i = 0; i < set.size(); i++) {
						articles[i] = (String) it.next();
					}
				}
				//sent to a member
				String defaultToAddress = Agloco.ADDON_PROPERTIES.getProperty(TEST_EMAIL_TO_ADDRESS);
				if(defaultToAddress == null) defaultToAddress = DEFAULT_TEST_EMAIL_TO_ADDRESS;
				System.out.print("Send test email to[" + defaultToAddress + "]: ");
				String toAddress = Agloco.in.readLine();
				if(toAddress == null || toAddress.trim().length() == 0) toAddress = defaultToAddress;
				AGMember m = MemberServiceUtil.getAGMemberByEmail(toAddress);
				if(m == null) {
					m = new AGMember();
					m.setUserId("agloco.com.3");
					m.setMemberCode("AMAXTEST");
					m.setPassword("AGLOCO");
					m.setEmailAddress(toAddress);
					m.setFirstName("Receiver");
					m.setLastName("Test");
				}
				for(int i = 0; i < articles.length; i++) {
					sendTestMail(m, articles[i], toAddress);
				}
			}
		} catch(Exception e) {
			_log.error(e, e);
		} 
	}

	private static void sendTestMail(AGMember m, String article, String toAddress) {
		String msg;
		try {
			String companyId = "agloco.com";
			if(TEST_EMAIL.equalsIgnoreCase(article)) {
				String subject = "Test\r\n多行主题!\r\n";
				InetAddress ia = InetAddress.getLocalHost();
				String body = "Test to see if mail server is OK!"
					+ "\r\nWelcome to AGLOCO: http://www.agloco.com."
					+ "\r\nContact(link disabled): emailservice@agloco.com"
					+ "\r\nContact(link enabled): mailto:emailservice@agloco.com"
					+ "\r\n这行是中文!"
					+ "\r\nSend IP: " + ia.getHostAddress();
				String fromName = PrefsPropsUtil.getString(companyId,
						PropsUtil.ADMIN_EMAIL_FROM_NAME);
				String fromAddress = PrefsPropsUtil.getString(companyId,
						PropsUtil.ADMIN_EMAIL_FROM_ADDRESS);
				String toName = Agloco.ADDON_PROPERTIES.getProperty(TEST_EMAIL_TO_NAME);
				if(toName == null || toName.trim().length() == 0) toName = DEFAULT_TEST_EMAIL_TO_NAME;
				InternetAddress from = new InternetAddress(fromAddress, fromName);
				InternetAddress to = new InternetAddress(toAddress, toName);
				MailMessage message = new MailMessage(from, to, subject, body, false);
				message.setCharset("gb2312");
				Part attachment = new MailPart();
				attachment.setContent(new File("addon.txt"));
				message.addAttachment(attachment);
				mailService.sendMail(message);
			} else {
				mailService.sendMail(m, null, article, null, null);
			}
			msg = article + " email send successfully to " + toAddress;
			System.out.println(msg);
			_log.info(msg);
		} catch (Exception e) {
			msg = article + " email send failed to " + toAddress;
			System.out.println(msg);
			_log.error(msg, e);
		} 
	}

}
