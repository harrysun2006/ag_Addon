package com.amaxgs.addon;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

class SendEmailToList {

	private static Log _log = LogFactory.getLog(SendEmailToList.class);
	private final static int DEFAULT_ITEM_TYPE = 1;
	private final static String DEFAULT_SENT_FILE_NAME   = "sent_email.txt";
	private final static String DEFAULT_UNSENT_FILE_NAME = "unsent_email.txt";

	private final static String[] ITEM_TYPES = {
		//"send email to temp members signed up at 102 after 2007-03-22",
		"AG_M_Temp's userId",
		"AG_Member's emailAddress",
	};

	private final static AGMemberResolver[] AG_MEMBER_RESOLVERS = {
		AGMemberResolver.AG_MEMBER_TEMP_UID_RESOLVER,
		AGMemberResolver.AG_MEMBER_EMAIL_RESOLVER,
	};

	private static void help() {
		for(int i = 0; i < ITEM_TYPES.length; i++) {
			System.out.println((i + 1) + ": " + ITEM_TYPES[i] + ".");
		}
		System.out.println("q. exit or quit!");
		System.out.print("select an item: ");
	}

	public static void send() throws Exception {
		String fileName, articleId, sendFile, unsendFile;
		try {
			while(true) {
				System.out.print("please input filename: ");
				fileName = Agloco.in.readLine();
				if(StringUtils.isBlank(fileName)) {
					System.out.println("file name is empty!");
					return;
				}

				System.out.print("please input the article id: ");
				articleId = Agloco.in.readLine();
				if(StringUtils.isBlank(articleId)) {
					System.out.println("article id can not be empty!");
					return ;
				}

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

				help();
				String answer = Agloco.in.readLine();
				int item;
				if(answer == null || answer.trim().length() == 0) item = DEFAULT_ITEM_TYPE;
				else if("q".equalsIgnoreCase(answer)) return;
				else {
					try {
						item = Integer.parseInt(answer);
						if (item < 1 || item > ITEM_TYPES.length) item = -1;
					} catch (Exception e) {
						item = -2;
					}
					if(item < 0) {
						System.out.print("Please select a valid item!");
						continue;
					}
				}
				
				System.out.print("continue to send emails to list in " + fileName + " of " 
						+ ITEM_TYPES[item - 1] + "?[yes/(no)]: ");
				answer = Agloco.in.readLine();
				if("yes".equalsIgnoreCase(answer)) {
					if(item >= 1 && item <= ITEM_TYPES.length) {
						AGMemberLister lister = new AGMemberLister.FileLister(fileName);
						SendEmail.send(articleId, sendFile, unsendFile, lister, AG_MEMBER_RESOLVERS[item - 1]);
					}
					else continue;
				}
			}
		} catch(Exception e) {
			_log.error(e, e);
		} 
	}

}
