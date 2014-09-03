package com.agloco.service.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.agloco.Constants;
import com.agloco.model.AGMember;
import com.agloco.model.AGMemberTemp;
import com.agloco.service.MailExcluder;
import com.agloco.util.ConfigUtil;
import com.liferay.portal.model.Company;

public class GeneralMailExcluder implements MailExcluder {

	public boolean exclude(Company company, AGMember member) {
		return excludeByEmailAddress(company, member.getEmailAddress());
	}

	public boolean exclude(Company company, AGMemberTemp tempMember) {
		return excludeByEmailAddress(company, tempMember.getEmailAddress());
	}

	private boolean excludeByEmailAddress(Company company, String emailAddress) {
		boolean r = false;
		String emailAddressExclude = ConfigUtil.getConfig(Constants.EMAIL_ADDRESS_EXCLUDE, company.getCompanyId());
		if(emailAddressExclude == null) emailAddressExclude = "";
		Pattern p = Pattern.compile(emailAddressExclude, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(emailAddress);
		r = r || m.matches();
		return r;
	}
}
