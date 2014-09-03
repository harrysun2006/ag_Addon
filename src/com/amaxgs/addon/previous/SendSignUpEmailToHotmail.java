package com.amaxgs.addon.previous;


public class SendSignUpEmailToHotmail {

	/*
	private static Log _log = LogFactory.getLog(SendSignUpEmailToHotmail.class);

	private final static String MAIL_SERVICE_EXCLUDER = "mail.service.excluder";
	private final static String DEFAULT_MAIL_SERVICE_EXCLUDER = "";
	private final static String SIGN_UP_ARTICLE = "AGS_SIGNUP_EMAIL_TEMP2";
	private final static int MAX_SEND_NUMBER = 400;
	private final static String HOTMAIL_SUFFIX = "@hotmail.com";
	private final static String MSN_SUFFIX = "@msn.com";

	private static MailService mailService;

	public static void sendEmail() throws IOException{	
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
		int i = 0;
		while(i < MAX_SEND_NUMBER){
			List hotmail = MemberServiceUtil.listAGMemberTempByEmailSuffix(HOTMAIL_SUFFIX);
			if(hotmail != null && hotmail.size() > 0){
				for(Iterator it = hotmail.iterator(); it.hasNext();){
					AGMemberTemp agmt = (AGMemberTemp)it.next();
					try {
						AlternateMailService.sendMail(agmt, SIGN_UP_ARTICLE);
						agmt.setMailCount(new Integer(5));
						MemberServiceUtil.updateAGMemberTemp(agmt);
						i++;
						_log.info("send sign up email to hotmail success:" + agmt.getEmailAddress());
					} catch (PortalException e) {
						_log.info("send sign up email to hotmail failure:" + agmt.getEmailAddress());
						e.printStackTrace();
					} catch (SystemException e) {
						_log.info("send sign up email to hotmail failure:" + agmt.getEmailAddress());
						e.printStackTrace();
					}
					if(i >= MAX_SEND_NUMBER){
						return;
					}
				}
			}
			
			List msn = MemberServiceUtil.listAGMemberTempByEmailSuffix(MSN_SUFFIX);
			if(msn != null && msn.size() > 0){
				for(Iterator it = msn.iterator(); it.hasNext();){
					AGMemberTemp agmt = (AGMemberTemp)it.next();
					try {
						AlternateMailService.sendMail(agmt, SIGN_UP_ARTICLE);
						agmt.setMailCount(new Integer(5));
						MemberServiceUtil.updateAGMemberTemp(agmt);
						i++;
						_log.info("send sign up email by hand success:" + agmt.getEmailAddress());
					} catch (PortalException e) {
						_log.info("send sign up email by hand failure:" + agmt.getEmailAddress());
						e.printStackTrace();
					} catch (SystemException e) {
						_log.info("send sign up email by hand failure:" + agmt.getEmailAddress());
						e.printStackTrace();
					}
					if(i >= MAX_SEND_NUMBER){
						return;
					}
				}
			}
			if((hotmail == null || hotmail.size() < 1) && (msn == null || msn.size() < 1)){
				return;
			}
		}
	}
*/
}
