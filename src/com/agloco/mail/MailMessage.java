package com.agloco.mail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.agloco.util.MailUtil;
import com.agloco.util.StringUtil;

public class MailMessage extends AbstractPart {

	private static final long serialVersionUID = -2206421820807829684L;
	private static final Log log = LogFactory.getLog(MailMessage.class);
	private static final String DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss Z";
	private static final String CID_BASE = "0123456789-abcdef@";
	private static final Pattern HTML_SRC_PATTERN = Pattern.compile("<\\w+\\s+[^>]*(background|src|value|content)\\s*=\\s*(['|\"]?)([^>'\"]*)\\2[^>]*>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
	private static final Pattern HTML_CHARSET_PATTERN = Pattern.compile("charset\\s*=\\s*\"*([^\"\\r\\n]*)\"*");
	private static final int BUFFER_SIZE = 4096;

	private String baseContentId = StringUtil.random(CID_BASE, 18);
	private List parts = new ArrayList();
	private StringBuffer source = new StringBuffer();
	private StringBuffer rawHead = null;

	public MailMessage() {
		setCharset(DEFAULT_CHARSET);
		setEncoding(DEFAULT_ENCODING);
	}

	public MailMessage(InternetAddress from, InternetAddress to, String subject,
			String body) throws PartException {
		this(from, to, subject, body, false);
	}

	public MailMessage(InternetAddress from, InternetAddress to, String subject,
			String body, boolean htmlFormat) throws PartException {
		this(from, new InternetAddress[] {to}, null, null, subject, body, htmlFormat);
	}

	public MailMessage(InternetAddress from, InternetAddress[] to,
			InternetAddress[] cc, InternetAddress[] bcc, String subject, String body) throws PartException {
		this(from, to, cc, bcc, subject, body, false);
	}

	public MailMessage(InternetAddress from, InternetAddress[] to,
			InternetAddress[] cc, InternetAddress[] bcc, String subject, String body,
			boolean htmlFormat) throws PartException {
		this();
		setFrom(from);
		setRecipients(Message.RecipientType.TO, to);
		setRecipients(Message.RecipientType.CC, cc);
		setRecipients(Message.RecipientType.BCC, bcc);
		setSubject(subject);
		if (htmlFormat) {
			setHTML(body);
			setType(Type.HTML);
		} else {
			setText(body);
			setType(Type.TEXT);
		}
	}

	/**
	 * from
	 * @param addresses
	 */
	public void addFrom(InternetAddress[] addresses) {
		if (addresses != null && addresses.length > 0) {
			String from = getHeaderValue(Header.FROMS);
			if(from == null) from = MailUtil.getAddressText(addresses);
			else from = from + ", " + MailUtil.getAddressText(addresses);
			setHeader(Header.FROM, from);
		}
	}

	public void addFrom(InternetAddress address) {
		addFrom(new InternetAddress[] { address });
	}

	public InternetAddress getFrom() {
		InternetAddress[] froms = getFroms();
		InternetAddress from = null;
		if(froms != null && froms.length > 0) from = froms[0];
		return from;
	}

	public InternetAddress[] getFroms() {
		String from = getHeaderValue(Header.FROMS);
		return MailUtil.getAddress(from);
	}

	public void setFrom(InternetAddress address) {
		setHeader(Header.FROM, MailUtil.getAddressText(address));
	}

	public void setFrom(String address) {
		InternetAddress[] froms = MailUtil.getAddress(address);
		setHeader(Header.FROM, MailUtil.getAddressText(froms));
	}

	public String getSender() {
		return MailUtil.getAddressText(getFroms());
	}

	public void setSender(String s) {
		setFrom(s);
	}

	public String getSenderName() {
		return MailUtil.getPersonal(getFrom());
	}

	public String getSenderAddress() {
		return MailUtil.getAddress(getFrom());
	}

	public String getSenderText() {
		return MailUtil.getText(getFrom());
	}

	/**
	 * recipients
	 * @param type
	 * @param address
	 */
	public void addRecipient(Message.RecipientType type, InternetAddress address) {
		addRecipients(type, new InternetAddress[] { address });
	}

	public void addRecipients(Message.RecipientType type, InternetAddress[] addresses) {
		if (addresses != null && addresses.length > 0) {
			String headName = type.toString();
			String recipients = getHeaderValue(headName);
			if(recipients == null) recipients = MailUtil.getAddressText(addresses);
			else recipients = recipients + ", " + MailUtil.getAddressText(addresses);
			setHeader(headName, recipients);
		}
	}

	public InternetAddress[] getRecipients(Message.RecipientType type) {
		String recipients = getHeaderValue(type.toString());
		return MailUtil.getAddress(recipients);
	}

	public InternetAddress[] getAllRecipients() {
		StringBuffer recipients = new StringBuffer();
		for(int i = 0; i < Header.RECIPIENTS.length; i++) {
			recipients.append(getHeaderValue(Header.RECIPIENTS[i])).append(",");
		}
		return MailUtil.getAddress(recipients.toString());
	}

	public void setRecipient(Message.RecipientType type, InternetAddress address) {
		InternetAddress[] addresses = new InternetAddress[] { address };
		setRecipients(type, addresses);
	}

	public void setRecipients(Message.RecipientType type, InternetAddress[] addresses) {
		setHeader(type.toString(), MailUtil.getAddressText(addresses));
	}

	public String getRecipient() {
		InternetAddress[] tos = getRecipients(Message.RecipientType.TO);
		return MailUtil.getAddressText(tos);
	}

	public void setRecipient(String s) {
		setRecipients(Message.RecipientType.TO, MailUtil.getAddress(s));
	}

	public String getCarbonCopy() {
		InternetAddress[] ccs = getRecipients(Message.RecipientType.CC);
		return MailUtil.getAddressText(ccs);
	}

	public void setCarbonCopy(String s) {
		setRecipients(Message.RecipientType.CC, MailUtil.getAddress(s));
	}

	public String getBlindCarbonCopy() {
		InternetAddress[] bccs = getRecipients(Message.RecipientType.BCC);
		return MailUtil.getAddressText(bccs);
	}

	public void setBlindCarbonCopy(String s) {
		setRecipients(Message.RecipientType.BCC, MailUtil.getAddress(s));
	}

	/**
	 * Return-Path
	 * @return
	 */
	public InternetAddress[] getReplyTo() {
		String replyTo = getHeaderValue(Header.REPLY_TO);
		return MailUtil.getAddress(replyTo);
	}

	public void setReplyTo(InternetAddress[] addresses) {
		setHeader(Header.RETURN_PATH, MailUtil.getAddressText(addresses));
	}

	/**
	 * Date
	 * @return
	 */
	public Date getSentDate() {
		String value = getHeaderValue(Header.SENTDATE);
		DateFormat df = new SimpleDateFormat(DATE_FORMAT);
		Date date = null;
		try {
			if(value != null) date = df.parse(value);
		} catch(ParseException pe) {}
		return date;
	}

	public void setSentDate(Date date) {
		if(date == null) return;
		DateFormat df = new SimpleDateFormat(DATE_FORMAT);
		setHeader(Header.SENTDATE, df.format(date));
	}

	/**
	 * Content-Transfer-Encoding
	 * @return
	 */
	public String getEncoding() {
		return getContentTransferEncoding();
	}

	public void setEncoding(String encoding) {
		setContentTransferEncoding(encoding);
	}

	/**
	 * Subject
	 * @return
	 */
	public String getSubject() {
		return MailUtil.decodeText(getHeaderValue(Header.SUBJECT));
	}

	public void setSubject(String s) {
		if(s != null) s = s.trim();
		setHeader(Header.SUBJECT, MailUtil.encodeText(s));
	}

	/**
	 * Size
	 */
	public long getSize() {
		return source.length();
	}

	public String getSizeText() {
		return StringUtil.format(new Double(getSize() / 1000.0), "0.0K");
	}

	/**
	 * Priority
	 * @return
	 */
	public Priority getPriority() {
		String priority = getHeaderValue(Header.PRIORITIES);
		return Priority.getPriority(priority);
	}

	public void setPriority(Priority priority) {
		setHeader(Header.IMPORTANCE, priority.toString());
	}

	public String getImportance() {
		Priority priority = getPriority();
		return priority.toString();
	}

	public void setImportance(String s) {
		Priority priority = Priority.getPriority(s);
		setPriority(priority);
	}

	/**
	 * Mime Version
	 * @return
	 */
	public String getMimeVersion() {
		return getHeaderValue(Header.MIME_VERSION);
	}

	public void setMimeVersion(String version) {
		setHeader(Header.MIME_VERSION, version);
	}

	/**
	 * X-Mailer
	 */
	public String getXMailer() {
		return getHeaderValue(Header.X_MAILER);
	}

	public void setXMailer(String mailer) {
		setHeader(Header.X_MAILER, mailer);
	}

	/**
	 * Message-ID
	 * @return
	 */
	public String getMessageId() {
		String id = getHeaderValue(Header.MESSAGE_ID);
		return MailUtil.getMessageId(id);
	}

	public void setMessageId(String id) {
		setHeader(Header.MESSAGE_ID, "<" + MailUtil.getMessageId(id) + ">");
	}

	/**
	 * MailPart
	 * @param part
	 */
	public void addPart(Part part) {
		if (parts.contains(part)) {
			log.warn("Duplicate part in mail message!");
		} else {
			parts.add(part);
		}
	}

	public void removePart(Part part) {
		parts.remove(part);
	}

	public void removePart(int index) {
		parts.remove(index);
	}

	public Collection getParts() {
		return this.parts;
	}

	public void clearParts() {
		parts.clear();
	}

	/**
	 * return defined type parts 
	 * @param type
	 * @return
	 */
	public Collection getParts(Part.Type type) {
		Collection c = new ArrayList();
		Part part;
		if (parts != null) {
			for (Iterator it = parts.iterator(); it.hasNext();) {
				part = (Part) it.next();
				if (type.equals(part.getType()))
					c.add(part);
			}
		}
		return c;
	}

	/**
	 * return one part of the defined type
	 * @param type
	 * @return
	 */
	public Part getPart(Part.Type type) {
		Part result = null;
		Part part;
		if (parts != null) {
			for (Iterator it = parts.iterator(); it.hasNext();) {
				part = (Part) it.next();
				if (part.getType().equals(type)) {
					result = part;
					break;
				}
			}
		}
		return result;
	}

	/**
	 * plain text
	 * @return
	 */
	public String getText() {
		StringBuffer text = new StringBuffer();
		if (parts != null) {
			for (Iterator it = parts.iterator(); it.hasNext();) {
				Part part = (Part) it.next();
				if (part.getType().equals(Part.Type.TEXT))
					text.append(part.getText());
			}
		}
		return text.toString();
	}

	public int getTextSize() {
		int len = 0;
		if (parts != null) {
			for (Iterator it = parts.iterator(); it.hasNext();) {
				Part part = (Part) it.next();
				if (part.getType().equals(Part.Type.TEXT))
					len += part.getSize();
			}
		}
		return len;
	}

	public void setText(String s) throws PartException {
		clearText();
		if(s != null) addText(s);
	}

	public void addText(URL url) throws PartException {
		Reader reader;
		int len;
		char[] buffer = new char[BUFFER_SIZE];
		StringBuffer sb = new StringBuffer();
		File file = new File(url.getFile());
		try {
			reader = new FileReader(file);
			while ((len = reader.read(buffer)) > 0) {
				sb.append(buffer, 0, len);
			}
			reader.close();
			addText(sb.toString());
		} catch (IOException ioe) {
			throw new PartException(ioe);
		}
	}

	public void addText(String s) throws PartException {
		Part part = getPart(Part.Type.TEXT);
		if (part == null) {
			part = new MailPart(Part.Type.TEXT);
			part.setContent(s);
		} else {
			parts.remove(part);
			part.setContent(part.getText() + s);
		}
		addPart(part);
	}

	public void addText(char[] cbuf) throws PartException {
		addText(String.valueOf(cbuf));
	}

	public void addText(char[] cbuf, int len) throws PartException {
		addText(String.valueOf(cbuf, 0, len));
	}

	public void clearText() {
		parts.removeAll(getParts(Part.Type.TEXT));
	}

	/**
	 * html
	 * @return
	 */
	public String getHTML() {
		StringBuffer html = new StringBuffer();
		if (parts != null) {
			for (Iterator it = parts.iterator(); it.hasNext();) {
				Part part = (Part) it.next();
				if (part.getType().equals(Part.Type.HTML))
					html.append(part.getText());
			}
		}
		return html.toString();
	}

	public int getHTMLSize() {
		int len = 0;
		if (parts != null) {
			for (Iterator it = parts.iterator(); it.hasNext();) {
				Part part = (Part) it.next();
				if (part.getType().equals(Part.Type.HTML))
					len += part.getSize();
			}
		}
		return len;
	}

	public void setHTML(String s) throws PartException {
		if (s == null) s = "";
		clearHTML();
		addHTML(s);
	}

	public void addHTML(URL url) throws PartException {
		Reader reader;
		int len;
		char[] buffer = new char[BUFFER_SIZE];
		StringBuffer sb = new StringBuffer();
		File file = new File(url.getFile());
		String base = "";
		try {
			reader = new FileReader(file);
			while ((len = reader.read(buffer)) > 0) {
				sb.append(buffer, 0, len);
			}
			reader.close();
			base = file.getParent();
			if (base == null) base = "";
			String html = sb.toString();
			try {
				html = composeHTML(sb, base);
			} catch (PartException pe) {
				log.error("can NOT compose HTML part!", pe);
			}
			addHTML(html);
		} catch (IOException ioe) {
			throw new PartException(ioe);
		}
	}

	/**
	 * 对邮件的HTML部分,做进一步的解析替换处理 1. 替换background|src|value = file ==>
	 * background|src|value = "cid:xxx" 2. 从文件系统中读取file, 构造part
	 * TODO: 根据base和src做一些细化
	 * 
	 * @param html
	 * @param base
	 */
	private String composeHTML(final CharSequence html, String base) throws PartException {
		String htmlCharset;
		Map inlines = new Hashtable();
		StringBuffer r = new StringBuffer();
		Matcher m1, m2;
		int start = 0, end = html.length();
		String name, value, id;
		File inline;
		MailPart part;
		m1 = HTML_SRC_PATTERN.matcher(html);
		boolean find = m1.find(0);
		while (find) {
			name = m1.group(1);
			value = m1.group(3);
			if ("src".equalsIgnoreCase(name) || "value".equalsIgnoreCase(name)
					|| "background".equalsIgnoreCase(name)) {
				inline = new File(value);
				if (!inline.canRead())
					inline = new File(base + "/" + value);
				if (inline.canRead() && inline.isFile()) {
					// file exists then add to _cids and Inlines
					if (inlines.containsKey(value)) {
						// already in inlines
						id = (String) inlines.get(value);
					} else {
						// a new html related inline
						part = new MailPart(Part.Type.INLINE);
						part.setContent(inline);
						addInline(part);
						id = generateContentId(inlines.size() + 1);
						part.setContentId(id);
						inlines.put(value, id);
					}
					// replace src=... or value=... with cid:...
					r.append(html.subSequence(start, m1.start(3)));
					r.append("cid:").append(id);
					start = m1.end(3);
				}
			}
			if ("content".equalsIgnoreCase(name)) {
				m2 = HTML_CHARSET_PATTERN.matcher(value);
				if (m2.find() && m2.groupCount() >= 1)
					htmlCharset = m2.group(1);
			}
			find = m1.find();
		}
		r.append(html.subSequence(start, end));
		return r.toString();
	}

	private String generateContentId(int index) {
		NumberFormat format = new DecimalFormat("100000@");
		return format.format(index) + baseContentId;
	}

	public void addHTML(String s) throws PartException {
		Part part = getPart(Part.Type.HTML);
		if (part == null) {
			part = new MailPart(Part.Type.HTML);
			part.setContent(s);
		} else {
			parts.remove(part);
			part.setContent(part.getText() + s);
		}
		addPart(part);
	}

	public void addHTML(char[] cbuf) throws PartException {
		addHTML(String.valueOf(cbuf));
	}

	public void addHTML(char[] cbuf, int len) throws PartException {
		addHTML(String.valueOf(cbuf, 0, len));
	}

	public void clearHTML() {
		parts.removeAll(getParts(Part.Type.HTML));
	}

	/**
	 * attachements
	 * @return
	 */
	public Collection getAttachments() {
		return getParts(Part.Type.ATTACHMENT);
	}

	public void addAttachment(Part attachment) {
		if(attachment instanceof AbstractPart) ((AbstractPart) attachment).setType(Part.Type.ATTACHMENT);
		addPart(attachment);
	}

	public void removeAttachment(Part attachment) {
		parts.remove(attachment);
	}

	public void clearAttachments() {
		parts.removeAll(getParts(Part.Type.ATTACHMENT));
	}

	/**
	 * inlines
	 * @return
	 */
	public Collection getInlines() {
		return getParts(Part.Type.INLINE);
	}

	public void addInline(Part inline) {
		if(inline instanceof AbstractPart) ((AbstractPart) inline).setType(Part.Type.INLINE);
		addPart(inline);
	}

	public void removeInline(Part inline) {
		parts.remove(inline);
	}

	public void clearInlines() {
		parts.removeAll(getParts(Part.Type.INLINE));
	}

	/**
	 * source
	 * @param message
	 */
	public void read(Message message) throws PartException {
		try {
			ByteArrayOutputStream s = new ByteArrayOutputStream();
			message.writeTo(s);
			source.append(s.toString());
		} catch (MessagingException me) {
			log.fatal("read message source error!", me);
			throw new PartException(me);
		} catch (IOException ioe) {
			log.fatal("read message source error!", ioe);
			throw new PartException(ioe);
		}
	}

	public String getRawText() {
		return source.toString();
	}

	public byte[] getBytes() {
		return source.toString().getBytes();
	}

	public InputStream getInputStream() {
		return new ByteArrayInputStream(source.toString().getBytes());
	}

	public void setContent(Object content) throws PartException {
		if (content == null) return;
		Type type = getType();
		if (content instanceof String) {
			if (Type.HTML.equals(type)) setHTML((String) content);
			else setText((String) content);
		} else {
			log.warn(content.getClass().getName() + " type content is ignored!");
		}
	}

	public void setRawText(String s) throws IOException {
		if (s == null) s = "";
		clearRawText();
		addRawText(s);
	}

	public void addRawText(char[] cbuf) throws IOException {
		source.append(cbuf);
	}

	public void addRawText(String s) throws IOException {
		source.append(s);
	}

	public void clearRawText() {
		source.delete(0, source.length());
	}

	public String getRawHead() {
		if (rawHead == null || rawHead.length() <= 0) {
			rawHead = new StringBuffer();
			byte[] bytes = source.toString().getBytes();
			for (int i = 0; i < bytes.length; i++) {
				rawHead.append((char) bytes[i]);
				if (bytes[i] == '\n' && isBlankNextLine(bytes, i + 1))
					break;
			}
		}
		return rawHead.toString();
	}

	private boolean isBlankNextLine(byte[] bytes, int start) {
		int i = start;
		boolean result = false;
		while (i < bytes.length) {
			if (bytes[i] == '\r' || bytes[i] == '\t' || bytes[i] == ' ')
				i++;
			else if (bytes[i] == '\n') {
				result = true;
				break;
			} else
				break;
		}
		if (i == bytes.length)
			result = true;
		return result;
	}

	public int getAttachmentCount() {
		Collection c = getParts(Part.Type.ATTACHMENT);
		return c.size();
	}

	public void write(OutputStream os) throws IOException {
		os.write(source.toString().getBytes());
	}

	/**
	 * Define mail headers
	 * 
	 * @author Harry Sun
	 * 
	 */
	private static interface Header {

		public final static String RETURN_PATH = "Return-Path";

		public final static String DELIVERED_TO = "Delivered-To";

		public final static String TO = "To";

		public final static String CC = "CC";

		public final static String BCC = "BCC";

		public final static String FROM = "From";
		
		public final static String SENDER = "Sender";

		public final static String PRIORITY = "Priority";

		public final static String IMPORTANCE = "Importance";
		
		public final static String X_MSMAIL_PRIORITY = "X-MSMail-Priority";
	
		public final static String X_PRIORITY = "X-Priority";
	
		public final static String[] FROMS = {FROM, SENDER, };
		
		public final static String[] RECIPIENTS = {TO, CC, BCC,};

		public final static String[] PRIORITIES = {IMPORTANCE, PRIORITY , X_MSMAIL_PRIORITY, X_PRIORITY,};
		
		public final static String[] REPLY_TO = {RETURN_PATH, FROM, SENDER, };

		public final static String SUBJECT = "Subject";

		public final static String MIME_VERSION = "MIME-Version";

		public final static String SENTDATE = "Date";

		public final static String X_MAILER = "X-Mailer";

		public final static String MESSAGE_ID = "Message-ID";
	}

	/**
	 * Define mail priority
	 * 
	 * @author Harry Sun
	 */
	public static class Priority {
		public static final Priority HIGH = new Priority("high");

		public static final Priority NORMAL = new Priority("normal");

		public static final Priority LOW = new Priority("low");

		public static final Priority NONE = new Priority("");

		private static final int NORMAL_LEVEL = 3;

		private String priority;

		public static final Priority getPriority(int priority) {
			if (priority == NORMAL_LEVEL)
				return NORMAL;
			else if (priority < NORMAL_LEVEL)
				return LOW;
			else
				return HIGH;
		}

		public static final Priority getPriority(String priority) {
			Priority p = NONE;
			if (priority != null) {
				String s = priority.trim();
				try {
					int i = Integer.parseInt(s);
					p = getPriority(i);
				} catch (NumberFormatException nfe) {
					if (HIGH.toString().equalsIgnoreCase(s) || "important".equalsIgnoreCase(s)) p = HIGH;
					else if (LOW.toString().equalsIgnoreCase(s)) p = LOW;
					else p = NORMAL;
				}
			}
			return p;
		}

		public String toString() {
			return priority;
		}

		public boolean equals(Object object) {
			boolean result = false;
			if (Priority.class.isInstance(object)) {
				Priority p = (Priority) object;
				result = p.priority.equalsIgnoreCase(this.priority);
			}
			return result;
		}

		private Priority(String s) {
			priority = s;
		}
	}

}