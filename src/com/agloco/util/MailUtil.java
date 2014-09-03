package com.agloco.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeUtility;

import com.sun.mail.util.BASE64DecoderStream;
import com.sun.mail.util.QPDecoderStream;
import com.sun.mail.util.UUDecoderStream;

/**
 * some useful methods used in mail
 * @author Harry Sun
 */
public class MailUtil {

	/**
	 * return the string of an Address value
	 * @param addresses
	 * @return
	 */
	public static String getAddressText(InternetAddress[] addresses) {
		return getAddressText(addresses, true);
	}

	public static String getAddressText(InternetAddress[] addresses, boolean encode) {
		StringBuffer r = new StringBuffer();
		if (addresses != null && addresses.length > 0) {
			String addressStr;
			for (int i = 0; i < addresses.length; i++) {
				if(addresses[i] == null) continue;
				if(encode) addressStr = addresses[i].toString();
				else addressStr = addresses[i].toUnicodeString();
				r.append(addressStr).append(",\r\n  ");
			}
			if (r.length() > 0) {
				r.delete(r.length() - 5, r.length());
			}
		}
		return r.toString();
	}

	/**
	 * return the string of an Address value
	 * @param address
	 * @return
	 */
	public static String getAddressText(InternetAddress address) {
		return getAddressText(new InternetAddress[]{address});
	}

	/**
	 * parse email addresses string, return Address[]
	 * @param address
	 * @return
	 */
	public static InternetAddress[] getAddress(String text) {
		InternetAddress[] addresses = new InternetAddress[0];
		try {
			if(text != null) addresses = InternetAddress.parse(text);
		} catch(AddressException ae) {
			String[] as = text.split(",|;");
			addresses = new InternetAddress[as.length];
			for(int i = 0; i < as.length; i++) {
				try {
					addresses[i] = new InternetAddress(as[i]);
				} catch(AddressException addex) {
				}
			}
		}
		for(int i = 0; i < addresses.length; i++) addresses[i].getPersonal();
		return addresses;
	}

	/**
	 * return the address part of email address
	 * @param address
	 * @return
	 */
	public static String getAddress(InternetAddress address) {
		return (address == null) ? "" : address.getAddress();
	}

	/**
	 * return the personal part of email address
	 * @param address
	 * @return
	 */
	public static String getPersonal(InternetAddress address) {
		return (address == null) ? "" : address.getPersonal();
	}

	/**
	 * return the display text of email address
	 * @param address
	 * @return
	 */
	public static String getText(InternetAddress address) {
		String text = getPersonal(address);
		if(text == null || text.trim().length() == 0) text = getAddress(address);
		return text;
	}

	private final static Pattern MESSAGE_ID_PATTERN = Pattern.compile("<?([^>]*)>?");

	/**
	 * return the "F27300834BB2014288D94674B95E78B608B81B9E@s1.intranet.com"
	 * in Message-ID "<F27300834BB2014288D94674B95E78B608B81B9E@s1.intranet.com>"
	 * @param id
	 * @return
	 */
	public static String getMessageId(String id) {
		if(id == null) return null;
		Matcher m = MESSAGE_ID_PATTERN.matcher(id);
		if(m.find() && m.groupCount() >= 1) return m.group(1);
		else return id;
	}

	/**
	 * parse base64 encoded block(inline/attachment) and return an inputstream
	 * @param source
	 * @param encoding
	 * @return
	 * @throws MessagingException
	 */
	public static final InputStream decode(InputStream source, String encoding) throws MessagingException {
		return MimeUtility.decode(source, encoding);
	}

	/**
	 * decode text
	 * @param source
	 * @param encoding
	 * @param charset
	 * @return
	 * @throws MessagingException
	 */
	public static final String decodeText(final String source, String encoding, String charset) throws MessagingException {
		InputStream is, ris;
		String result = "";
		is = new ByteArrayInputStream(source.getBytes());
		if("base64".equalsIgnoreCase(encoding))
			ris = new BASE64DecoderStream(is);
		else if("quoted-printable".equalsIgnoreCase(encoding))
			ris = new QPDecoderStream(is);
		else if("uuencode".equalsIgnoreCase(encoding) || "x-uuencode".equalsIgnoreCase(encoding) || "x-uue".equalsIgnoreCase(encoding))
			ris = new UUDecoderStream(is);
		else if("binary".equalsIgnoreCase(encoding) || "7bit".equalsIgnoreCase(encoding) || "8bit".equalsIgnoreCase(encoding))
			ris = is;
		else
			throw(new MessagingException("Unknown encoding: " + encoding));
		try {
			if(ris != null) {
				int size = ris.available();
				byte[] b = new byte[size];
				int off = 0, len = 0;
				do {
					len = ris.read(b, off, size - off);
					if(len > 0) off += len;
				} while(len > 0);
				ris.close();
				if(charset != null) result = new String(b, 0, off, charset);
				else result = new String(b, 0, off);
			}
		} catch(IOException ioe) {
			throw(new MessagingException("Decode error", ioe));
		}
		return result;
	}

	private final static Pattern HEADER_VALUE_PATTERN = Pattern.compile("=\\?([-\\w]+)\\?([B|Q])\\?([+=/\\w]+)\\?=");

	/**
	 * parse the item value in headers
	 * @param value
	 * =?utf-8?B?TWF5Lkh16IOh6JSa?= <may.hu@gcecn.com> or
	 * =?utf-8?B?5p2O5pWPIDxsaW1pbkB0aG9ybi1iaXJkLmNvbT4s?=
	 * 		=?utf-8?B?5a2j5Lit5Y6fIDxqenlAdGhvcm4tYmlyZC5jb20+?=
	 * @return
	 */
	public static final String decodeText(final String source) {
		if(source == null) return null;
		String result = source;
		try {
			Matcher m = HEADER_VALUE_PATTERN.matcher(source);
			StringBuffer sb = new StringBuffer();
			String charset = "";
			String encoding = "";
			boolean found = false;
			while(m.find()) {
				charset = m.group(1);
				encoding = m.group(2);
				sb.append(m.group(3));
				found = true;
			}
			StringBuffer r = new StringBuffer();
			if(found) {
				r
					.append("=?").append(charset)
					.append("?").append(encoding)
					.append("?").append(sb).append("?=");
				result = MimeUtility.decodeText(r.toString());
			}
		} catch(UnsupportedEncodingException uee) {}
		return result;
	}

	public static final String encodeText(String source) {
		String result;
		try {
			result = MimeUtility.encodeText(source);
		} catch(UnsupportedEncodingException uee) {
			result = source;
		}
		return result;
	}

	public static final String encodeText(String source, String charset, String encoding) {
		String result;
		try {
			result = MimeUtility.encodeText(source, charset, encoding);
		} catch(UnsupportedEncodingException uee) {
			result = source;
		}
		return result;
	}
}
