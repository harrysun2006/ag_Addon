package com.agloco.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.agloco.util.StringUtil;

public class MessageObject extends MessageModel implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3057323748353899643L;

	private int logId = 0;

	private String userId = "";

	private String emailAddress = "";

	private String memberCode = "";

	private String referralCode = "";

	private String operate = "";

	private String ip = "";

	private String serverIp = "";

	private String description = "";

	private String sessionId = "";

	private String userAgent = "";

	public String getServerIp()
	{
		return serverIp;
	}

	public void setServerIp(String serverIp)
	{
		this.serverIp = serverIp;
	}

	public String getSessionId()
	{
		return sessionId;
	}

	public void setSessionId(String sessionId)
	{
		this.sessionId = sessionId;
	}

	public String getUserAgent()
	{
		return userAgent;
	}

	public void setUserAgent(String userAgent)
	{
		this.userAgent = userAgent;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public MessageObject()
	{
		super();
	}

	public MessageObject(String userId, String emailAddress, String memberCode,
			String referralCode, String operate, String ip)
	{
		super();
		this.userId = userId;
		this.emailAddress = emailAddress;
		this.memberCode = memberCode;
		this.referralCode = referralCode;
		this.operate = operate;
		this.ip = ip;
	}

	public MessageObject(String userId, String emailAddress, String memberCode,
			String referralCode, String operate, String ip, String description)
	{
		super();
		this.userId = userId;
		this.emailAddress = emailAddress;
		this.memberCode = memberCode;
		this.referralCode = referralCode;
		this.operate = operate;
		this.ip = ip;
		this.description = description;
	}

	public String getReferralCode()
	{
		return referralCode;
	}

	public void setReferralCode(String referralCode)
	{
		this.referralCode = referralCode;
	}

	public String getEmailAddress()
	{
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress)
	{
		this.emailAddress = emailAddress;
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

	public String getMemberCode()
	{
		return memberCode;
	}

	public void setMemberCode(String memberCode)
	{
		this.memberCode = memberCode;
	}

	public String getOperate()
	{
		return operate;
	}

	public void setOperate(String operate)
	{
		this.operate = operate;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}
	
	public String getLogDate()
	{
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Calendar createDate = this.getCreateDate();
		String logDate = "";
		if(createDate!=null)
		{
			logDate = df.format(createDate.getTime());
		}
		return logDate;
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
		if (StringUtil.isNotEmpty(this.getEmailAddress()))
		{
			buffer.append("  EmailAddress:");
			buffer.append(this.getEmailAddress());
		}
		if (StringUtil.isNotEmpty(this.getUserId()))
		{
			buffer.append("  UserId:");
			buffer.append(this.getUserId());
		}
		if (StringUtil.isNotEmpty(this.getMemberCode()))
		{
			buffer.append("  MemberCode:");
			buffer.append(this.getMemberCode());
		}
		if (StringUtil.isNotEmpty(this.getReferralCode()))
		{
			buffer.append("  ReferralCode:");
			buffer.append(this.getReferralCode());
		}
		if (StringUtil.isNotEmpty(this.getIp()))
		{
			buffer.append("  IP:");
			buffer.append(this.getIp());
		}
		if (StringUtil.isNotEmpty(this.getServerIp()))
		{
			buffer.append("  ServerIp:");
			buffer.append(this.getServerIp());
		}
		if (StringUtil.isNotEmpty(this.getUserAgent()))
		{
			buffer.append("  UserAgent:");
			buffer.append(this.getUserAgent());
		}
		if (StringUtil.isNotEmpty(this.getDescription()))
		{
			buffer.append("  Description:");
			buffer.append(this.getDescription());
		}
		// TODO Auto-generated method stub
		return buffer.toString();
	}

}
