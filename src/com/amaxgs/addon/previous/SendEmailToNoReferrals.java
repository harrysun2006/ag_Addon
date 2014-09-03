package com.amaxgs.addon.previous;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.agloco.model.AGMember;
import com.amaxgs.addon.AGMemberLister;
import com.amaxgs.addon.AGMemberResolver;
import com.amaxgs.addon.Agloco;
import com.amaxgs.addon.SendEmail;

/**
 * @deprecated
 * @author harry_sun
 *
 */
class SendEmailToNoReferrals {

	private final static String DEFAULT_SENT_FILE_NAME   = "sent_email_no_referrals.txt";
	private final static String DEFAULT_UNSENT_FILE_NAME = "unsent_email_no_referrals.txt";

	private final static Log log = LogFactory.getLog(SendEmailToNoReferrals.class);
	
	private static void sendEmail(String articleId){
		
		StringBuffer countHql  = new StringBuffer(128);
		countHql.append("select count(m) from AGMember m,com.liferay.portal.model.User u")
				.append(" where m.userId = u.userId")
				.append(" and m.userId not like 'agloco.com_'")
				.append(" and m.memberId not in (select distinct t.primaryKey.memberId from AGMemberTree t)")
				.append(" and m.memberCode not like 'AGLO____'")
				.append(" and u.loginDate is not NULL");
				
		
		StringBuffer listHql  = new StringBuffer(128);
		listHql.append("select m from AGMember m,com.liferay.portal.model.User u")
				.append(" where m.userId = u.userId")
				.append(" and m.userId not like 'agloco.com_'")
				.append(" and m.memberId not in (select distinct t.primaryKey.memberId from AGMemberTree t)")
				.append(" and m.memberCode not like 'AGLO____'")
				.append(" and u.loginDate is not NULL")
				.append(" order by m.memberId");
		
		String sendFile = null;
		String unsendFile = null;
		
		try {
			
			System.out.print("please input the send success email list[" + DEFAULT_SENT_FILE_NAME + "]: ");
			sendFile = Agloco.in.readLine();
			if(StringUtils.isBlank(sendFile)) {
				sendFile = DEFAULT_SENT_FILE_NAME;
			}
			
			System.out.print("please input the send failure email list[" + DEFAULT_UNSENT_FILE_NAME + "]: ");
			unsendFile = Agloco.in.readLine();
			if(StringUtils.isBlank(unsendFile)) {
					unsendFile = DEFAULT_UNSENT_FILE_NAME;
			}
			
		} catch (IOException e) {
			if(log.isErrorEnabled()){
				log.error(e,e);
			}
		}
		AGMemberLister lister = new AGMemberLister.DBLister(countHql.toString(), listHql.toString(), null, null, AGMemberLister.DEFAULT_PAGE_SIZE, true, AGMember.class);
		SendEmail.send(articleId, sendFile, unsendFile, lister, AGMemberResolver.AG_MEMBER_EMAIL_RESOLVER);
	}
}
