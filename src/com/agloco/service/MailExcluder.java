package com.agloco.service;

import com.agloco.model.AGMember;
import com.agloco.model.AGMemberTemp;
import com.liferay.portal.model.Company;

public interface MailExcluder {

	public boolean exclude(Company company, AGMember member);

	public boolean exclude(Company company, AGMemberTemp tempMember);

}
