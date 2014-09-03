package com.amaxgs.addon.previous;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.agloco.Constants;
import com.agloco.model.AGMember;
import com.agloco.service.util.MailServiceUtil;
import com.agloco.service.util.MemberServiceUtil;
import com.amaxgs.addon.Agloco;

class SendEmailToStillNotLogin {

	private static Log _log = LogFactory.getLog(SendEmailToStillNotLogin.class);

	public static void sendEmail() {
		
		int i = 0;
		System.out.println("======================= begin to send email to member still not login =======================");
		try {
			//sent to member import from excel but still not login
			List list = MemberServiceUtil.listAgMemberNotLogin();
			System.out.print(list.size() + " members will receive the email, are you sure?[n]");
			String answer = Agloco.in.readLine();
			if("yes".equalsIgnoreCase(answer)) _log.info("start sending emails... ...");
			else return;
			if(list != null && list.size() > 0){
				for(Iterator it = list.iterator(); it.hasNext();){
					AGMember m = (AGMember) it.next();
					String info = info(m);
					try{
						MailServiceUtil.sendSignupMail(m, Locale.getDefault(), Constants.ARTICLEID_AGS_SIGNUP_EMAIL);
						System.out.println("send email to " + info + " successfully!");
					}
					catch(Exception e){
						System.out.println("send email to " + info + " failed!");
					}
					++i;
				}
			}
			_log.info(i + " emails have sent successfully!");
		}catch(Exception e){
			e.printStackTrace();
		} 
	}

	private static String info(AGMember m) {
		StringBuffer sb = new StringBuffer();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		if(m != null) {
			String createDate = (m.getCreateDate() == null) ? "" : df.format(m.getCreateDate().getTime());
			sb.append(" mt[")
				.append(m.getMemberCode())
				.append(", ").append(m.getEmailAddress())
				.append(", ").append(createDate)
				.append("]");
		}
		return sb.toString();
	}
}
