package com.agloco.mail.filter;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.agloco.exception.MailFilterException;
import com.agloco.mail.MailMessage;
import com.agloco.mail.Part;
import com.agloco.util.ConfigUtil;
import com.liferay.portal.model.Company;

public class MailTypeFilter implements MailFilter {
	
	private final static String MAIL_ARTICLE_TYPE = "email.article.type.";
	
	public void doFilter(MailMessage msg, Map map) {
		try{
			String articleId = (String) map.get(MailFilter.MAP_KEY_ARTICLE_ID);
			Company company = (Company) map.get(MailFilter.MAP_KEY_COMPANY);
			String type  = ConfigUtil.getConfig(MAIL_ARTICLE_TYPE+articleId, company.getCompanyId());
			if(msg.getInlines().size() <= 0 && msg.getAttachmentCount() <= 0) {
				if(StringUtils.isNotBlank(type)) msg.setType(Part.Type.valueOf(type));
			}
		}
		catch(Exception e){
			throw new MailFilterException();
		}

	}

}
