package com.amaxgs.addon;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.agloco.model.AGMember;
import com.agloco.service.util.MailServiceUtil;

class SendEmail {

	private final static Log _log = LogFactory.getLog(SendEmail.class);

	private final static int PAGE_SIZE = 500;
	private final static int MILLISECONDS_PER_MINUTE = 1000 * 60;

	public static void send(String articleId, String sendFile, String unsendFile, 
			AGMemberLister lister, AGMemberResolver resolver) {

		sendFile = FileHelper.guessFileName(sendFile);
		unsendFile = FileHelper.guessFileName(unsendFile);

		if (StringUtils.isBlank(sendFile)) {
			System.out.println("send file name can not be empty!");
			if (_log.isErrorEnabled()) {
				_log.error("send file name can not be empty!");
			}
			return;
		}

		if (StringUtils.isBlank(unsendFile)) {
			System.out.println("unsend file name can not be empty!");
			if (_log.isErrorEnabled()) {
				_log.error("unsend file name can not be empty!");
			}
			return;
		}

		int success = 0;
		int failure = 0;
		int skip = 0;
		List list = null;
		Set sentSet = null; // sent email set

		FileWriter fw_s = null;
		BufferedWriter bw_s = null;

		FileWriter fw_u = null;
		BufferedWriter bw_u = null;

		DateFormat df = new SimpleDateFormat("z(Z) yyyy-MM-dd HH:mm:ss");

		String testEmail = Agloco.ADDON_PROPERTIES.getProperty("test.member.email.address");
		String[] testEmails = {};
		if (testEmail != null) {
			testEmails = testEmail.split(",");
		}

		try {
			System.out.print("please input batch size of sending email[" + PAGE_SIZE + "]: ");
			int pageSize = PAGE_SIZE;
			String ps = Agloco.in.readLine();
			if (StringUtils.isNotBlank(ps)) {
				if (StringUtils.isNumeric(ps)) {
					pageSize = Integer.parseInt(ps);
				}
			}

			System.out.print("please input interval(minutes) of batch sending email[1]: ");
			int interval = 1;
			long sleepTime;
			String st = Agloco.in.readLine();
			if (StringUtils.isNotBlank(st)) {
				if (StringUtils.isNumeric(st)) {
					interval = Integer.parseInt(st);
				}
			}
			sleepTime = interval * MILLISECONDS_PER_MINUTE;

			// read email list from file
			try {
				sentSet = FileHelper.readFile2Set(sendFile);
				_log.info("read email from file success, total number is: " + sentSet.size());
			} catch(FileNotFoundException fnfe) {
				sentSet = new HashSet();
				if (_log.isWarnEnabled()) _log.warn("file " + sendFile + " does NOT exist, will be created!");
			}

			// open success email file, print timestamp
			fw_s = new FileWriter(sendFile, true);
			bw_s = new BufferedWriter(fw_s);
			bw_s.newLine();
			bw_s.write(df.format(Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTime()));
			bw_s.newLine();

			// open unsend email file, print timestamp
			fw_u = new FileWriter(unsendFile, true);
			bw_u = new BufferedWriter(fw_u);
			bw_u.newLine();
			bw_u.write(df.format(Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTime()));
			bw_u.newLine();
			if (resolver == null) resolver = AGMemberResolver.AG_MEMBER_EMAIL_RESOLVER;

			int testCount = 0;
			if (testEmail != null && testEmails.length > 0) {
				for (int i = 0; i < testEmails.length; i++) {
					AGMember m = resolver.getAGMember(testEmails[i]);
					if (m != null) {
						MailServiceUtil.sendMail(m, null, articleId, null, null);
						testCount++;
						_log.info("test sending " + articleId	+ " email success, emailAddress = " + m.getEmailAddress());
						System.out.println("test sending " + articleId + " email success, emailAddress = " + m.getEmailAddress());
					}
				}
			}

			System.out.println("has sent " + testCount + " test email success, please check these emails!");
			System.out.print("are you sure to continue sending emails?[yes/(no)]: ");
			String choice = Agloco.in.readLine();
			if (!"yes".equalsIgnoreCase(choice)) {
				_log.info("you have cancel the batch sending!");
				return;
			}

			long pageNumber = 1;
			Long total = lister.getTotalNumber();
			if (total != null) {
				long totalNumber = total.longValue();
				if (totalNumber == 0) {
					_log.info("there is no users");
					return;
				}
				_log.info("there are " + totalNumber + " users need to be sent the email!");
				System.out.println("there are " + totalNumber	+ " users need to be sent the email!");
	
				pageNumber = totalNumber / pageSize + 1;
				_log.info("there are " + pageNumber + " batches!");
				System.out.println("there are " + pageNumber + " batches!");
			}
			for (int i = 1; i <= pageNumber; i++) {
				list = lister.getList(new Long(i));
				if (list == null || list.size() < 1) {
					_log.info("there are no users need to be sent email and the job will exit");
					break;
				}
	
				_log.info("there are " + list.size() + " users need to be sent email this batch");
				System.out.println("there are " + list.size() + " users need to be sent email this batch");
	
				for (Iterator it = list.iterator(); it.hasNext();) {
					AGMember m = resolver.getAGMember(it.next());
					if (m != null) {
						try {
							if (m.getMemberCode().equals("AGLOADMIN")
									|| m.getMemberCode().equals("AGLOSERV")
									|| m.getMemberCode().equals("AGLOTEST")
									|| m.getMemberCode().equals("AGLOTESTC")
									|| m.getMemberCode().startsWith("AGLOADM")
									|| m.getStatus().equals(AGMember.MEMBER_STATUS_INACTIVE)) {
								continue;
							}
							if (sentSet.contains(m.getEmailAddress().trim().toLowerCase())) {
								skip++;
								// write email in filter file
								/*
								bw_u.write(m.getEmailAddress());
								bw_u.newLine();
								bw_u.flush();
								*/
							} else {
								MailServiceUtil.sendMail(m, null, articleId, null, null);
								success++;
								_log.info("send " + articleId
										+ " email success, userId/memberId = " + m.getUserId()
										+ "/" + m.getMemberId());
								// write email in file
								bw_s.write(m.getEmailAddress());
								bw_s.newLine();
								bw_s.flush();
								if (success % pageSize == 0) {
									_log.info("by now has sent " + articleId
											+ " emails(success/failure/skip): " + success + "/"
											+ failure + "/" + skip);
									System.out.println("by now has sent " + articleId
											+ " emails(success/failure/skip):" + success + "/"
											+ failure + "/" + skip);
									_log.info("begin sleeping ..." + interval + " minutes");
									System.out.println("begin sleeping ..." + interval + " minutes");
									Thread.sleep(sleepTime);
								}
							}
						} catch (Exception e) {
							failure++;
							_log.error("send " + articleId + " email failed, userId = " + m.getUserId(), e);
						}
					}
				}
				list.clear();
			}
			_log.info("total sent " + articleId + " emails(success/failure/skip): "
					+ success + "/" + failure + "/" + skip);
			System.out.println("total sent " + articleId
					+ " emails(success/failure/skip): " + success + "/" + failure + "/"	+ skip);
		} catch (IOException e) {
			_log.error("operating file error!", e);
			System.out.println(e);
		} catch (Exception e) {
			_log.error("query members error!", e);
			System.out.println(e);
		} finally {
			try {
				// close write success file...
				if (bw_s != null) {
					bw_s.close();
				}
				if (fw_s != null) {
					fw_s.close();
				}

				// close write unsent file
				if (bw_u != null) {
					bw_u.close();
				}
				if (fw_u != null) {
					fw_u.close();
				}
			} catch (IOException e) {
				_log.error("write file error!", e);
			}
		}
	}

}
