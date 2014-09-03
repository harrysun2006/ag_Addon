package com.agloco.model;

import java.io.Serializable;
import java.util.Calendar;

public class MessageModel implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3057323748353899643L;

	private Calendar createDate;

	private String thread;

	private String priority;

	private String category;
	
	private String message;
	
	private String exception;

	public String getException()
	{
		return exception;
	}

	public void setException(String exception)
	{
		this.exception = exception;
	}

	public MessageModel()
	{
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public String getCategory()
	{
		return category;
	}

	public void setCategory(String category)
	{
		this.category = category;
	}

	

	public Calendar getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Calendar createDate) {
		this.createDate = createDate;
	}

	public String getPriority()
	{
		return priority;
	}

	public void setPriority(String priority)
	{
		this.priority = priority;
	}

	public String getThread()
	{
		return thread;
	}

	public void setThread(String thread)
	{
		this.thread = thread;
	}

}
