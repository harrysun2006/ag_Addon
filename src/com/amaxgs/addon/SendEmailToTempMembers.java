package com.amaxgs.addon;

import java.io.BufferedReader;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.agloco.model.AGMember;

class SendEmailToTempMembers {

	private static Log _log = LogFactory.getLog(SendEmailToTempMembers.class);
	private final static int DEFAULT_SENDEMAIL_ITEM = 1;
	private final static String DEFAULT_SENT_FILE_NAME = "sent_email_mt.txt";
	private final static String DEFAULT_UNSENT_FILE_NAME = "unsent_email_mt.txt";

	private final static String[] SENDEMAIL_ITEMS = { "send email to all temporary members", };

	private final static boolean[] USE_HQLS = { true, };

	private final static String[] COUNT_HQLS = { "select count(mt) from AGMemberTemp mt where mt.status = 'N'", };

	private final static String[] LIST_HQLS = { "select mt from AGMemberTemp mt where mt.status = 'N'", };

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
						sendEmail(Agloco.in, COUNT_HQLS[item - 1], LIST_HQLS[item - 1],
								USE_HQLS[item - 1]);
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
				countHql = countHql + " and mt.memberId > " + beginId;
				listHql = listHql + "and mt.memberId > " + beginId;
			}

			if (endId != null) {
				countHql = countHql + " and mt.memberId <= " + endId;
				listHql = listHql + " and mt.memberId <= " + endId;
			}
			listHql = listHql + " order by mt.memberId";
		} catch (IOException e) {
			_log.error(e, e);
		}

		// SendEmail.sendToMembers(articleId, sendFile, unsendFile, countHql,
		// listHql, null, null, bHql, DEFAULT_RESOLVER);
		AGMemberLister lister = new AGMemberLister.DBLister(countHql, listHql,
				null, null, AGMemberLister.DEFAULT_PAGE_SIZE, true, AGMember.class);
		SendEmail.send(articleId, sendFile, unsendFile, lister,
				AGMemberResolver.AG_MEMBER_TEMP_EMAIL_RESOLVER);
	}

}
