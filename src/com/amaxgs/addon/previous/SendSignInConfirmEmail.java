package com.amaxgs.addon.previous;

import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.agloco.model.AGMember;
import com.agloco.service.util.MailServiceUtil;
import com.agloco.service.util.MemberServiceUtil;

class SendSignInConfirmEmail {

	private static Log _log = LogFactory.getLog(SendSignInConfirmEmail.class);

	public static void sendEmail() {

		String[] emails = new String[] { "bayour5@yahoo.com",
				"ten7nsyu_sigizan3n@yahoo.co.jp", "glenda.hayes7@ntlworld.com",
				"decius@thetechzone.com", "sesilia_d_k@yahoo.com",
				"anton_jansen@versatel.nl", "charlso1@yahoo.com",
				"jlurock@fastmail.fm", "gloria_geamanu@yahoo.com", "wmrs99@gmail.com",
				"admin@neverest.eu", "sasha_coolhunk3@yahoo.com",
				"naveenreddyxclusive@yahoo.co.in", "admin@fidforum.ig3.net",
				"rahulk111000@yahoo.co.in", "lorim215@yahoo.com",
				"nagrockz143@yahoo.com", "liminghyx@qq.com",
				"aravindan.blackknight@gmail.com", "silentrob567@yahoo.com",
				"laamir@telkom.net", "agniassociates@gmail.com", "knizamb@gmail.com",
				"wangwen2046@sohu.com", "c.fregoni@tiscali.it",
				"white_paradise04@yahoo.com", "xever86@yahoo.com",
				"tanuj.arneja@gmail.com", "zy@ks-kawaguchi.com",
				"kersy_bharucha@yahoo.co.in" };
		String msg;
		int s, f, t;
		s = f = 0;
		t = emails.length;
		for (int i = 0; i < emails.length; i++) {
			AGMember member = MemberServiceUtil.getAGMemberByEmail(emails[i].trim()
					.toLowerCase());
			try {
				MailServiceUtil.sendFirstSigninMail(member, Locale.US);
				msg = "@@send sign in confirm email successfully: " + member.getEmailAddress();
				_log.info(msg);
				System.out.println(msg);
				s++;
			} catch (Exception e) {
				msg = "@@send sign in confirm email failed: " + member.getEmailAddress();
				_log.error(msg, e);
				System.out.println(msg);
				f++;
			} 
		}
		msg = "@@send sign in confirm email to list(success/failed/total): " + s + "/" + f + "/" + t;
		_log.info(msg);
		System.out.println(msg);
	}
	
}
