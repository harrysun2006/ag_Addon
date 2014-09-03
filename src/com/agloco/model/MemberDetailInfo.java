package com.agloco.model;

import java.util.Calendar;
import java.util.Date;

public class MemberDetailInfo {
	private String userId;
	private Long memberId;
	private String memberCode;
	private Calendar createDate;
	private Calendar modifiedDate;
	private String password;
	private String firstName;
	private String middleName;
	private String lastName;
	private String emailAddress;
	private String birthDate;
	private String address1;
	private String address2;
	private String city;
	private String state;
	private String postCode;
	private String country;
	private String status;
	private Date lastLoginDate = null;
	private String lastLoginIP = "";
	private String loginIP;
	private Date loginDate;
	private Integer mailCount;
	private boolean agreedToTermsOfUse;
	private Calendar lastMailTime;
	private String mailResult;
	private boolean tempMember;

	public String getLoginIP() {
		return loginIP;
	}

	public void setLoginIP(String loginIP) {
		this.loginIP = loginIP;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	

	public Calendar getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Calendar createDate) {
		this.createDate = createDate;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public Date getLastLoginDate() {
		return lastLoginDate;
	}

	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	public String getLastLoginIP() {
		return lastLoginIP;
	}

	public void setLastLoginIP(String lastLoginIP) {
		this.lastLoginIP = lastLoginIP;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}

	public String getMemberCode() {
		return memberCode;
	}

	public void setMemberCode(String memberCode) {
		this.memberCode = memberCode;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public Calendar getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Calendar modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isTempMember() {
		return tempMember;
	}

	public void setTempMember(boolean tempMember) {
		this.tempMember = tempMember;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Calendar getLastMailTime() {
		return lastMailTime;
	}

	public void setLastMailTime(Calendar lastMailTime) {
		this.lastMailTime = lastMailTime;
	}

	public Integer getMailCount() {
		return mailCount;
	}

	public void setMailCount(Integer mailCount) {
		this.mailCount = mailCount;
	}

	public String getMailResult() {
		return mailResult;
	}

	public void setMailResult(String mailResult) {
		this.mailResult = mailResult;
	}

	public boolean isAgreedToTermsOfUse() {
		return agreedToTermsOfUse;
	}

	public void setAgreedToTermsOfUse(boolean agreedToTermsOfUse) {
		this.agreedToTermsOfUse = agreedToTermsOfUse;
	}



}
