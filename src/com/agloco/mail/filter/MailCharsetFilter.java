package com.agloco.mail.filter;

import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.agloco.exception.MailFilterException;
import com.agloco.mail.MailMessage;
import com.agloco.model.AGMember;
import com.agloco.util.ConfigUtil;
import com.liferay.portal.model.Company;

public class MailCharsetFilter implements MailFilter {


	private final static String EMAIL_CHARSET = "email.charset."; 
	private final static String EMAIL_AT = "@";
	
	public void doFilter(MailMessage msg, Map map) {
		try{
			AGMember m = (AGMember)map.get(MailFilter.MAP_KEY_MEMBER);
			Company company = (Company)map.get(MailFilter.MAP_KEY_COMPANY);
			Locale local = (Locale)map.get(MailFilter.MAP_KEY_LOCAL);
			if(local != null && Locale.CHINESE.getLanguage().equals(local.getLanguage())){
				String emailAddress = m.getEmailAddress();
				String charset = ConfigUtil.getConfig(EMAIL_CHARSET+emailAddress.substring(emailAddress.indexOf(EMAIL_AT)+1, emailAddress.length()), company.getCompanyId());
				if(StringUtils.isNotBlank(charset)){
					msg.setCharset(charset);
				}	
			}
				
		}
		catch(Exception e){
			throw new MailFilterException();
		}
		

	}

}
