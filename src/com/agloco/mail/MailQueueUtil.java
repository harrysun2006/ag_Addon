package com.agloco.mail;

import javax.jms.Queue;

import org.springframework.context.ApplicationContext;

import com.liferay.portal.spring.util.SpringUtil;

public class MailQueueUtil {

	public static String NAME = "com.agloco.mail.MailQueue";

	public static Queue getQueue() {
		ApplicationContext ctx = SpringUtil.getContext();

		return (Queue)ctx.getBean(NAME);
	}

}
