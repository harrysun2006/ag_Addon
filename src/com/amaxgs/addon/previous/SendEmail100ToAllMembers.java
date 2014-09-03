package com.amaxgs.addon.previous;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import com.agloco.model.AGMember;
import com.agloco.service.util.MailServiceUtil;
import com.amaxgs.addon.service.AddonService;
import com.liferay.portal.spring.util.SpringUtil;

class SendEmail100ToAllMembers {

	private final static Long PAGE_SIZE = new Long(500);
	private final static String ARTICLE_ID = "AG_MAIL_100";
	private final static Log _log = LogFactory.getLog(SendEmail100ToAllMembers.class);
	
	private final static String MEMBER_BLOG_URL   = "http://www.agloco.com/web/guest/blogarticle";
	private final static String OFFICE_BLOG_URL = "http://blog.agloco.com";
	private final static String JOHN_CHOW_URL 	  = "http://www.johnchow.com/category/agloco";
	private final static String MCCALL_SNOTES_URL = "http://mccallsnotes.spaces.live.com/";

	
	protected final static String DEFAULT_SENT_FILE_NAME = "sent_email_100.txt";
	private final static String DEFAULT_UNSENT_FILE_NAME = "unsent_email_100.txt";
	
	public static void sendEmail(String fileName){
	

		StringBuffer countHql  = new StringBuffer(100);
		countHql.append("select count(m) from AGMember m");
				
		
		StringBuffer listHql  = new StringBuffer(100);
		listHql.append("select m from AGMember m")
				.append(" order by m.memberId");
		
		AddonService service = null;
		int success = 0;
		int failure = 0;
		int skip = 0;
		int sleepTime = 1000*60;
		List list = null;
		Set sentSet = null; //sent email set
		
		String[] names = {
				"MEMBER_BLOG_URL",
				"OFFICE_BLOG_URL",
				"JOHN_CHOW_URL",
				"MCCALL_SNOTES_URL"
				};
		
		String[] values = {
				MEMBER_BLOG_URL,
				OFFICE_BLOG_URL,
				JOHN_CHOW_URL,
				MCCALL_SNOTES_URL
				};
		
		FileWriter fw_s = null;
		BufferedWriter bw_s = null;
		
		FileWriter fw_u = null;
		BufferedWriter bw_u = null;
		
		DateFormat df = new SimpleDateFormat("z(Z) yyyy-MM-dd HH:mm:ss");
		
		try{
			
			//read email list from file
			sentSet = readFile(getPath()+fileName);
			_log.info("read email from file success,total number is:"+ sentSet.size());

			
			//open success email file
			fw_s = new FileWriter(getPath()+fileName,true);
			bw_s = new BufferedWriter(fw_s);
			bw_s.newLine();
			bw_s.write(df.format(Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTime()));
			bw_s.newLine();
			
			//open unsend email file
			fw_u = new FileWriter(getPath() + DEFAULT_UNSENT_FILE_NAME, true);
			bw_u = new BufferedWriter(fw_u);
			bw_u.newLine();
			bw_u.write(df.format(Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTime()));
			bw_u.newLine();
			
			ApplicationContext ctx = SpringUtil.getContext(); 
			service = (AddonService)ctx.getBean(AddonService.class.getName());
			Long total = service.getTotalNumber(countHql.toString(), null, null);
			int totalNumber = (total == null) ? 0 : total.intValue();
			if(totalNumber == 0){
				_log.info("there is no member");
				//return;
			}
			
			_log.info("there are " + totalNumber + " members need to be sent email"); 
			System.out.println("there are " + totalNumber + " members need to be sent email");
			int pageNumber = totalNumber/PAGE_SIZE.intValue() + 1;
			_log.info("there are " + pageNumber + " pages");
			System.out.println("there are " + pageNumber + " pages");
			
			for(int i = 1; i <= pageNumber; i++) {
				list = service.listObject(listHql.toString(), null, null, new Long(i), PAGE_SIZE);
				if(list == null || list.size() < 1){
					_log.info("there are no members need to be sent email and the job will exit");
					break;
				}
				
				_log.info("there are " + list.size() + " members need to be sent email this thread"); 
				System.out.println("there are " + list.size() + " members need to be sent email this thread");
				
				for(Iterator it = list.iterator(); it.hasNext();) {
					AGMember m = (AGMember)it.next();
					if(m != null ){
						try{
							if(m.getMemberCode().equals("AGLOADMIN")   || 
							   m.getMemberCode().equals("AGLOSERV")    || 
							   m.getMemberCode().equals("AGLOTEST")    || 
							   m.getMemberCode().equals("AGLOTESTC")   ||
							   m.getMemberCode().startsWith("AGLOADM") ||
							   m.getStatus().equals(AGMember.MEMBER_STATUS_INACTIVE)
							   ){
								continue;
							}
							if(sentSet.contains(m.getEmailAddress().trim().toLowerCase())){
								skip++;
								//write email in filter file
								bw_u.write(m.getEmailAddress());
								bw_u.newLine();
								bw_u.flush();
							}
							else{
								MailServiceUtil.sendMail(m, null, ARTICLE_ID, names, values);
								success++; 
								_log.info("send " + ARTICLE_ID + " email success, memberId = " + m.getMemberId());
								//write email in file
								bw_s.write(m.getEmailAddress());
								bw_s.newLine();
								bw_s.flush();	
								if(success % PAGE_SIZE.intValue() == 0){
									_log.info("by now has sent " + ARTICLE_ID + " emails(success/failure/skip): " + success + "/" + failure + "/" + skip);
									System.out.println("by now has sent "+ ARTICLE_ID + " emails(success/failure/skip):" + success + "/" + failure + "/" +skip);
									_log.info("begin sleeping ..." + sleepTime + " millisecond");
									System.out.println("begin sleeping ..." + sleepTime + " millisecond");
									Thread.sleep(sleepTime);
								}
							}
						}
						catch(Exception e){
							failure++;
							_log.error("send " + ARTICLE_ID + " email error, memberId = " + m.getMemberId(), e);
						}
					}
				}
				list.clear();
			}
			_log.info("total sent " + ARTICLE_ID + " emails(success/failure/skip): " + success + "/" + failure + "/" + skip);
			System.out.println("total sent "+ ARTICLE_ID + " emails(success/failure/skip): " + success + "/" + failure + "/" +skip);
		}
		catch (IOException e) {
			_log.error("operating file error!", e);
			System.out.println(e);
		}
		catch(Exception e){
			_log.error("query the no referral members error!", e);
			System.out.println(e);
		}
		finally{
			try {
				
				//close write success file...
				if(bw_s != null){
					bw_s.close();
				}
				
				if(fw_s != null){
					fw_s.close();
				}

				//close write unsent file
				if(bw_u != null){
					bw_u.close();
				}
				
				if(fw_u != null){
					fw_u.close();
				}
				
			} catch (IOException e) {
				_log.error("write file error!",e);
			}

		}
		
		
	}
	
	private static Set readFile(String fileName) throws IOException {
		Set set = new HashSet();
		try {
			File f = new File(fileName);
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			String email = null;
			while((email = br.readLine()) != null){
				if(StringUtils.isNotBlank(email.trim().toLowerCase())){
					set.add(email.trim().toLowerCase());	
				}
			}
			_log.info("read file: " + fileName + " into list successfully!");
		} catch (FileNotFoundException e) {
			_log.error("file: " + fileName + " not found!");
			throw e;
		} catch (IOException e) {
			_log.error("read file: " + fileName + " failed!");
			throw e;
		}
		return set;
	}
	
	private static String getPath() {
		String path = SendEmail100ToAllMembers.class.getClassLoader().getResource(".").getPath();
		return path;
	}
	
}
