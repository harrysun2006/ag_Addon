package com.agloco.mail;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.Header;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Convert MailMessage Object to Message Object
 * @author Harry Sun
 */
public class MessageWriter {

	private static Log log = LogFactory.getLog(MessageWriter.class);
	private static Session session = Session.getDefaultInstance(System.getProperties(), null);

	public MessageWriter() {
	}

	/**
	 * Convert a MailMessage object to a Message object
	 * @param mail
	 * @return
	 * @throws MessagingException
	 * @throws IOException
	 */
	public Message writeMail(MailMessage mail) throws MessagingException, IOException {
		Message message = new MimeMessage(session);
		writeMail(mail, message);
		return message;
	}

	/**
	 * Convert a MailMessage object to a Message object
	 * 1. Text:
	 * 		text(mail)
	 * 2. HTML:
	 * 		related(mail)
	 * 				|-html
	 * 				|_inlines(ignored if none)
	 * 3. Attachments:
	 * 		mixed(mail)
	 * 				|_attachments
	 * 4. Text + HTML:
	 * 		related(mail)
	 * 				|-alternative
	 * 				|			|-text
	 * 				|			|_html
	 * 				|_inlines(ignored if none)
	 * 5. Text + Attachments:
	 * 		mixed(mail)
	 * 				|-text
	 * 				|_attachments
	 * 6. HTML + Attachments:
	 * 		mixed(mail)
	 * 				|-related
	 * 				|			|-html
	 * 				|			|_inlines(ignored if none)
	 * 				|_attachments
	 * 7. Text + HTML + Attachments:
	 * 		mixed(mail)
	 * 				|-related
	 * 				|			|_alternative
	 * 				|			|			|-text
	 * 				|			|			|_html
	 * 				|			|_inlines(ignored if none)
	 * 				|_attachments
	 * rules:
	 * mp = null;
	 * if(html.size > 0) {
	 * 	if(text.size > 0) {
	 * 		mp = new multipart("related");
	 * 		alternative = new multipart("alternative");
	 * 		alternative.add(text);
	 * 		alternative.add(html);
	 * 		mp.add(alternative);
	 *	} else if(inlines.count > 0) {
	 *	} else if(attachments.count > 0) {
	 *	} else {
	 *		message.setContent(html, mail.getContentType());
	 *	}
	 *	if(inlines.count > 0) {
	 *	 	if(mp == null) {
	 *			mp = new multipart("related");
	 *			mp.add(html);
	 *		}
	 *		mp.addAll(inlines);
	 *	}
	 * } 
	 * if(text.size > 0) {
	 * 	if(html.size > 0) {
	 * 	} else if(inlines.count > 0) {
	 * 	} else if(attachment.count > 0) {
	 * 		mp = new multipart("mixed");
	 * 		mp.add(text);
	 * 	} else {
	 * 		message.setContent(text, mail.getContentType());
	 * }
	 * if(attachments.count > 0) {
	 * 		if(mp != null && html.size > 0) {
	 * 			temp = mp;
	 * 			mp = new multipart("mixed");
	 * 			if(temp != null) mp.add(temp);
	 * 		}
	 * 		mp.addAll(attachments);
	 * }
	 * message.headers = mail.headers;
	 * if(mp != null) {
	 * 		message = mp;
	 * }
	 * @param mail
	 * @param message
	 * @throws MessagingException
	 * @throws IOException
	 */
	public void writeMail(MailMessage mail, Message message) throws MessagingException, IOException {
		try {
			Part text = mail.getPart(Part.Type.TEXT);
			Part html = mail.getPart(Part.Type.HTML);
			Part part = null;
			int textSize = mail.getTextSize();
			int htmlSize = mail.getHTMLSize();
			Collection inlines = mail.getInlines();
			Collection attachments = mail.getAttachments();
			Multipart mp = null;
			if(htmlSize > 0) {
				if(textSize > 0) {
					mp = new MimeMultipart("related");
					Multipart alternative = new MimeMultipart("alternative");
					alternative.addBodyPart(createBodyPart(mail, text));
					alternative.addBodyPart(createBodyPart(mail, html));
					BodyPart bp = new MimeBodyPart();
					bp.setContent(alternative);
					mp.addBodyPart(bp);
				} else if(inlines.size() > 0) {
				} else if(attachments.size() > 0) {
				} else {
					if(mail.getType() == null || Part.Type.UNKNOWN.equals(mail.getType())) mail.setType(Part.Type.HTML);
					message.setContent(html.getHTML(), mail.getContentType());
				}
				if(inlines.size() > 0) {
					if(mp == null) {
						mp = new MimeMultipart("related");
						mp.addBodyPart(createBodyPart(mail, html));
					}
					for(Iterator it = inlines.iterator(); it.hasNext(); ) {
						part = (Part)it.next();
						mp.addBodyPart(createBodyPart(mail, part));
					}
				}
			} 
			if(textSize > 0) {
				if(htmlSize > 0) {
				} else if(inlines.size() > 0) {
				} else if(attachments.size() > 0) {
					mp = new MimeMultipart("mixed");
					mp.addBodyPart(createBodyPart(mail, text));
				} else {
					if(mail.getType() == null || Part.Type.UNKNOWN.equals(mail.getType())) mail.setType(Part.Type.TEXT);
					message.setContent(text.getText(), mail.getContentType());
				}
			}
			if(attachments.size() > 0) {
				if(mp != null && htmlSize > 0) {
					Multipart temp = mp;
					mp = new MimeMultipart("mixed");
					if(temp != null) {
						BodyPart bp = new MimeBodyPart();
						bp.setContent(temp);
						mp.addBodyPart(bp);
					}
				} 
				for(Iterator it = attachments.iterator(); it.hasNext(); ) {
					part = (Part)it.next();
					mp.addBodyPart(createBodyPart(mail, part));
				}
			}
			composeMessageHeaders(mail, message);
			if(mp != null) message.setContent(mp);
			try {
				mail.read(message);
			} catch(Exception e) {}
		} catch(MessagingException me) {
			throw(me);
		} 
	}

	/**
	 * Convert a part object to a BodyPart object
	 * @param mail
	 * @param part
	 * @return
	 * @throws MessagingException
	 */
	private BodyPart createBodyPart(MailMessage mail, Part part) throws MessagingException {
		if(part == null) return null;
		String charset;
		String encoding;
		String fileName;
		ContentType contentType = new ContentType();
		Part.Type type;
		String text;
		InputStream is;
		InputStreamDataSource isds;
		DataHandler dh;
		BodyPart bp = new MimeBodyPart();

		charset = mail.getCharset();
		encoding = mail.getContentTransferEncoding();
		if(charset == null) charset = part.getCharset();
		if(encoding == null) encoding = part.getContentTransferEncoding();
		if(charset == null) charset = Part.DEFAULT_CHARSET;
		if(encoding == null) encoding = Part.DEFAULT_ENCODING;
		type = part.getType();
		if(Part.Type.TEXT.equals(type) || Part.Type.HTML.equals(type)) {
			if(Part.Type.TEXT.equals(type)) text = part.getText();
			else text = part.getHTML();
			contentType = new ContentType(type.toString());
			contentType.setParameter(Part.CONTENT_TYPE_CHARSET, charset);
			bp.setContent(text, contentType.toString());
			bp.setHeader(Part.CONTENT_TYPE, contentType.toString());
			bp.setHeader(Part.CONTENT_TRANSFER_ENCODING, encoding);
		} else if(Part.Type.ATTACHMENT.equals(type) || Part.Type.INLINE.equals(type)) {
			is = part.getInputStream();
			contentType = new ContentType("application/octet-stream");
			encoding = "base64";//attachment always uses base64
			if(Part.Type.INLINE.equals(type)) bp.addHeader(Part.CONTENT_ID, part.getContentId());
			fileName = part.getFileName();
			if(fileName != null) {
				try {
					fileName = MimeUtility.encodeText(fileName, charset, "B");
				} catch(UnsupportedEncodingException uee) {
					log.warn("can NOT encode \"" + fileName + "\" into \"" + encoding + "\" as \"" + charset + "\"", uee);
				} 
			}
			contentType.setParameter(Part.CONTENT_TYPE_FILENAME, fileName);
			isds = new InputStreamDataSource(is);
			dh = new DataHandler(isds);
			bp.setDataHandler(dh);
			bp.setHeader(Part.CONTENT_TYPE, contentType.toString());
			bp.setHeader(Part.CONTENT_TRANSFER_ENCODING, encoding);
			bp.setHeader(Part.CONTENT_DISPOSITION, type + "; filename=\"" + fileName + "\"");
		}
		return bp;
	}

	/**
	 * handle all headers of mail
	 * @param mail
	 * @param message
	 * @throws MessagingException
	 */
	private void composeMessageHeaders(MailMessage mail, Message message) throws MessagingException {
		if(mail == null || message == null) return;
		String charset = mail.getCharset();
		String encoding = mail.getContentTransferEncoding();
		if("quoted-printable".equalsIgnoreCase(encoding)) encoding = "Q";
		else encoding = "B";

		Enumeration names = mail.getHeaderNames();
		String name;
		String[] values;
		Header header;
		StringBuffer r = new StringBuffer();
		if(log.isTraceEnabled()) r.append("read mail headers: \n");
		while(names.hasMoreElements()) {
			name = (String) names.nextElement();
			values = mail.getHeader(name);
			for(int i = 0; i < values.length; i++) {
				header = new Header(name, values[i]);
				composeHeader(header, message, charset, encoding);
				if(log.isTraceEnabled()) r.append(header.getName()).append(": ").append(header.getValue()).append("\n");
			}
		}
		log.trace(r.toString());
	}

	/**
	 * encode unicode part of the header
	 * @param header
	 * @param message
	 */
	private void composeHeader(Header header, Message message, String charset, String encoding) throws MessagingException {
		String value = header.getValue();
		if(value == null) value = "";
		try {
			if(value.getBytes().length != value.length()) {
			//has unicode
				value = MimeUtility.encodeText(value, charset, encoding);
			}
			message.addHeader(header.getName(), value);
		} catch(UnsupportedEncodingException uee) {
			log.warn("can NOT encode the address text", uee);
		}
	}

}
