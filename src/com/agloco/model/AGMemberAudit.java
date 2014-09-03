package com.agloco.model;

import java.io.Serializable;
import java.sql.Blob;
import java.util.Calendar;

import org.hibernate.lob.BlobImpl;

import com.agloco.Constants;
import com.agloco.util.CryptUtil;

public class AGMemberAudit implements Serializable{

	private static final long serialVersionUID = -7848727337441140801L;

	public static final String OPERATION_EDIT = "E";

	private String key = Constants.AGLOCO_AESKEY.toString();
	private String charSet = Constants.DATABASE_CHARSET;

	private Long auditId;
	private String operator;
	private String operation;
	private Calendar operateDate;

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
	private String reserved1;
	private String reserved2;
	private String reserved3;

	private Blob encMemberCode;
	private Blob encPassword;
	private Blob encFirstName;
	private Blob encMiddleName;
	private Blob encLastName;
	private Blob encEmailAddress;
	private Blob encBirthDate;
	private Blob encAddress1;
	private Blob encAddress2;
	private Blob encCity;
	private Blob encState;
	private Blob encPostCode;
	private Blob encCountry;

	public Long getAuditId() {
		return auditId;
	}

	public void setAuditId(Long auditId) {
		this.auditId = auditId;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public Calendar getOperateDate() {
		return operateDate;
	}

	public void setOperateDate(Calendar operateDate) {
		this.operateDate = operateDate;
	}


	public String getMemberCode() {
		return memberCode;
	}

	public void setMemberCode(String memberCode) {
		this.memberCode = memberCode;
	}

	public Calendar getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Calendar createDate) {
		this.createDate = createDate;
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

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
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

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getReserved1() {
		return reserved1;
	}

	public void setReserved1(String reserved1) {
		this.reserved1 = reserved1;
	}

	public String getReserved2() {
		return reserved2;
	}

	public void setReserved2(String reserved2) {
		this.reserved2 = reserved2;
	}

	public String getReserved3() {
		return reserved3;
	}

	public void setReserved3(String reserved3) {
		this.reserved3 = reserved3;
	}

	/**
	 * use AESEncry to encrypt secure fields
	 * call this method before youre session.save() and session.update()
	 */
	public void encrypt() {
		setEncMemberCode(CryptUtil.AESEncrypt(memberCode, key, charSet));
		setEncPassword(CryptUtil.AESEncrypt(password, key, charSet));
		setEncFirstName(CryptUtil.AESEncrypt(firstName, key, charSet));
		setEncMiddleName(CryptUtil.AESEncrypt(middleName, key, charSet));
		setEncLastName(CryptUtil.AESEncrypt(lastName, key, charSet));
		setEncEmailAddress(CryptUtil.AESEncrypt(emailAddress, key, charSet));
		setEncBirthDate(CryptUtil.AESEncrypt(birthDate, key, charSet));
		setEncAddress1(CryptUtil.AESEncrypt(address1, key, charSet));
		setEncAddress2(CryptUtil.AESEncrypt(address2, key, charSet));
		setEncCity(CryptUtil.AESEncrypt(city, key, charSet));
		setEncState(CryptUtil.AESEncrypt(state, key, charSet));
		setEncPostCode(CryptUtil.AESEncrypt(postCode, key, charSet));
		setEncCountry(CryptUtil.AESEncrypt(country, key, charSet));
	}

	/** the below properties are used for AES Encrypt while inserting and updating **/

	protected Blob getEncMemberCode() {
		return encMemberCode;
	}

	protected void setEncMemberCode(Blob b) {
		memberCode = CryptUtil.AESDecrypt(b, key, charSet);
		encMemberCode = b;
	}

	protected void setEncMemberCode(byte[] b) {
		encMemberCode = (b == null) ? null : new BlobImpl(b);
	}

	protected Blob getEncPassword() {
		return encPassword;
	}

	protected void setEncPassword(Blob b) {
		password = CryptUtil.AESDecrypt(b, key, charSet);
		encPassword = b;
	}

	protected void setEncPassword(byte[] b) {
		encPassword = (b == null) ? null : new BlobImpl(b);
	}

	protected Blob getEncFirstName() {
		return encFirstName;
	}

	protected void setEncFirstName(Blob b) {
		firstName = CryptUtil.AESDecrypt(b, key, charSet);
		encFirstName = b;
	}

	protected void setEncFirstName(byte[] b) {
		encFirstName = (b == null) ? null : new BlobImpl(b);
	}

	protected Blob getEncMiddleName() {
		return encMiddleName;
	}

	protected void setEncMiddleName(Blob b) {
		middleName = CryptUtil.AESDecrypt(b, key, charSet);
		encMiddleName = b;
	}

	protected void setEncMiddleName(byte[] b) {
		encMiddleName = (b == null) ? null : new BlobImpl(b);
	}

	protected Blob getEncLastName() {
		return encLastName;
	}

	protected void setEncLastName(Blob b) {
		lastName = CryptUtil.AESDecrypt(b, key, charSet);
		encLastName = b;
	}

	protected void setEncLastName(byte[] b) {
		encLastName = (b == null) ? null : new BlobImpl(b);
	}

	protected Blob getEncEmailAddress() {
		return encEmailAddress;
	}

	protected void setEncEmailAddress(Blob b) {
		emailAddress = CryptUtil.AESDecrypt(b, key, charSet);
		encEmailAddress = b;
	}

	protected void setEncEmailAddress(byte[] b) {
		encEmailAddress = (b == null) ? null : new BlobImpl(b);
	}

	protected Blob getEncBirthDate() {
		return encBirthDate;
	}

	protected void setEncBirthDate(Blob b) {
		birthDate = CryptUtil.AESDecrypt(b, key, charSet);
		encBirthDate = b;
	}

	protected void setEncBirthDate(byte[] b) {
		encBirthDate = (b == null) ? null : new BlobImpl(b);
	}

	protected Blob getEncAddress1() {
		return encAddress1;
	}

	protected void setEncAddress1(Blob b) {
		address1 = CryptUtil.AESDecrypt(b, key, charSet);
		encAddress1 = b;
	}

	protected void setEncAddress1(byte[] b) {
		encAddress1 = (b == null) ? null : new BlobImpl(b);
	}

	protected Blob getEncAddress2() {
		return encAddress2;
	}

	protected void setEncAddress2(Blob b) {
		address2 = CryptUtil.AESDecrypt(b, key, charSet);
		encAddress2 = b;
	}

	protected void setEncAddress2(byte[] b) {
		encAddress2 = (b == null) ? null : new BlobImpl(b);
	}

	protected Blob getEncCity() {
		return encCity;
	}

	protected void setEncCity(Blob b) {
		city = CryptUtil.AESDecrypt(b, key, charSet);
		encCity = b;
	}

	protected void setEncCity(byte[] b) {
		encCity = (b == null) ? null : new BlobImpl(b);
	}

	protected Blob getEncState() {
		return encState;
	}

	protected void setEncState(Blob b) {
		state = CryptUtil.AESDecrypt(b, key, charSet);
		encState = b;
	}

	protected void setEncState(byte[] b) {
		encState = (b == null) ? null : new BlobImpl(b);
	}

	protected Blob getEncPostCode() {
		return encPostCode;
	}

	protected void setEncPostCode(Blob b) {
		postCode = CryptUtil.AESDecrypt(b, key, charSet);
		encPostCode = b;
	}

	protected void setEncPostCode(byte[] b) {
		encPostCode = (b == null) ? null : new BlobImpl(b);
	}

	protected Blob getEncCountry() {
		return encCountry;
	}

	protected void setEncCountry(Blob b) {
		country = CryptUtil.AESDecrypt(b, key, charSet);
		encCountry = b;
	}

	protected void setEncCountry(byte[] b) {
		encCountry = (b == null) ? null : new BlobImpl(b);
	}

}