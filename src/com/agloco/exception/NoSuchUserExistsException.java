package com.agloco.exception;

import com.liferay.portal.PortalException;

public class NoSuchUserExistsException extends PortalException
{
	public NoSuchUserExistsException() {
		super();
	}

	public NoSuchUserExistsException(String msg) {
		super(msg);
	}

	public NoSuchUserExistsException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public NoSuchUserExistsException(Throwable cause) {
		super(cause);
	}

}
