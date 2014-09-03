package com.agloco.model;

import java.io.Serializable;
import java.util.Calendar;

/**
 * 
 * @author Erick Kong
 *
 */
public class AGQuery implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long queryId;
	private String queryName;
	private String query;
	private String queryParamsHTML;
	private String queryTitle;
	private int queryType;
	private String preScript;
	private String postScript;
	private String resultDisplayScript;
	private String queryDescription;
	private Calendar createDate;

	
	private String queryTypeString = "DEFAULT";
	protected static String DAILY = "DAILY";
	protected static String WEEKLY = "WEEKLY";
	protected static String MONTHLY = "MONTHLY";

	public String getQueryParamsHTML()
	{
		return queryParamsHTML;
	}
	public void setQueryParamsHTML(String queryParamsHTML)
	{
		this.queryParamsHTML = queryParamsHTML;
	}
	public boolean isMultiQuery()
	{
		if(this.query.indexOf("_SQL_")>0)
			return true;
		else
			return false;
	}
	public Calendar getCreateDate()
	{
		return createDate;
	}
	public void setCreateDate(Calendar createDate)
	{
		this.createDate = createDate;
	}
	public String getQuery()
	{
		return query;
	}
	public void setQuery(String query)
	{
		this.query = query;
	}
	public String getQueryDescription()
	{
		return queryDescription;
	}
	public void setQueryDescription(String queryDescription)
	{
		this.queryDescription = queryDescription;
	}
	public Long getQueryId()
	{
		return queryId;
	}
	public void setQueryId(Long queryId)
	{
		this.queryId = queryId;
	}
	public String getQueryName()
	{
		return queryName;
	}
	public void setQueryName(String queryName)
	{
		this.queryName = queryName;
	}
	public int getQueryType()
	{
		return queryType;
	}
	public void setQueryType(int queryType)
	{
		this.queryType = queryType;
		switch (queryType)
		{
		case 1:
			queryTypeString = DAILY;
			break;
		case 7:
			queryTypeString = WEEKLY;
			break;
		case 30:
			queryTypeString = MONTHLY;
			break;
		}

	}
	public String getQueryTitle()
	{
		return queryTitle;
	}
	public void setQueryTitle(String queryTitle)
	{
		this.queryTitle = queryTitle;
	}
	public String getPreScript()
	{
		return preScript;
	}
	public void setPreScript(String preScript)
	{
		this.preScript = preScript;
	}
	public String getPostScript()
	{
		return postScript;
	}
	public void setPostScript(String postScript)
	{
		this.postScript = postScript;
	}
	public String getResultDisplayScript()
	{
		return resultDisplayScript;
	}
	public void setResultDisplayScript(String resultDisplayScript)
	{
		this.resultDisplayScript = resultDisplayScript;
	}
	public String getQueryTypeString()
	{
		return queryTypeString;
	}

}
