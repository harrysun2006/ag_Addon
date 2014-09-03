package com.amaxgs.addon;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.agloco.Constants;
import com.agloco.util.CryptUtil;
import com.liferay.portal.spring.hibernate.CacheRegistry;
import com.liferay.portal.util.ClusterPool;
import com.liferay.portal.util.PropsUtil;
import com.liferay.util.Base64;

public class Agloco {

	private static Log _log = LogFactory.getLog(Agloco.class);
	public static BufferedReader in;
	protected static Properties ADDON_PROPERTIES = new Properties();

	private static void help() {
		System.out.println("?: print this help.");
		System.out.println("1: send a test email -->");
		System.out.println("2: print current time.");
		System.out.println("3: show decrypted referral code.");
		System.out.println("4: send email to members -->");
		System.out.println("5: send email to temporary members -->");
		System.out.println("6: send email to who in list -->");
		System.out.println("7: dump member information -->");
		System.out.println("8: upgrade viewbar.");
		System.out.println("9: check rolling tables -->");
		System.out.println("a: clear content cached across the cluster.");
		System.out.println("q: exit this program!");
	}

	private static void loadProps() {
		try {
			InputStream is = ClassLoader.getSystemResourceAsStream("addon.properties");
			ADDON_PROPERTIES.load(is);
			is.close();
		} catch(Exception e) {}
	}

	private static void redirectIn(String[] args) {
		InputStream is = System.in;
		try {
			if(args.length > 0) {
				is = ClassLoader.getSystemResourceAsStream(args[0]);
			} 
		} catch(Exception e) {
			is = System.in;
		}
		in = new BufferedReader(new InputStreamReader(is));
	}

	public static void main(String[] args) throws Exception {
		try {
			redirectIn(args);
			loadProps();
			ContextHelper.addResource("jdbc.properties");
			ContextHelper.addResource("jdbc-vb.properties");
			ContextHelper.addResource("mail.properties");
			while(true) {
				help();
				loadProps();
				System.out.print("choice: ");
				String choice = in.readLine();
				if("?".equals(choice)) help();
				else if("1".equals(choice)) sendTestEmail();
				else if("2".equals(choice)) printCurrentTime();
				else if("3".equals(choice)) decodeReferralCode();
				else if("4".equals(choice)) sendEmailToMembers();
				else if("5".equals(choice)) sendEmailToTempMembers();
				else if("6".equals(choice)) sendEmailToList();
				else if("7".equals(choice)) dumpMemberInfo();
				else if("8".equals(choice)) upgradeViewbar();
				else if("9".equals(choice)) checkRollingTables();
				else if("a".equals(choice)) clearClusterCache();
				else if("q".equals(choice)) {
					if(args.length > 0) {
						_log.info("exit this program");
						Thread.sleep(1000 * 60 * 10);
					}
					System.exit(0);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			_log.error(e, e);
		} 
	}

	private static void sendTestEmail() throws Exception {
		SendTestEmail.sendEmail();
	}

	private static void printCurrentTime() {
		Calendar nowUTC = Calendar.getInstance();
		Calendar nowPST = Calendar.getInstance(TimeZone.getTimeZone("PST"));
		DateFormat df = new SimpleDateFormat("z(Z) yyyy-MM-dd HH:mm:ss");
		System.out.println("user timezone: " + System.getProperty("user.timezone") + ", now: " + df.format(nowUTC.getTime()));
		df.setTimeZone(TimeZone.getTimeZone("UTC"));
		System.out.println("user timezone: " + System.getProperty("user.timezone") + ", now: " + df.format(nowUTC.getTime()));
		df.setTimeZone(TimeZone.getTimeZone("PST"));
		System.out.println("user timezone: " + System.getProperty("user.timezone") + ", now: " + df.format(nowPST.getTime()));
		TimeZone.setDefault(TimeZone.getTimeZone("PST"));
		df = new SimpleDateFormat("z(Z) yyyy-MM-dd HH:mm:ss");
		System.out.println("user timezone: " + System.getProperty("user.timezone") + ", now: " + df.format(nowUTC.getTime()));
		_log.trace("This is a TRACE!");
		_log.debug("This is a DEBUG!");
		_log.info("This is an INFO!");
		_log.warn("This is a WARN!");
		_log.error("This is an EROOR!");
		_log.fatal("This is a FATAL!");
	}

	private static void decodeReferralCode() throws Exception {
		System.out.print("please enter an encrypted referral code: ");
		String enReferralCode = in.readLine();
		byte[] b = Base64.decode(enReferralCode);	
		String s = CryptUtil.AESDecrypt(b, Constants.COMMON_AESKEY, Constants.DATABASE_CHARSET); 
		System.out.println("the decrypted referral code is: " + s);
	}

	private static void sendEmailToMembers() throws Exception {
		SendEmailToMembers.send();
	}

	private static void sendEmailToTempMembers() throws Exception {
		SendEmailToTempMembers.send();
	}

	private static void sendEmailToList() throws Exception {
		SendEmailToList.send();
	}

	private static void dumpMemberInfo() throws Exception {
		System.out.print("list member information?[yes/(no)]: ");
		String answer = in.readLine();
		if("yes".equalsIgnoreCase(answer)){
			
			System.out.print("please input conditions(like: m.memberId < 2 or m.emailAddress='agloco@agloco.com'): ");
			String conditions = in.readLine();
			if(StringUtils.isBlank(conditions)) {
				System.out.println("conditions can not be empty");
				return;
			}
			
			System.out.print("please input file name: ");
			String fileName = in.readLine();
			if(StringUtils.isBlank(fileName)) {
				System.out.println("file name can not be empty");
				return;
			}
			ListMemberInfo.memberInfoToExcel(conditions, fileName);
		}
	}

	private static void upgradeViewbar() throws Exception {
		ViewbarUpdater.update();
	}

	private static void checkRollingTables() throws Exception {
		
	}

	private static void clearClusterCache() throws Exception {
		CacheRegistry.clear();
//	ClusterPool.clear();
		Calendar defaultCalendar = Calendar.getInstance();
		defaultCalendar.add(Calendar.MINUTE, 10);
		ClusterPool.getCache().flushAll(defaultCalendar.getTime());
	}

}
