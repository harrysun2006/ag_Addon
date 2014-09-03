package com.amaxgs.addon;

import org.apache.commons.beanutils.BeanUtils;

import com.agloco.model.AGMember;
import com.agloco.model.AGMemberTemp;
import com.agloco.service.util.MemberServiceUtil;

interface AGMemberResolver {

	public AGMember getAGMember(String s) throws Exception;
	public AGMember getAGMember(Object o) throws Exception;
	public final static AGMemberResolver AG_MEMBER_TEMP_UID_RESOLVER = new AGMemberTempUid();
	public final static AGMemberResolver AG_MEMBER_TEMP_EMAIL_RESOLVER = new AGMemberTempEmail();
	public final static AGMemberResolver AG_MEMBER_EMAIL_RESOLVER = new AGMemberEmail();
	
	public static class AGMemberTempUid implements AGMemberResolver {
		public AGMember getAGMember(String s) throws Exception {
			AGMemberTemp mt = MemberServiceUtil.getAGMemberTempByUserId(s);
			AGMember m = null;
			if (mt != null) {
				m = new AGMember();
				BeanUtils.copyProperties(m, mt);
			}
			return m;
		}

		public AGMember getAGMember(Object o) throws Exception {
			throw new Exception("getAGMember is not supported in class!");
		}
	}

	public static class AGMemberTempEmail implements AGMemberResolver {
		public AGMember getAGMember(String s) throws Exception {
			AGMemberTemp mt = MemberServiceUtil.getAGMemberTempByEmail(s);
			AGMember m = null;
			if (mt != null) {
				m = new AGMember();
				BeanUtils.copyProperties(m, mt);
			}
			return m;
		}

		public AGMember getAGMember(Object o) throws Exception {
			AGMemberTemp mt = (AGMemberTemp) o;
			AGMember m = null;
			if (mt != null) {
				m = new AGMember();
				BeanUtils.copyProperties(m, mt);
			}
			return m;
		}
	}

	public static class AGMemberEmail implements AGMemberResolver {
		public AGMember getAGMember(String s) throws Exception {
			return MemberServiceUtil.getAGMemberByEmail(s);
		}

		public AGMember getAGMember(Object o) throws Exception {
			return (AGMember) o;
		}
	}

}
