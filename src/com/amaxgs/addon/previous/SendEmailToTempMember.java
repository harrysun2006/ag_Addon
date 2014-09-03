package com.amaxgs.addon.previous;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.agloco.Constants;
import com.agloco.model.AGMemberTemp;
import com.agloco.service.util.MailServiceUtil;
import com.agloco.service.util.MemberServiceUtil;
import com.amaxgs.addon.Agloco;

class SendEmailToTempMember {

	private static Log _log = LogFactory.getLog(SendEmailToTempMember.class);

	public static void sendEmail() {
		System.out.println("======================= begin to send email to temp member =======================");
		try {
			System.out.print("begin datetime(yyyy-MM-dd HH:mm)[null]: ");
			String begin = Agloco.in.readLine();
			System.out.print("begin datetime(yyyy-MM-dd HH:mm)[now]: ");
			String end = Agloco.in.readLine();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date beginDate = null;
			if(begin == null || begin.trim().length() == 0) beginDate = null;
			else {
				beginDate = df.parse(begin);
			}
			Date endDate = null;
			if(end == null || end.trim().length() == 0) endDate = new Date(); 
			else {
				endDate = df.parse(end);
			}
			System.out.println("1. article AGS_SIGNUP_EMAIL");
			System.out.println("2. article ARTICLEID_SIGNUP_EMAIL_TEMP1");
			System.out.print("choose article[2]: ");
			String article = Agloco.in.readLine();
			if(article == null || article.trim().length() == 0) article = Constants.ARTICLEID_AGS_SIGNUP_EMAIL_TEMP1;
			else if("1".equalsIgnoreCase(article)) article = Constants.ARTICLEID_AGS_SIGNUP_EMAIL;
			else if("2".equalsIgnoreCase(article)) article = Constants.ARTICLEID_AGS_SIGNUP_EMAIL_TEMP1;
			else article = Constants.ARTICLEID_AGS_SIGNUP_EMAIL_TEMP1;
			//sent to temp member
			List list = MemberServiceUtil.listAgMemberTemp(beginDate, endDate);
			System.out.print(list.size() + " temporary members will receive the email, are you sure?[n]");
			String answer = Agloco.in.readLine();
			if("yes".equalsIgnoreCase(answer)) _log.info("start sending emails... ...");
			else return;
			int suc = 0, fail = 0, total = 0;
			if(list != null && list.size() > 0){
				for(Iterator it = list.iterator(); it.hasNext();){
					AGMemberTemp mt = (AGMemberTemp) it.next();
					String info = info(mt);
					try{
						MailServiceUtil.sendSignupMail(mt, Locale.getDefault(), article);
						info = "send email to " + info + " successfully!";
						System.out.println(info);
						_log.info(info);
						suc++;
					} catch(Exception e) {
						info = "send email to " + info + " failed!";
						System.out.println(info);
						_log.error(info, e);
						fail++;
					}
					total++;
				}
			}
			_log.info(total + " emails have been sent, " + suc + " success, " + fail + " failed!");
		} catch(Exception e) {
			e.printStackTrace();
		} 
	}

	private static String info(AGMemberTemp mt) {
		StringBuffer sb = new StringBuffer();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		if(mt != null) {
			String createDate = (mt.getCreateDate() == null) ? "" : df.format(mt.getCreateDate().getTime());
			sb.append(" mt[")
				.append(mt.getMemberCode())
				.append(", ").append(mt.getEmailAddress())
				.append(", ").append(createDate)
				.append("]");
		}
		return sb.toString();
	}
}
