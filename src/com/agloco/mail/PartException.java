package com.agloco.mail;

import org.apache.commons.lang.exception.NestableRuntimeException;

public class PartException  extends NestableRuntimeException {

	public PartException() {
		super();
	}

	public PartException(String msg) {
		super(msg);
	}

	public PartException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public PartException(Throwable cause) {
		super(cause);
	}
}