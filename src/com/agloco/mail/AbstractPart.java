package com.agloco.mail;

import java.io.InputStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import com.agloco.util.MailUtil;
import com.agloco.util.StringUtil;

/**
 * @author Harry Sun
 */
public abstract class AbstractPart implements Part, Serializable {

	private static final String BOUNDARY_BASE = "0123456789_ABCDEF#";
	private static final String[] EMPTY_STRINGS = {};
	private static final String CONTENTID_REGEX = "<?([^\"<>\\r\\n]*)>?";
	private String baseBoundary = StringUtil.random(BOUNDARY_BASE, 28);

	protected Hashtable headers;

	public AbstractPart() {
		this.headers = new Hashtable();
	}

	/**
	 * Content-ID of inline part
	 * @return
	 */
	public String getContentId() {
		return getHeaderValue(CONTENT_ID);
	}

	public void setContentId(String value) {
		value = StringUtil.find(value, CONTENTID_REGEX, 1);
		setHeader(CONTENT_ID, value);
	}

	/**
	 * Content-Type, the Content-Type header value of a part
	 * @return
	 */
	public String getContentType() {
		return getHeaderValue(CONTENT_TYPE);
	}

	public void setContentType(String s) {
		if(s == null) return;
		setHeader(CONTENT_TYPE, s);
	}

	/**
	 * MimeType, the MimeType of a part
	 * @return
	 */
	public String getMimeType() {
		ContentType ct = new ContentType(getContentType());
		return ct.getBaseType();
	}

	/**
	 * Boundary of a part
	 * @return
	 */
	public String getBoundary() {
		ContentType ct = new ContentType(getContentType());
		return ct.getParameter(Part.CONTENT_TYPE_BOUNDARY);
	}

	public void setBoundary(String boundary) {
		if(boundary == null) boundary = generateBoundary(0);
		ContentType ct = new ContentType(getContentType());
		ct.setParameter(Part.CONTENT_TYPE_BOUNDARY, boundary);
		setHeader(Part.CONTENT_TYPE, ct.toString());
	}

	private String generateBoundary(int depth) {
		NumberFormat format = new DecimalFormat("--NEXT_0000_");
		return format.format(depth + 1) + baseBoundary;
	}

	/**
	 * Charset of a part
	 * @return
	 */
	public String getCharset() {
		ContentType ct = new ContentType(getContentType());
		String charset = ct.getParameter(Part.CONTENT_TYPE_CHARSET);
		if(charset == null) charset = DEFAULT_CHARSET;
		return charset;
	}

	public void setCharset(String charset) {
		if(charset == null) return;
		ContentType ct = new ContentType(getContentType());
		ct.setParameter(CONTENT_TYPE_CHARSET, charset);
		setHeader(CONTENT_TYPE, ct.toString());
	}

	public Type getType() {
		ContentType ct = new ContentType(getContentType());
		return ct.getType();
	}

	public void setType(Type type) {
		ContentType ct = new ContentType(getContentType());
		ct.setType(type);
		setHeader(CONTENT_TYPE, ct.toString());
	}

	/**
	 * TransferEncoding of a part: base64, quoted-printable, ...
	 * @return
	 */
	public String getContentTransferEncoding() {
		String encoding;
		Type type = getType();
		if(Part.Type.TEXT.equals(type)) encoding = "base64";
		else if(Part.Type.HTML.equals(type)) encoding = "quoted-printable";
		else encoding = DEFAULT_ENCODING;
		return encoding;
	}

	public void setContentTransferEncoding(String encoding) {
		setHeader(CONTENT_TRANSFER_ENCODING, encoding);
	}

	/**
	 * Content-Description, the Content-Description header value of a part
	 * @return
	 */
	public String getDescription() {
		return getHeaderValue(CONTENT_DESCRIPTION);
	}

	public void setDescription(String description) {
		setHeader(CONTENT_DESCRIPTION, description);
	}

	/**
	 * Content-Disposition, the Content-Disposition header value of a part
	 * @return
	 */
	public String getDisposition() {
		return getHeaderValue(CONTENT_DISPOSITION);
	}

	public void setDisposition(String disposition) {
		setHeader(CONTENT_DISPOSITION, disposition);
	}

	/**
	 * Content-Location, the Content-Location header value of a part
	 * @return
	 */
	public String getLocation() {
		return getHeaderValue(CONTENT_LOCATION);
	}

	public void setLocation(String location) {
		setHeader(CONTENT_LOCATION, location);
	}

	// private static final String FILENAME_REGEX = "filename\\s*=\\s*\"*([^\"]*)\"*";

	/**
	 * file name of a part, name value of Content-Type or filename value of Content-Disposition
	 * @return
	 */
	public String getFileName() {
		ContentType ct = new ContentType(getContentType());
		String filename = ct.getParameter(CONTENT_TYPE_FILENAME);
		if(filename == null) {
			ContentDisposition disposition = new ContentDisposition(getDisposition());
			filename = disposition.getParameter(Part.DISPOSITION_FILENAME);
		}
		if(filename == null) filename = getDescription();
		if(filename == null) filename = getLocation();
		if(filename != null) filename = MailUtil.decodeText(filename);
		return filename;
	}

	public void setFileName(String fileName) {
		// if(fileName != null && fileName.length() < fileName.getBytes().length) fileName = MailHelper.encodeText(fileName);
		if(fileName == null) return;
		ContentType ct = new ContentType(getContentType());
		ct.setParameter(CONTENT_TYPE_FILENAME, fileName);
		ContentDisposition disposition = new ContentDisposition(getDisposition());
		disposition.setParameter(DISPOSITION_FILENAME, fileName);
		setHeader(CONTENT_DISPOSITION, disposition.toString());
		setHeader(CONTENT_TYPE, ct.toString());
	}

	/**
	 * headers of the part
	 * @return
	 */
	public String[] getHeader(String name) {
		Collection values = (Collection) headers.get(name);
		if(values == null) return EMPTY_STRINGS;
		else {
			String[] r = new String[values.size()];
			int i = 0;
			for(Iterator it = values.iterator(); it.hasNext(); ) {
				r[i++] = (String) it.next();
			}
			return r;
		}
	}

	protected String getHeaderValue(String name) {
		String[] values = getHeader(name);
		return (values.length > 0) ? values[0] : null;
	}

	protected String getHeaderValue(String[] names) {
		String value = null;
		if(names == null) names = EMPTY_STRINGS;
		for(int i = 0; i < names.length; i++) {
			value = getHeaderValue(names[i]);
			if(value != null) break;
		}
		return value;
	}

	public void setHeader(String name, String[] values) {
		Collection c = (Collection) headers.get(name);
		if(c == null) {
			c = new ArrayList();
			headers.put(name, c);
		}
		c.clear();
		for(int i = 0; i < values.length; i++) {
			if(values[i] == null) values[i] = "";
			c.add(values[i]);
		}
	}

	public void setHeader(String name, String value) {
		setHeader(name, new String[]{value});
	}

	public void addHeader(String name, String value) {
		Collection c = (Collection) headers.get(name);
		if(c == null) {
			c = new ArrayList();
			headers.put(name, c);
		}
		if(value == null) value = "";
		c.add(value);
//		String[] values = getHeader(name);
//		String[] newValues = new String[values.length + 1];
//		System.arraycopy(values, 0, newValues, 0, values.length);
//		newValues[values.length] = value;
//		headers.put(name, newValues);
	}

	public void removeHeader(String name) {
		headers.remove(name);
	}

	public Enumeration getHeaderNames() {
		return (headers == null) ? null : headers.keys();
	}

	public void clearHeaders() {
		headers.clear();
	}

	/**
	 * if the part is inline/attachment type, return the byte[] of it
	 * @return
	 */
	public abstract byte[] getBytes();

	/**
	 * if the part is inline/attachment type, return the inputstream of it
	 * @return
	 */
	public abstract InputStream getInputStream();

	/**
	 * if the part is text/plain type, return the plain text of it
	 * @return
	 */
	public abstract String getText();

	/**
	 * if the part is text/html type, return the html source of it
	 * @return
	 */
	public abstract String getHTML();

	/**
	 * return the size of the part, if it is an inline/attachment, then return the byte count of it
	 * if it is text/html, return the size of the text/html.
	 * @return
	 */
	public abstract long getSize();

	/**
	 * set the content of the part
	 * @param content
	 */
	public abstract void setContent(Object content) throws PartException;
}
