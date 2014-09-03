package com.agloco.form;

import java.util.Calendar;

import org.apache.struts.action.ActionForm;

/**
 * 
 * @author Erick_Kong
 *
 */
public class MemberQueryForm extends ActionForm 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String memberCode = "";
	private String memberCodeOp = "";
	private String emailAddress = "";
	private String emailAddressOp = "";
	private String beginDate = "";
	private String endDate = "";
	private String timezone = "";
	private String status = "";
	private String country = "";
	private Long pageSize = null;
	private Long pageNum = null;
	private String memberType = "";
	private String CMD = "";
	private String selectMembers = "";
	
	private int totalResult = 0;
	
	public int getTotalResult()
	{
		return totalResult;
	}
	public void setTotalResult(int totalResult)
	{
		this.totalResult = totalResult;
	}
	public String getCountry()
	{
		return country;
	}
	public void setCountry(String country)
	{
		this.country = country;
	}
	public String getEmailAddress()
	{
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress)
	{
		this.emailAddress = emailAddress.trim();
	}
	public String getEmailAddressOp()
	{
		return emailAddressOp;
	}
	public void setEmailAddressOp(String emailAddressOp)
	{
		this.emailAddressOp = emailAddressOp;
	}
	public String getBeginDate()
	{
		return beginDate;
	}
	public void setBeginDate(String beginDate)
	{
		this.beginDate = beginDate.trim();
	}
	public String getEndDate()
	{
		return endDate;
	}
	public void setEndDate(String endDate)
	{
		this.endDate = endDate.trim();
	}
	public String getMemberCode()
	{
		return memberCode;
	}
	public void setMemberCode(String memberCode)
	{
		this.memberCode = memberCode.trim();
	}
	public String getMemberCodeOp()
	{
		return memberCodeOp;
	}
	public void setMemberCodeOp(String memberCodeOp)
	{
		this.memberCodeOp = memberCodeOp;
	}
	public String getMemberType()
	{
		return memberType;
	}
	public void setMemberType(String memberType)
	{
		this.memberType = memberType;
	}
	public Long getPageSize()
	{
		return pageSize;
	}
	public String getStatus()
	{
		return status;
	}
	public void setStatus(String status)
	{
		this.status = status;
	}
	public String getTimezone()
	{
		return timezone;
	}
	public void setTimezone(String timezone)
	{
		this.timezone = timezone;
	}
	public String getCMD()
	{
		return CMD;
	}
	public void setCMD(String cmd)
	{
		CMD = cmd;
	}
	public Long getPageNum()
	{
		return pageNum;
	}
	public void setPageNum(Long pageNum)
	{
		this.pageNum = pageNum;
	}
	public void setPageSize(Long pageSize)
	{
		this.pageSize = pageSize;
	}
	public String getSelectMembers()
	{
		return selectMembers;
	}
	public void setSelectMembers(String selectMembers)
	{
		this.selectMembers = selectMembers;
	}
	
	

}
