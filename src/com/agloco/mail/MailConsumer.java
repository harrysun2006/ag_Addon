package com.agloco.mail;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.mail.internet.InternetAddress;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.agloco.model.AGMailMessage;
import com.agloco.service.util.CommonServiceUtil;
import com.liferay.portal.kernel.util.MethodInvoker;
import com.liferay.portal.kernel.util.MethodWrapper;
import com.liferay.portal.kernel.util.StackTraceUtil;
import com.liferay.portal.util.PropsUtil;

public class MailConsumer  implements MessageListener {

	public void consume() throws Exception 
	{
		QueueConnectionFactory qcf = MailQCFUtil.getQCF();
		QueueConnection con = qcf.createQueueConnection();

		QueueSession session = con.createQueueSession(
			false, Session.AUTO_ACKNOWLEDGE);
		Queue queue = (Queue)MailQueueUtil.getQueue();
		QueueReceiver subscriber = session.createReceiver(queue);
		subscriber.setMessageListener(this);
		con.start();
	}

	public void onMessage(Message msg) {
		
		ObjectMessage objMsg = (ObjectMessage)msg;
		try {
			Object obj = objMsg.getObject();

			if (obj instanceof MailMessage) {
				_onMessage((MailMessage)obj);
			} else if (obj instanceof com.liferay.util.mail.MailMessage) {
				_onMessage((com.liferay.util.mail.MailMessage)obj);
			}	else if (obj instanceof MethodWrapper) {
				_onMessage((MethodWrapper)obj);
			}
		}
		catch (Exception e) {
			AGMailMessage mailMsg = new AGMailMessage();
			try {
				mailMsg.setSerialiableMsg(objMsg.getObject());
				CommonServiceUtil.addAGMailMessage(mailMsg);
			} catch (JMSException e1) {
				if(_log.isErrorEnabled()){
					_log.error("save send failure email's msg error!", e1);
				}
			}
			
			if(_log.isErrorEnabled()){
				_log.error("consume and send mail error, put msg into mysql! ",e);
			}
//			_log.error(StackTraceUtil.getStackTrace(e));
		}
	}

	private void _onMessage(MailMessage mailMessage) throws Exception {
		MailEngine.send(mailMessage);
	}

	private void _onMessage(com.liferay.util.mail.MailMessage mailMessage) throws Exception {
		InternetAddress[] auditTrail = InternetAddress.parse(
			PropsUtil.get(PropsUtil.MAIL_AUDIT_TRAIL));

		if (auditTrail.length > 0) {
			InternetAddress[] bcc = mailMessage.getBCC();

			if (bcc != null) {
				InternetAddress[] allBCC = new InternetAddress[
					bcc.length + auditTrail.length];

				System.arraycopy(bcc, 0, allBCC, 0, bcc.length);

				System.arraycopy(
					auditTrail, 0, allBCC, bcc.length, auditTrail.length);

				mailMessage.setBCC(allBCC);
			}
			else {
				mailMessage.setBCC(auditTrail);
			}
		}
		MailEngine.send(mailMessage);
	}

	private void _onMessage(MethodWrapper methodWrapper) throws Exception {
		MethodInvoker.invoke(methodWrapper);
	}

	private static Log _log = LogFactory.getLog(MailConsumer.class);

}
