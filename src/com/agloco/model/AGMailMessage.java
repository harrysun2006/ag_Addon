package com.agloco.model;

import java.io.InputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.SQLException;

import org.apache.commons.lang.SerializationUtils;
import org.hibernate.Hibernate;


public class AGMailMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8203480772103942499L;
	
	private Long id;
	private Blob message;
	private Serializable serialiableMsg;
	
	
	public Serializable getSerialiableMsg() throws SQLException {
		InputStream is = getMessage().getBinaryStream();
		serialiableMsg = (is == null) ? null : (Serializable) SerializationUtils.deserialize(is); 
		return serialiableMsg;
	}
	
	public void setSerialiableMsg(Serializable serialiableMsg) {
		this.serialiableMsg = serialiableMsg;
		byte[] b = SerializationUtils.serialize(serialiableMsg);
		setMessage(b == null ? null : Hibernate.createBlob(b));
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	protected Blob getMessage() {
		return message;
	}
	protected void setMessage(Blob message) {
		this.message = message;
	}
	
}
