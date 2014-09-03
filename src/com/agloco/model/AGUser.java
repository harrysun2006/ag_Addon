package com.agloco.model;

import java.io.Serializable;
import java.util.Calendar;

public class AGUser implements Serializable{
	
	private String userId = "";
	private String companyId = "";
	private Calendar createDate = null;
	private Calendar modifiedDate = null;
	private String password = "";
	private Calendar lastLoginDate = null;
	private String lastLoginIP = "";
	
	private String emailAddress = "";
	private String languageId;
	private int active;
	private int agreedToTermsOfUse;
	
	public int getActive() {
		return active;
	}
	public void setActive(int active) {
		this.active = active;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getLastLoginIP() {
		return lastLoginIP;
	}
	public void setLastLoginIP(String lastLoginIP) {
		this.lastLoginIP = lastLoginIP;
	}
	public Calendar getCreateDate()
	{
		return createDate;
	}
	public void setCreateDate(Calendar createDate)
	{
		this.createDate = createDate;
	}
	public Calendar getLastLoginDate()
	{
		return lastLoginDate;
	}
	public void setLastLoginDate(Calendar lastLoginDate)
	{
		this.lastLoginDate = lastLoginDate;
	}
	public Calendar getModifiedDate()
	{
		return modifiedDate;
	}
	public void setModifiedDate(Calendar modifiedDate)
	{
		this.modifiedDate = modifiedDate;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getAgreedToTermsOfUse() {
		return agreedToTermsOfUse;
	}
	public void setAgreedToTermsOfUse(int agreedToTermsOfUse) {
		this.agreedToTermsOfUse = agreedToTermsOfUse;
	}
	public String getLanguageId() {
		return languageId;
	}
	public void setLanguageId(String languageId) {
		this.languageId = languageId;
	}

}
