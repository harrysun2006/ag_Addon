package com.agloco.util;

import java.net.InetAddress;

public class MiscUtil {

	private MiscUtil() {
		
	}

	public static String getLocalIp() {
		return getLocalIp(true);
	}

	public static String getLocalIp(boolean exclude) {
		InetAddress[] ia = getLocalIps();
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < ia.length; i++) {
			sb.append(ia[i].getHostAddress()).append(", ");
		}
		if(sb.length() > 0) sb.delete(sb.length() - 2, sb.length());
		return sb.toString();
	}

	/**
	 * judge if the ip is local ip, for JDK 1.4-
	 * @param clientIP
	 * @return
	 */
	public static boolean isLocalClient(String ip) {
		//log.debug("client ip is " + clientIP);
		if (null == ip ) return false;  
		if ("127.0.0.1".equals(ip)) return true;  
		InetAddress[] localIP = getLocalIps();
		for(int i =0 ;i <localIP.length ;i++){
			if (localIP[i].getHostAddress().equals(ip)) return true; 
		}
		return false;
	}

	/**
	 * get all local ips
	 * @return
	 */
	public static InetAddress[] getLocalIps() {
		InetAddress[] ia = null;
		try {
			ia = InetAddress.getAllByName(InetAddress.getLocalHost().getHostName());
		} catch(Exception e) {
		}
		return ia;
	}

}
