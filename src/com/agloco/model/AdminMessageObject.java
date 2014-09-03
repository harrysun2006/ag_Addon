package com.agloco.model;

import java.io.Serializable;

import com.agloco.util.StringUtil;

public class AdminMessageObject extends MessageModel implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3057323748353899643L;

	private int logId = 0;

	private String userId = "";

	private String ip = "";

	private String className = "";

	private String method = "";
	
	private String entityClass = "";
	
	private String primaryKey = "";
	
	private String operate = "";

	private String description = "";

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getIp()
	{
		return ip;
	}

	public void setIp(String ip)
	{
		this.ip = ip;
	}

	public int getLogId()
	{
		return logId;
	}

	public void setLogId(int logId)
	{
		this.logId = logId;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public String toString()
	{
		StringBuffer buffer = new StringBuffer();
		
		if (StringUtil.isNotEmpty(this.getOperate()))
		{
			buffer.append("  Operate:");
			buffer.append(this.getOperate());
			if (this.getOperate().equals("ERROR"))
			{
				if (StringUtil.isNotEmpty(this.getException()))
				{
					buffer.append("  Exception:");
					buffer.append(this.getException());
				}
			}
		}
		if (StringUtil.isNotEmpty(this.getUserId()))
		{
			buffer.append("  UserId:");
			buffer.append(this.getUserId());
		}
		if (StringUtil.isNotEmpty(this.getIp()))
		{
			buffer.append("  IP:");
			buffer.append(this.getIp());
		}
		if (StringUtil.isNotEmpty(this.getClassName()))
		{
			buffer.append("  ClassName:");
			buffer.append(this.getClassName());
		}
		if (StringUtil.isNotEmpty(this.getMethod()))
		{
			buffer.append("  Method:");
			buffer.append(this.getMethod());
		}
		if (StringUtil.isNotEmpty(this.getEntityClass()))
		{
			buffer.append("  EntityClass:");
			buffer.append(this.getEntityClass());
		}
		if (StringUtil.isNotEmpty(this.getPrimaryKey()))
		{
			buffer.append("  PrimaryKey:");
			buffer.append(this.getPrimaryKey());
		}
		if (StringUtil.isNotEmpty(this.getDescription()))
		{
			buffer.append("  Description:");
			buffer.append(this.getDescription());
		}

		return buffer.toString();
	}

	public String getClassName()
	{
		return className;
	}

	public void setClassName(String className)
	{
		this.className = className;
	}

	public String getMethod()
	{
		return method;
	}

	public void setMethod(String method)
	{
		this.method = method;
	}

	public String getOperate() {
		return operate;
	}

	public void setOperate(String operate) {
		this.operate = operate;
	}

	public String getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(String entityClass) {
		this.entityClass = entityClass;
	}

	public String getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}

}
