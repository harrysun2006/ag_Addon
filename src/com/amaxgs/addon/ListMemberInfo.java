package com.amaxgs.addon;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.agloco.model.AGMember;
import com.agloco.model.AGMemberAudit;
import com.amaxgs.addon.service.AddonService;
import com.liferay.portal.spring.util.SpringUtil;

class ListMemberInfo {

	private static final Log log = LogFactory.getLog(ListMemberInfo.class);
	
	private static final AddonService ads = (AddonService) SpringUtil.getContext().getBean(AddonService.class.getName());

	private static final String[] memberHeader = {"memberId","memberCode","userId",
												  "createDate","modifiedDate",
												  "firstName","middleName","lastName",
												  "emailAddress","password",
												  "city","state","postCode",
												  "country"};
	
	private static final String[] auditHeader = {"memberId","memberCode","userId",
												  "createDate","modifiedDate",
												  "firstName","middleName","lastName",
												  "emailAddress","password","address1","address2",
												  "city","state","postCode",
												  "country"};

	private static final String[] logHeader = {"createDate","userId","emailAddress",
											   "memberCode","referralCode","operate",
											   "ip","serverIp","sessionId",
											   "thread","priority","category",
											   "message","exception",
											   "userAgent","description"};
	

	
	public static void memberInfoToExcel(String conditions,String fileName) throws IOException{
		StringBuffer sb = new StringBuffer();
		sb.append("Member Information").append("\n");
		for (int i = 0; i < memberHeader.length - 1; i++) {
			sb.append(memberHeader[i]).append(",");
		}
		sb.append(memberHeader[memberHeader.length - 1]);
		sb.append("\n");
		List tempList = ads.getMemberInfo(conditions);
		DateFormat datef = new SimpleDateFormat("yyyy-MM-dd hh:mm:SS");
		for(int i =0;i<tempList.size();i++){
			AGMember temp=(AGMember)tempList.get(i);
			sb.append(temp.getMemberId()).append(",");
			sb.append(temp.getMemberCode()).append(",");
			sb.append(temp.getUserId()).append(",");
			if (temp.getCreateDate()!= null){
				sb.append(datef.format(temp.getCreateDate().getTime())).append(",");
			}else{
				sb.append(",");
			}
			if (temp.getModifiedDate()!= null ){
			sb.append(datef.format(temp.getModifiedDate().getTime())).append(",");
			}else {
				sb.append(",");
			}
			sb.append(temp.getFirstName()).append(",");
			sb.append(temp.getMiddleName()).append(",");
			sb.append(temp.getLastName()).append(",");
			sb.append(temp.getEmailAddress()).append(",");
			sb.append(temp.getPassword()).append(",");
			sb.append(temp.getCity()).append(",");
			sb.append(temp.getState()).append(",");
			sb.append(temp.getPostCode()).append(",");
			sb.append(temp.getCountry()).append(",");
			sb.append("\n");
		}
		sb.append("\n\n");
//		sb.append(ads.getMemberInfo(conditions)).append("\n\n");
		
		sb.append("Member Audit Information").append("\n");
		for (int i = 0; i < auditHeader.length - 1; i++) {
			sb.append(auditHeader[i]).append(",");
		}
		sb.append(auditHeader[auditHeader.length - 1]);
		sb.append("\n");
		
		
		tempList = ads.getMemberAuditInfo(conditions);
		for(int i =0;i<tempList.size();i++){
			AGMemberAudit temp=(AGMemberAudit)tempList.get(i);
			sb.append(temp.getMemberId()).append(",");
			sb.append(temp.getMemberCode()).append(",");
			sb.append(temp.getUserId()).append(",");
			if (temp.getCreateDate()!= null){
				sb.append(datef.format(temp.getCreateDate().getTime())).append(",");
			}else{
				sb.append(",");
			}
			if (temp.getModifiedDate()!= null ){
			sb.append(datef.format(temp.getModifiedDate().getTime())).append(",");
			}else {
				sb.append(",");
			}
			sb.append(temp.getFirstName()).append(",");
			sb.append(temp.getMiddleName()).append(",");
			sb.append(temp.getLastName()).append(",");
			sb.append(temp.getEmailAddress()).append(",");
			sb.append(temp.getPassword()).append(",");
			sb.append(temp.getAddress1()).append(",");
			sb.append(temp.getAddress2()).append(",");
			sb.append(temp.getCity()).append(",");
			sb.append(temp.getState()).append(",");
			sb.append(temp.getPostCode()).append(",");
			sb.append(temp.getCountry()).append(",");
			sb.append("\n");
		}
		sb.append("\n\n");
//		sb.append(ads.getMemberAuditInfo(conditions)).append("\n\n");
		
		sb.append("Log Information").append("\n");
		for (int i = 0; i < logHeader.length - 1; i++) {
			sb.append(logHeader[i]).append(",");
		}
		sb.append(logHeader[logHeader.length - 1]);
		sb.append("\n");
		List tables = ads.listLogTableNames();
		sb.append(ads.getLogInfo(tables, conditions));

		FileWriter fw = null;
		PrintWriter pw = null;
		try {
			fw = new FileWriter(getPath() + fileName + ".csv",true);
			pw = new PrintWriter(fw);
			pw.print(sb.toString());
			
		} catch (IOException e) {
			if(log.isErrorEnabled()){
				log.error("write file error", e);
			}
		}
		finally{
			if(pw != null){
				pw.close();
			}
			if(fw != null){
				fw.close();
			}
		}
		
	}
	
	private static String getPath() {
		return ListMemberInfo.class.getClassLoader().getResource(".").getPath();
		
	}
	
//	public static void main(String[] args) throws Exception{
//	ListLogTableNames();
//	getMemberInfo();
//	getMemberAuditInfo();
//	getLogInfo();
//}
//
//private static void ListLogTableNames(){
//	List<String> tables = ads.listLogTableNames();
//	if (tables != null && tables.size() > 0) {
//		for (Iterator iter = tables.iterator(); iter.hasNext();) {
//			String tbName = (String) iter.next();
//			System.out.println(tbName);
//			
//		}
//	}
//}
//
//private static void getMemberInfo(){
//	StringBuffer sb = ads.getMemberInfo(" m.memberId < 4");
//	System.out.println(sb);
//}
//private static void getMemberAuditInfo(){
//	StringBuffer sb = ads.getMemberAuditInfo(" m.memberId < 4 ");
//	System.out.println(sb);
//}
//
//private static void getLogInfo(){
//	List<String> tables = ads.listLogTableNames();
//	if (tables != null && tables.size() > 0) {
//		for (Iterator iter = tables.iterator(); iter.hasNext();) {
//			String tbName = (String) iter.next();
//			System.out.println(tbName);
//			StringBuffer sb = ads.getLogInfo(tbName," m.memberId < 4 ");
//			System.out.println(sb);
//		}
//	}
//	
//}
	
}
