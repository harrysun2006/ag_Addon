package com.amaxgs.addon;

import java.io.BufferedReader;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.agloco.model.AGMember;

class SendEmailToMembers {

	private static Log _log = LogFactory.getLog(SendEmailToMembers.class);
	private final static int DEFAULT_SENDEMAIL_ITEM = 1;
	private final static String DEFAULT_SENT_FILE_NAME = "sent_email_m.txt";
	private final static String DEFAULT_UNSENT_FILE_NAME = "unsent_email_m.txt";

	private final static String[] SENDEMAIL_ITEMS = {
			"send email to all members",
			"send email to members with referrals",
			"send email to members w/o referrals",
			"send email to members who did not download viewbar", };

	private final static boolean[] USE_HQLS = { true, true, true, false, };

	private final static String[] COUNT_HQLS = {
			"select count(m) from AGMember m where m.status = 'N'",
			"select count(m) from AGMember m, com.liferay.portal.model.User u"
					+ " where m.userId = u.userId"
					+ " and m.userId not like 'agloco.com_'"
					+ " and m.memberId in (select distinct t.primaryKey.memberId from AGMemberTree t)"
					+ " and m.memberCode not like 'AGLO____'"
					+ " and u.loginDate is not NULL",
			"select count(m) from AGMember m,com.liferay.portal.model.User u"
					+ " where m.userId = u.userId"
					+ " and m.userId not like 'agloco.com_'"
					+ " and m.memberId not in (select distinct t.primaryKey.memberId from AGMemberTree t)"
					+ " and m.memberCode not like 'AGLO____'"
					+ " and u.loginDate is not NULL",
			"select count(*) from AG_Member m where m.status = 'N' "
					+ "and m.memberId not in (select t.memberId from viewbar.VB_Time_Total t)", };

	private final static String[] LIST_HQLS = {
			"select m from AGMember m where m.status = 'N'",
			"select m from AGMember m,com.liferay.portal.model.User u"
					+ " where m.userId = u.userId"
					+ " and m.userId not like 'agloco.com_'"
					+ " and m.memberId in (select distinct t.primaryKey.memberId from AGMemberTree t)"
					+ " and m.memberCode not like 'AGLO____'"
					+ " and u.loginDate is not NULL",
			"select m from AGMember m,com.liferay.portal.model.User u"
					+ " where m.userId = u.userId"
					+ " and m.userId not like 'agloco.com_'"
					+ " and m.memberId not in (select distinct t.primaryKey.memberId from AGMemberTree t)"
					+ " and m.memberCode not like 'AGLO____'"
					+ " and u.loginDate is not NULL",
			"select memberId, userId, memberCode, AES_DECRYPT(password_, 'JC2aadv11') as password_,  "
					+ "emailAddress, AES_DECRYPT(firstName, 'JC2aadv11') as firstName, "
					+ "AES_DECRYPT(lastName, 'JC2aadv11') as lastName, AES_DECRYPT(middleName, 'JC2aadv11') as middleName "
					+ " from AG_Member m where m.status = 'N' "
					+ "and m.memberId not in (select t.memberId from viewbar.VB_Time_Total t)", };

	private static void help() {
		for (int i = 0; i < SENDEMAIL_ITEMS.length; i++) {
			System.out.println((i + 1) + ": " + SENDEMAIL_ITEMS[i] + ".");
		}
		System.out.println("q. exit or quit!");
		System.out.print("select an item: ");
	}

	public static void send() throws Exception {
		try {
			while (true) {
				help();
				String answer = Agloco.in.readLine();
				int item;
				if (answer == null || answer.trim().length() == 0)
					item = DEFAULT_SENDEMAIL_ITEM;
				else if ("q".equalsIgnoreCase(answer))
					return;
				else {
					try {
						item = Integer.parseInt(answer);
						if (item < 1 || item > SENDEMAIL_ITEMS.length)
							item = -1;
					} catch (Exception e) {
						item = -2;
					}
					if (item < 0) {
						System.out.print("please select a valid item!");
						continue;
					}
				}
				System.out.print("continue to " + SENDEMAIL_ITEMS[item - 1]
						+ "?[yes/(no)]: ");
				answer = Agloco.in.readLine();
				if ("yes".equalsIgnoreCase(answer)) {
					if (item >= 1 && item <= SENDEMAIL_ITEMS.length) {
						sendEmail(Agloco.in, COUNT_HQLS[item - 1],
								LIST_HQLS[item - 1], USE_HQLS[item - 1]);
					} else
						continue;
				}
			}
		} catch (Exception e) {
			_log.error(e, e);
		}
	}

	private static void sendEmail(BufferedReader in, String countHql,
			String listHql, boolean bHql) {
		String articleId = null;
		String sendFile = null;
		String unsendFile = null;
		String answer = null;
		Long beginId = null;
		Long endId = null;

		try {
			System.out.print("please input the article id: ");
			articleId = in.readLine();
			if (StringUtils.isBlank(articleId)) {
				System.out.println("article id can not be empty!");
				return;
			}

			System.out.print("please input the send success email list["
					+ DEFAULT_SENT_FILE_NAME + "]: ");
			sendFile = in.readLine();
			if (StringUtils.isBlank(sendFile)) {
				sendFile = DEFAULT_SENT_FILE_NAME;
			}

			System.out.print("please input the send failure email list["
					+ DEFAULT_UNSENT_FILE_NAME + "]: ");
			unsendFile = in.readLine();
			if (StringUtils.isBlank(unsendFile)) {
				unsendFile = DEFAULT_UNSENT_FILE_NAME;
			}

			System.out.print("please input the begin memberId: ");
			answer = in.readLine();
			if (!StringUtils.isBlank(answer)) {
				try {
					beginId = new Long(Long.parseLong(answer));
				} catch (Exception e1) {
					beginId = null;
				}
			}

			System.out.print("please input the end memberId: ");
			answer = in.readLine();
			if (!StringUtils.isBlank(answer)) {
				try {
					endId = new Long(Long.parseLong(answer));
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
			_log.error(e, e);
		}
		AGMemberLister lister = new AGMemberLister.DBLister(countHql, listHql,
				null, null, AGMemberLister.DEFAULT_PAGE_SIZE, bHql,
				AGMember.class);
		SendEmail.send(articleId, sendFile, unsendFile, lister,
				AGMemberResolver.AG_MEMBER_EMAIL_RESOLVER);
	}

}
