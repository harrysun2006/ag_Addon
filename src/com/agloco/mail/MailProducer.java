package com.agloco.mail;

import java.io.Serializable;

import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.agloco.model.AGMailMessage;
import com.agloco.service.util.CommonServiceUtil;
import com.agloco.util.ConfigUtil;
import com.agloco.util.MiscUtil;
import com.liferay.portal.util.PropsUtil;
import com.liferay.util.JMSUtil;
import com.liferay.util.mail.MailEngineException;

public class MailProducer
{

	private final static Log log = LogFactory.getLog(MailProducer.class);

	private static boolean consumerStart = true;

	static
	{
		MailConsumer consumer = new MailConsumer();

		try
		{
			consumer.consume();
		}
		catch (Exception e)
		{
			// TODO: handle exception
			consumerStart = false;
			log.error("MailConsumer init error!");
			
			// If consumer not startup, send mail to administrator
			try
			{
				MailMessage mailMessage = new MailMessage();
				String to = ConfigUtil.getConfig("system.abnormal.manager", PropsUtil.get("default.company.id"));
				String fromName = PropsUtil.get("admin.email.from.name");
				String fromAddress = PropsUtil.get("admin.email.from.address");
				mailMessage.setContentType("text/plain");
				mailMessage.setRecipient(to);
				mailMessage.setFrom(fromName+ " <" + fromAddress + ">");
				mailMessage.setPriority(MailMessage.Priority.HIGH);
				mailMessage.addHeader("X-Priority", "1 (Highest)");
				mailMessage.setSubject("DERBY DB CRASHED");
				
				String server = MiscUtil.getLocalIp();
				StringBuffer body = new StringBuffer();
				body.append("Derby DB on server: "+server+" couldn't start up!\n");
				body.append("Please resolve the problem as soon as you receive this mail!");
				
				mailMessage.setText(body.toString());
				
				MailEngine.send(mailMessage);
			}
			catch (MailEngineException e1)
			{
				// TODO Auto-generated catch block
				log.error("Send emergent mail failed!",e1);
			}
		}
	}

	public static void produce(Serializable obj)
	{
		QueueConnection con = null;
		QueueSession session = null;
		QueueSender sender = null;

		if (isConsumerStart())
		{
			try
			{
				QueueConnectionFactory qcf = MailQCFUtil.getQCF();
				Queue queue = MailQueueUtil.getQueue();

				con = qcf.createQueueConnection();
				session = con.createQueueSession(false,
						QueueSession.AUTO_ACKNOWLEDGE);
				sender = session.createSender(queue);

				ObjectMessage objMsg = session.createObjectMessage();
				objMsg.setObject(obj);
				sender.setDeliveryMode(DeliveryMode.PERSISTENT);
				sender.send(objMsg);
			}
			catch (JMSException jmse)
			{
				AGMailMessage msg = new AGMailMessage();
				msg.setSerialiableMsg(obj);
				CommonServiceUtil.addAGMailMessage(msg);
				if (log.isErrorEnabled())
				{
					log.error("produce mail error, put mail msg into mysql! ",
							jmse);
				}
			}
			finally
			{
				JMSUtil.cleanUp(con, session, sender);
			}
		}
		else
		{
			AGMailMessage msg = new AGMailMessage();
			msg.setSerialiableMsg(obj);
			CommonServiceUtil.addAGMailMessage(msg);
			JMSUtil.cleanUp(con, session, sender);
		}
	}

	public static boolean isConsumerStart()
	{
		return consumerStart;
	}

}
