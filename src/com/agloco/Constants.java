package com.agloco;

import com.liferay.util.Http;

public interface Constants {

	public static final String ADMIN_USER_ID = "omniadmin.users";
	public static final String ADMIN_ROLE_ID = "1001";
	public static final StringBuffer AGLOCO_AESKEY = new StringBuffer("agloco");
	public static final String COMMON_AESKEY = "agloco";
	public static final String DATABASE_CHARSET = "UTF-8";

	public static final String TRUE = "true";
	public static final String FALSE = "false";
	
	public static final String DEFAULT_MAIL = "agloco@algoco.com";
	public static final String MEMBERCODE = "memberCode";
	public static final String EMAILADDRESS = "emailAddress";
	public static final String LOGIN = "login";
	public static final String PASSWORD = "password";
	public static final String REMEMBERME = "rememberMe";
	
	public static final String FORGETINFO = "FORGETINFOTIPS";
	public static final String DEFAULTPLID = "PUB.1.1";
	public static final String NULLSTRING = "";
	public static final String CANOMNIADMINLOGIN="canOmniadminLogin";
	public static final String CLEARCACHEADMIN="clearcacheadmin";
	public static final String REMOTECLEARCACHE="remoteclearcache";
	public static final String REMOTECLEARCACHESUCCESS="remoteclearcachesuccess";

	public static final String ARTICLEID_AGS_FIRST_SIGNIN_EMAIL = "AGS_FIRST_SIGNIN_EMAIL";
	public static final String ARTICLEID_AGS_FORGOT_MEMBERCODE_EMAIL = "AGS_FORGOT_MEMBERCODE_EMAIL";
	public static final String ARTICLEID_AGS_FORGOT_PASSWORD_EMAIL = "AGS_FORGOT_PASSWORD_EMAIL";
	public static final String ARTICLEID_AGS_SIGNUP_EMAIL = "AGS_SIGNUP_EMAIL";
	public static final String ARTICLEID_AGS_SIGNUP_EMAIL_TEMP1 = "AGS_SIGNUP_EMAIL_TEMP1";
	public static final String ARTICLEID_AGS_SIGNUP_EMAIL_TIMING = "AGS_SIGNUP_EMAIL_TIMING";
	public static final String ARTICLEID_AGS_CHANGE_PASSWORD_EMAIL = "AGS_CHANGE_PASSWORD_EMAIL";
	public static final String ARTICLEID_AGS_CHANGE_EMAIL_ADDRESS_EMAIL = "AGS_CHANGE_EMAIL_ADDRESS_EMAIL";
	public static final String ARTICLEID_AG_TERMS_OF_USE = "AG_TERMS_OF_USE";
	public static final String ARTICLEID_AG_AGLOCO_UPDATE = "AG_AGLOCO_UPDATE";
	public static final String ARTICLEID_AG_SIGNUP_LEFT_ARTICE = "AG_SIGNUP_LEFT_ARTICE";
	public static final String ARTICLEID_AG_BLOGLINK = "AG_BLOGLINK";
	
	public static final String PASSWORD_LENGTH = "passwords.length";
	public static final String MAIL_SIGNUP_MAXSEND = "mail.signup.maxsend";
	public static final String MAIL_SIGNUP_INTERVAL = "mail.signup.interval";
	public static final String MAIL_SIGNUP_SENDMAXTRY = "mail.signup.sendmaxtry";
	public static final String REFERRAL_TREE_MAXLEVEL = "referral.tree.maxlevel";
	public static final String REFERRAL_TREE_CALCLEVEL = "referral.tree.calclevel";
	public static final String EMAIL_ADDRESS_EXCLUDE = "email.address.exclude";
	public static final String EMAIL_ADDRESS_EXCLUDE_WEEK_TASK = "week.task.email.address.exclude";
	public static final String AUDIT_EMAIL_BCC_NAME = "audit.email.bcc.name";
	public static final String AUDIT_EMAIL_BCC_ADDRESS = "audit.email.bcc.address";
	public static final String REMOTE_SOCKET_PORT = "remote.socket.port";
	public static final String CONFIG_EMAIL_SERVICE_DOMAIN_URL_NEED = "config.email.service.domain.url.need";
	public static final String CONFIG_EMAIL_SERVICE_DOMAIN_URL_PRE  = "email.service.domain.url.";
	
	public static int DEF_MAIL_SIGNUP_MAXSEND = 2; //Max send mail count, previous is 3. 2006-12-18
	public static int DEF_MAIL_SIGNUP_INTERVAL = 3600;	// 1 hour
	public static int DEF_MAIL_SIGNUP_AGO = 3600;	// 1 hour
	public static int DEF_MAIL_SIGNUP_INTERVAL_WEEKLY = 7;	// 7 days
	public static int DEF_MAIL_SIGNUP_SENDMAXTRY = 3;
	public static int DEF_REFERRAL_TREE_MAXLEVEL = 8;
	public static int DEF_REFERRAL_TREE_CALCLEVEL = 5;
	
	public static final String DEFAULT_VIEW_USER_ID_SUFFIX = "default";
	public static final String DEFAULT_COMPANY_ID = "default.company.id";//modified by terry at 2007-05-09,old value:default
	
	public static final String COOKIE_SIGN_UP			 = "cookie.sign.up.referral.code";
	public static final String SESSION_REFERRAL_CODE 	 = "session.referral.code";
	public static final String COOKIE_SIGN_UP_USER_ID 	 = "cookie.sign.up.user.id";
	public static final String SESSION_SIGN_UP_USER_ID   = "session.sign.up.user.id";
	
	public static final String IS_MEMBER = "M";
	public static final String IS_TEMP_MEMBER = "T";
	public static final String NO_SUCH_EMALI = "N";

	public static final String HTTP = Http.HTTP_WITH_SLASH;

	public final static String MULTI_SQL_SEPARATOR = "_SQL_";

	public static final String SPRING_BEAN_HOOK = "spring.bean.hook";
	public static final String SPRING_BEAN_ID_PREFIX = "spring.bean.id.";
	public static final String SPRING_BEAN_CLASS_PREFIX = "spring.bean.class.";
	
	public static final String SPRING_BEAN_PROPERTYVALUES_HOOK="spring.bean.PropertyValues.hook";
	public static final String SPRING_BEAN_PROPERTYVALUES_ID="spring.bean.PropertyValues.id.";
	public static final String SPRING_BEAN_PROPERTYVALUES_PROPERTY="spring.bean.PropertyValues.property.";
	public static final String SPRING_BEAN_PROPERTYVALUES_VALUE ="spring.bean.PropertyValues.value.";

	public final static int BATCH_NUMBER = 20; // batch operate db number 
	public final static int PAGE_SIZE = 20; // default page size
	
	public final static String OPERATE_SINGIN = "SIGNIN";
	public final static String OPERATE_SINGUP = "SIGNUP";
	public final static String OPERATE_REFERRAL = "REFERRAL";
	
	public final static String THREAD_SESSION = "thread.session";
	
	public final static String AJAX_SERVICE_EVENTS_PRE = "ajax.service.events.pre";
	public final static String AJAX_SERVICE_EVENTS_POST = "ajax.service.events.post";
	
	public static final String SYMBOL_AT  = "@";
	
	public static final String ADMIN_USER_MEMBER_CODE_PRE 					 = "AGLOADM";
	public static final String ADMIN_USER_MEMBER_CODE_COUNTER 			     = "com.agloco.admin.member.code";
	public static final String ADMIN_USER_USER_ID_COUNTER	 				 = "com.agloco.admin.user.id";
	public static final String ADMIN_USER_MEMBER_CODE_MIN_NUMBER_VALUE 		 = "1";
	public static final int    ADMIN_USER_MEMBER_CODE_MINIMUM_INCREMENT_SIZE = 1;
	public static final String ADMIN_USER_USER_ID_MIN_NUMBER_VALUE 		 	 = "5";
	public static final int    ADMIN_USER_USER_ID_MINIMUM_INCREMENT_SIZE 	 = 1;
	public static final int    ADMIN_USER_USER_ID_MAX_NUMBER_VALUE 		 	 = 100;
	
	public static final int MAX_MEMBER_AGE = 130;
	
	//add at 2007-04-24
	public final static String AGLOCO_DATASOURCE   = "liferayDataSource";
	public final static String VIEWBAR_DATASOURCE  = "viewbarDataSource";
	public final static String TB_AG_M_TREE        = "AG_M_Tree";
	public final static String TB_VB_TIME_SUBTOTAL = "VB_Time_Subtotal";
	public final static int MAX_SURF_TIME          = 5*60*60;
	
	//add at 2007-05-08
	public final static String VIEWBAR_HIBERNATE_CONFIGS         = "viewbar.hibernate.configs";
	public final static String AGLOCO_GLOBAL_EARNING_RATE        = "agloco.global.earning.rate";
	public final static double DEFAULT_GLOBAL_EARNING_RATE       = 0.25;
	
	
		
	
}

