package com.agloco.mail;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.liferay.util.GetterUtil;
import com.liferay.util.Validator;
import com.liferay.util.mail.MailEngineException;

public class MailEngine extends com.liferay.util.mail.MailEngine {

	public static void send(
			InternetAddress from, InternetAddress[] to, InternetAddress[] cc,
			InternetAddress[] bcc, String subject, String body,
			boolean htmlFormat)
		throws MailEngineException {
		MailMessage mail = new MailMessage(from, to, cc, bcc, subject, body, htmlFormat);
		send(mail);
	}

	public static void send(MailMessage mail) throws MailEngineException {
		try {
			long start = 0;
			if (_log.isInfoEnabled()) start = System.currentTimeMillis();
			Session session = getSession();
			MessageWriter writer = new MessageWriter();
			Message msg = writer.writeMail(mail);
			_send(session, msg);
			if (_log.isInfoEnabled()) {
				long end = System.currentTimeMillis();
				StringBuffer sb = new StringBuffer();
				sb.append("send email success [")
					.append("From: ").append(mail.getSender())
					.append(", To: ").append(mail.getRecipient())
					.append(", CC: ").append(mail.getCarbonCopy())
					.append(", BCC: ").append(mail.getBlindCarbonCopy())
					.append(", Subject: ").append(mail.getSubject())
					.append(", Content-Type: ").append(mail.getContentType())
					.append(", Size: ").append(mail.getSize())
					.append("], using ")
					.append((end - start)).append(" ms!");
				_log.info(sb.toString());
			}
			if (_log.isDebugEnabled()) {
				_log.debug("Text:\n" + mail.getText());
				_log.debug("HTML:\n" + mail.getHTML());
				_log.debug("Attachment Count: " + mail.getAttachmentCount());
			}
		} catch (Exception e) {
			StringBuffer sb = new StringBuffer();
			sb.append("send email failed [")
				.append("From: ").append(mail.getSender())
				.append(", To: ").append(mail.getRecipient())
				.append(", CC: ").append(mail.getCarbonCopy())
				.append(", BCC: ").append(mail.getBlindCarbonCopy())
				.append(", Subject: ").append(mail.getSubject())
				.append(", Content-Type: ").append(mail.getContentType())
				.append(", Size: ").append(mail.getSize())
				.append("]");
			_log.error(sb.toString(), e);
			throw new MailEngineException(e);
		}
	}

	protected static void _send(Session session, Message msg)
		throws MessagingException {
		boolean smtpAuth = GetterUtil.getBoolean(session.getProperty("mail.smtp.auth"), false);
		String smtpHost = session.getProperty("mail.smtp.host");
		String user = session.getProperty("mail.smtp.user");
		String password = session.getProperty("mail.smtp.password");

		if (smtpAuth && Validator.isNotNull(user) &&
			Validator.isNotNull(password)) {
			Transport transport = session.getTransport("smtp");
			transport.connect(smtpHost, user, password);
			transport.sendMessage(msg, msg.getAllRecipients());
			transport.close();
		}	else {
			Transport.send(msg);
		}
	}

	private static Log _log = LogFactory.getLog(MailEngine.class);

}
