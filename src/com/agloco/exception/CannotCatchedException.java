package com.agloco.exception;

import com.liferay.portal.PortalException;

public class CannotCatchedException extends PortalException
{
	public CannotCatchedException() {
		super();
	}

	public CannotCatchedException(String msg) {
		super(msg);
	}

	public CannotCatchedException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public CannotCatchedException(Throwable cause) {
		super(cause);
	}

}
