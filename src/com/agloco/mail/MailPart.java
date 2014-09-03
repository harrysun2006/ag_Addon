package com.agloco.mail;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import javax.mail.Multipart;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.agloco.util.StringUtil;

/**
 * define the mail part
 * @author Harry Sun
 * 2004-10-25
 */
public class MailPart extends AbstractPart {

	private static final long serialVersionUID = 2261732058541783083L;
	private static final Log log = LogFactory.getLog(MailPart.class);

	protected String text;
	protected byte[] bytes;
	protected long size;

	public MailPart() {
		this(Part.Type.UNKNOWN);
	}

	public MailPart(Part.Type type) {
		this.text = null;
		this.bytes = new byte[0];
		setType(type);
	}

	public void setContent(Object content) throws PartException {
		if(content == null) {
		} else if(content instanceof javax.mail.Part) {
			setPartContent((javax.mail.Part)content);
		} else if(content instanceof CharSequence) {
			setCharSequenceContent((CharSequence)content);
		} else if(content instanceof Multipart) {
			setMultipartContent((Multipart)content);
		} else if(content instanceof InputStream) {
			setInputStreamContent((InputStream)content);
		} else if(content instanceof File) {
			setFileContent((File)content);
		} else if(content instanceof URL) {
			setURLContent((URL)content);
		} else {
			log.warn(content.getClass() + " object is ignored!");
		}
	}

	private void setMultipartContent(Multipart part) {
		setContentType(part.getContentType());
	}

	private void setPartContent(javax.mail.Part part) throws PartException {
		try {
			setContentId(getHeader(part, CONTENT_ID));
			setContentType(part.getContentType());
			setContentTransferEncoding(getHeader(part, CONTENT_TRANSFER_ENCODING));
			setDescription(part.getDescription());
			setDisposition(part.getDisposition());
			setFileName(part.getFileName());
			Object content = part.getContent();
			if(content instanceof String) {
				setCharSequenceContent((String)content);
			} else if(content instanceof InputStream) {
				setInputStreamContent((InputStream)content);
			}
		} catch(Exception e) {
			throw new PartException(e);
		}
	}

	private void setCharSequenceContent(CharSequence cs) {
		this.text = cs.toString();
	}

	private void setInputStreamContent(InputStream is) throws PartException {
		int size = 0, off, len;
		try {
			if(is != null) {
				size = is.available();
				bytes = new byte[size];
				off = 0;
				len = 0;
				do {
					len = is.read(bytes, off, size - off);
					if(len > 0) off += len;
				} while(len > 0);
				is.close();
			}
			Type type = getType();
			if(Type.TEXT.equals(type) || Type.HTML.equals(type)) {
				try {
					text = new String(bytes, 0, size, getCharset());
				} catch(UnsupportedEncodingException uee) {
					text = new String(bytes, 0, size);
				}
			}
		} catch(IOException ioe) {
			throw new PartException(ioe);
		}
	}

	private void setFileContent(File file) throws PartException {
		try {
			setFileName(file.getName());
			InputStream is = new FileInputStream(file);
			setInputStreamContent(is);
		} catch(IOException ioe) {
			throw new PartException(ioe);
		}
	}

	private void setURLContent(URL url) throws PartException {
		try {
			setFileName(url.getFile());
			InputStream is = url.openStream();
			setInputStreamContent(is);
			is.close();
		} catch(IOException ioe) {
			throw new PartException(ioe);
		}
	}

	/**
	 * if the part is inline/attachment type, return the byte[] of it
	 * @return
	 */
	public byte[] getBytes() {
		return this.bytes;
	}

	/**
	 * if the part is inline/attachment type, return the inputstream of it
	 * @return
	 */
	public InputStream getInputStream() {	
		InputStream is = null;
		if(bytes != null ) {
			is = new ByteArrayInputStream(bytes);
		}
		return is;
	}

	/**
	 * if the part is text/plain type, return the plain text of it
	 * @return
	 */
	public String getText() {
		return (text == null && getType().equals(Type.TEXT)) ? "" : text;
	}

	/**
	 * if the part is text/html type, return the html source of it
	 * @return
	 */
	public String getHTML() {
		return (text == null && getType().equals(Type.HTML)) ? "" : text;
	}

	/**
	 * return the size of the part, if it is an inline/attachment, then return the byte count of it
	 * if it is text/html, return the size of the text/html.
	 * @return
	 */
	public long getSize() {
		long size = 0;
		Type type = getType();
		if((type.equals(Type.TEXT) || type.equals(Type.HTML)) && text != null) {
			size = text.length();
		} else if(type.equals(Type.ATTACHMENT) || type.equals(Type.INLINE)) {
			size = bytes.length;
		}
		return (size <= 0) ? this.size : size;
	}

	public String getSizeText() {
		return StringUtil.format(new Double(getSize()/1000.0), "0.0K");
	}

	public void setSize(long value) {
		this.size = value;
	}

	private String getHeader(javax.mail.Part part, String name) {
		String[] values = null;
		try {
			values = part.getHeader(name);
		} catch(Exception e) {}
		return (values != null && values.length > 0) ? values[0] : null;
	}

}
