package com.agloco.mail;

import javax.jms.QueueConnectionFactory;

import org.activemq.pool.PooledConnectionFactory;
import org.springframework.context.ApplicationContext;

import com.liferay.portal.spring.util.SpringUtil;

public class MailQCFUtil {

	public static String NAME = "com.agloco.mail.MailQCF";

	public static QueueConnectionFactory getQCF() {
		ApplicationContext ctx = SpringUtil.getContext();

		PooledConnectionFactory pcf =
			(PooledConnectionFactory)ctx.getBean(NAME);

		return (QueueConnectionFactory)pcf.getConnectionFactory();
	}
}
