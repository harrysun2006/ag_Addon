package com.amaxgs.addon.previous;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.agloco.Constants;
import com.agloco.service.util.MemberServiceUtil;
import com.amaxgs.addon.Agloco;

class DisableMember {

	private static Log _log = LogFactory.getLog(DisableMember.class);

	private static List readFile(String fileName) {
		File f = new File(fileName);
		List list = null;
		try {
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			String email = null;
			while((email = br.readLine()) != null){
				if(list == null){
					list = new ArrayList();
				}
				if(StringUtils.isNotBlank(email.trim().toLowerCase())){
					list.add(email.trim().toLowerCase());	
				}
			}
			_log.info("read file: " + fileName + " into list successfully!");
		} catch (FileNotFoundException e) {
			_log.error("file: " + fileName + " not found!");
			System.out.println("file: " + fileName + " not found!");
		} catch (IOException e) {
			_log.error("read file: " + fileName + " failed!");
			System.out.println("read file: " + fileName + " failed!");
		}
		return list;
	}

	public static void disableMembers(String fileName) {
		List list = readFile(getPath() + fileName);
		if(list == null || list.size() < 1){
			_log.info("file: " + fileName + " has no content!");
			System.out.println("file: " + fileName + " has no content!");
			return;
		}
//		dispalyEmail(list);
		int s = 0;
		int f = 0;
		try {
			System.out.print("the file has " + list.size() + " emails need to be disabled, are you sure disable them?[yes/(no)]: ");
			String choice = Agloco.in.readLine();
			if("yes".equalsIgnoreCase(choice)) {
				for(Iterator iter = list.iterator(); iter.hasNext();) {
					String emailAddress = (String)iter.next();
					String result = MemberServiceUtil.disableMemberByEmail(emailAddress);
					if(result.equals(Constants.IS_MEMBER)){
						s++;
						_log.info("disable member: "+ emailAddress + " successfully!");
					} else if(result.equals(Constants.IS_TEMP_MEMBER)) {
						s++;
						_log.info("disable temporary member: "+ emailAddress + " successfully!");
					} else if(result.equals(Constants.NO_SUCH_EMALI)) {
						f++;
						_log.info("can't find email: "+ emailAddress + " in both AG_Member and AG_M_Temp table!");
					} else {
						f++;
						_log.info("unknown error!"); 
					}
				}
				_log.info("disable members in list(success/failed/total): " + s + "/" + f + "/" + list.size());
				System.out.println("disable members in list(success/failed/total): " + s + "/" + f + "/" + list.size());
			} else {
				_log.info("abort disable members!");
				System.out.println("abort disable members!");
			}
		} catch (IOException e) {
			_log.error("read command from console exception");
			System.out.println("read command from console exception");
			e.printStackTrace();
		}
	}

	private static String getPath() {
		String path = DisableMember.class.getClassLoader().getResource(".").getPath();
		return path;
	}

	private static void dispalyEmail(List list) {
		System.out.println("the file has " + list.size() + " email need to be disabled");
		System.out.println("email list:");
		for(Iterator iter = list.iterator(); iter.hasNext();){
			System.out.println((String)iter.next());
		}
	}

}
