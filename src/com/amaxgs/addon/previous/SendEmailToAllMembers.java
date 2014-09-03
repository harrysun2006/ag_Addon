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
class SendEmailToAllMembers {

	private final static String DEFAULT_SENT_FILE_NAME   = "sent_email_members.txt";
	private final static String DEFAULT_UNSENT_FILE_NAME = "unsent_email_members.txt";
	
	private final static Log log = LogFactory.getLog(SendEmailToAllMembers.class);
	
	private static void sendEmail(String articleId) {
		
		String countHql = "select count(m) from AGMember m where m.status = 'N'";
		String listHql  = "select m from AGMember m where m.status = 'N'";
		String sendFile = null;
		String unsendFile = null;
		String answer = null;
		Long beginId = null;
		Long endId = null;

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

			System.out.print("please input the begin memberId[N/A]: ");
			answer = Agloco.in.readLine();
			if(!StringUtils.isBlank(answer)) {
				try {
					beginId = Long.parseLong(answer);
				} catch (Exception e1) {
					beginId = null;
				}
			}

			System.out.print("please input the end memberId[N/A]: ");
			answer = Agloco.in.readLine();
			if(!StringUtils.isBlank(answer)) {
				try {
					endId = Long.parseLong(answer);
				} catch (Exception e1) {
					endId = null;
				}
			}

			if (beginId != null) {
				countHql = countHql + " and m.memberId > " + beginId;
				listHql = listHql + "and m.memberId > " + beginId;
			}

			if (endId != null) {
				countHql = countHql + " and m.memberId <= " + endId;
				listHql = listHql + " and m.memberId <= " + endId;
			}
			listHql = listHql + " order by m.memberId";
		} catch (IOException e) {
			if(log.isErrorEnabled()){
				log.error(e,e);
			}
		}
		AGMemberLister lister = new AGMemberLister.DBLister(countHql, listHql, null, null, AGMemberLister.DEFAULT_PAGE_SIZE, true, AGMember.class);
		SendEmail.send(articleId, sendFile, unsendFile, lister, AGMemberResolver.AG_MEMBER_EMAIL_RESOLVER);
	}
	
}
