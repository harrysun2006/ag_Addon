package com.agloco.mail;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.StringTokenizer;

import com.agloco.util.StringUtil;

/**
 * define a mail part
 * @author Harry Sun
 * 2004-10-25
 */
public interface Part {

	public final static String CONTENT_TYPE = "Content-Type";

	public final static String CONTENT_TYPE_TYPE = "type";
	public final static String CONTENT_TYPE_CHARSET = "charset";
	public final static String CONTENT_TYPE_BOUNDARY = "boundary";
	public final static String CONTENT_TYPE_FILENAME = "name";
	public final static String DISPOSITION_FILENAME = "filename";

	public static final String DEFAULT_CHARSET = "utf-8";
	public static final String DEFAULT_ENCODING = "base64";

	/**
	 * Content-Transfer-Encoding item in a part
	 */
	public final static String CONTENT_TRANSFER_ENCODING = "Content-Transfer-Encoding";
	
	/**
	 * Content-ID item in a part
	 */
	public final static String CONTENT_ID = "Content-ID";
	
	/**
	 * Content-Description item in a part
	 */
	public final static String CONTENT_DESCRIPTION = "Content-Description";
	
	/**
	 * Content-Location item in a part
	 */
	public final static String CONTENT_LOCATION = "Content-Location";
	
	/**
	 * Content-Disposition item in a part
	 */
	public final static String CONTENT_DISPOSITION = "Content-Disposition";

	/**
	 * Content-ID of an inline part
	 * @return
	 */
	public String getContentId();

	public void setContentId(String id);

	/**
	 * Content-Type, the Content-Type header value of a part
	 * @return
	 */
	public String getContentType();

	public void setContentType(String contentType);

	/**
	 * MimeType, the MimeType of a part
	 * @return
	 */
	public String getMimeType();

	/**
	 * Boundary of a part
	 * @return
	 */
	public String getBoundary();

	/**
	 * Charset of a part
	 * @return
	 */
	public String getCharset();

	public void setCharset(String charset);

	/**
	 * TransferEncoding of a part: base64, quoted-printable, ...
	 * @return
	 */
	public String getContentTransferEncoding();

	public void setContentTransferEncoding(String encoding);

	/**
	 * Content-Description, the Content-Description header value of a part
	 * @return
	 */
	public String getDescription();

	public void setDescription(String description);

	/**
	 * Content-Disposition, the Content-Disposition header value of a part
	 * @return
	 */
	public String getDisposition();

	public void setDisposition(String disposition);

	/**
	 * Content-Location, the Content-Location header value of a part
	 * @return
	 */
	public String getLocation();

	public void setLocation(String location);

	/**
	 * file name of a part, name value of Content-Type or filename value of Content-Disposition
	 * @return
	 */
	public String getFileName();

	public void setFileName(String name);

	public Type getType();

	/**
	 * if the part is inline/attachment type, return the byte[] of it
	 * @return
	 */
	public byte[] getBytes();

	/**
	 * if the part is inline/attachment type, return the inputstream of it
	 * @return
	 */
	public InputStream getInputStream();

	/**
	 * if the part is text/plain type, return the plain text of it
	 * @return
	 */
	public String getText();

	/**
	 * if the part is text/html type, return the html source of it
	 * @return
	 */
	public String getHTML();

	/**
	 * return the size of the part, if it is an inline/attachment, then return the byte count of it
	 * if it is text/html, return the size of the text/html.
	 * @return
	 */
	public long getSize();

	/**
	 * set the content of the part
	 * @param content
	 */
	public void setContent(Object content) throws PartException;

	/**
	 * headers of the part
	 * @author Harry Sun
	 */
	public String[] getHeader(String name);

	public void setHeader(String name, String[] values);

	public void addHeader(String name, String value);

	public void removeHeader(String name);

	public Enumeration getHeaderNames();

	public void clearHeaders();

	/**
	 * define the type of the part
	 * @author Harry Sun
	 */
	public static class Type implements Serializable {
		public static final Type MIXED = new Type("multipart/mixed");
		public static final Type RELATED = new Type("multipart/related");
		public static final Type ALTERNATIVE = new Type("multipart/alternative");
		public static final Type TEXT = new Type("text/plain");
		public static final Type HTML = new Type("text/html");
		public static final Type ATTACHMENT = new Type("attachment");
		public static final Type INLINE = new Type("inline");
		public static final Type UNKNOWN = new Type("");

		private static final String TYPE_REGEX = "([^;\\s]*)[;\\s]?";

		private String type;

		public static final Type valueOf(javax.mail.Part part) {
			Type pt = UNKNOWN;
			try {
				String contentType = part.getContentType();
				String contentDisposition = part.getDisposition();
				String fileName = part.getFileName();
				String[] ids = part.getHeader("CONTENT-ID");
				if(contentType == null) contentType = "";
				if(contentDisposition == null) contentDisposition = "";
				if(contentType.indexOf(TEXT.type) == 0) {
					pt = TEXT;
				} else if(contentType.indexOf(HTML.type) == 0) {
					pt = HTML;
				} else {
					if(contentDisposition.indexOf(ATTACHMENT.type) == 0) {
						pt = ATTACHMENT;
					} else if(contentDisposition.indexOf(INLINE.type) == 0) {
						pt = INLINE;
					} else if(ids != null) {
						pt = INLINE;
					} else if(fileName != null) {
						pt = ATTACHMENT;
					} else if(contentType != null){
						pt = valueOf(contentType);
					} else {
						pt = UNKNOWN;
					}
				}
			} catch(Exception e) {
			}
			return pt;
		}

		public static final Type valueOf(javax.mail.Multipart part) {
			return valueOf(part.getContentType());
		}

		public static final Type valueOf(String contentType) {
			Type pt;
			String stype = StringUtil.find(contentType, TYPE_REGEX, 1);
			if(stype == null) pt = UNKNOWN;
			else if(stype.equalsIgnoreCase(TEXT.type)) pt = TEXT;
			else if(stype.equalsIgnoreCase(HTML.type)) pt = HTML;
			else if(stype.equalsIgnoreCase(ATTACHMENT.type)) pt = ATTACHMENT;
			else if(stype.equalsIgnoreCase(INLINE.type)) pt = INLINE;
			else if(stype.equalsIgnoreCase(MIXED.type)) pt = MIXED;
			else if(stype.equalsIgnoreCase(RELATED.type)) pt = RELATED;
			else if(stype.equalsIgnoreCase(ALTERNATIVE.type)) pt = ALTERNATIVE;
			//else if(stype.length() > 0) pt = new Type(stype);
			else pt = UNKNOWN;
			return pt;
		}

		public String toString() {
			return type;
		}

		public boolean equals(Object object) {
			boolean result = false;
			if(object instanceof Type) {
				Type t = (Type)object;
				result = t.type.equalsIgnoreCase(this.type);
			}
			return result;
		}

		public String getPrimaryType() {
			StringTokenizer t = new StringTokenizer(type, ";/");
			return (t.hasMoreTokens()) ? t.nextToken() : null;
		}

		public String getSubType() {
			StringTokenizer t = new StringTokenizer(type, "/;");
			if(t.hasMoreTokens()) t.nextToken();
			return (t.hasMoreTokens()) ? t.nextToken() : null;
		}

		public Type(String s) {
			s = (s == null) ? "" : s.trim().toLowerCase();
			type = s;
		}
	}
}
