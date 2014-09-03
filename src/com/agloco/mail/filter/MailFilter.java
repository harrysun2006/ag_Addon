package com.agloco.mail.filter;

import java.util.Map;

import com.agloco.mail.MailMessage;

/**
 * 
 * @author terry_zhao
 *
 */
public interface MailFilter {

	public final static String MAP_KEY_MEMBER = "member";
	public final static String MAP_KEY_COMPANY = "company";
	public final static String MAP_KEY_ARTICLE_ID = "article";
	public final static String MAP_KEY_LOCAL = "local";
	
	public void doFilter(MailMessage msg,Map map);
	
}
