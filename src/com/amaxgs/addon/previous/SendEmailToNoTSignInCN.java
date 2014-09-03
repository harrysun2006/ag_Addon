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

import com.agloco.Constants;
import com.agloco.model.AGMemberTemp;
import com.agloco.service.util.MailServiceUtil;
import com.amaxgs.addon.service.AddonService;
import com.liferay.portal.spring.util.SpringUtil;

 class SendEmailToNoTSignInCN {

	private final static Long PAGE_SIZE = new Long(500);
	private final static String ARTICLE_ID = Constants.ARTICLEID_AGS_SIGNUP_EMAIL;
	private final static Log _log = LogFactory.getLog(SendEmailToNoTSignInCN.class);
	
	protected final static String DEFAULT_SENT_FILE_NAME = "sent_email_cn.txt";
	protected final static String DEFAULT_UNSENT_FILE_NAME = "unsent_email_cn.txt";
	
	public static void sendEmail(String fileName){
		StringBuffer countHql  = new StringBuffer(200);
		countHql.append("select count(m) from AGMemberTemp m,com.liferay.portal.model.User u")
				.append(" where m.userId = u.userId")
				.append(" and m.userId not like 'agloco.com_'")
				.append(" and u.languageId like 'zh___'")
				.append(" and ( m.emailAddress like :email1")
				   .append(" or m.emailAddress like :email2")
				   .append(" or m.emailAddress like :email3)")
				.append(" and m.memberCode not like 'AGLO____'");

		StringBuffer listHql  = new StringBuffer(200);
		listHql.append("select m from AGMemberTemp m,com.liferay.portal.model.User u")
				.append(" where m.userId = u.userId")
				.append(" and m.userId not like 'agloco.com_'")
				.append(" and u.languageId like 'zh___'")
				.append(" and ( m.emailAddress like :email1")
				   .append(" or m.emailAddress like :email2")
				   .append(" or m.emailAddress like :email3)")
				.append(" and m.memberCode not like 'AGLO____'")
				.append(" order by m.memberId");
		
		String[] parameter = {"email1","email2","email3"};
		String[] values = {"%sina.com","%sina.com.cn","%yahoo.com.cn"};
		
		AddonService service = null;
		int success = 0;
		int failure = 0;
		int skip = 0;
		int sleepTime = 1000*60;
		List list = null;
		Set sentSet = null; //sent email set
		
		FileWriter fw_s = null;
		BufferedWriter bw_s = null;
		
		FileWriter fw_u = null;
		BufferedWriter bw_u = null;
		
		DateFormat df = new SimpleDateFormat("z(Z) yyyy-MM-dd HH:mm:ss");
		
		try {
			
			//read email list from file
			sentSet = readFile(getPath() + fileName);
			_log.info("read email from sent email file, total number: " + sentSet.size());

			//open success email file
			fw_s = new FileWriter(getPath()+fileName,true);
			bw_s = new BufferedWriter(fw_s);
			bw_s.newLine();
			bw_s.write(df.format(Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTime()));
			bw_s.newLine();
			
			//open unsend email file
			fw_u = new FileWriter(getPath() + DEFAULT_UNSENT_FILE_NAME,true);
			bw_u = new BufferedWriter(fw_u);
			bw_u.newLine();
			bw_u.write(df.format(Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTime()));
			bw_u.newLine();
			
			ApplicationContext ctx = SpringUtil.getContext(); 
			service = (AddonService)ctx.getBean(AddonService.class.getName());
			Long total = service.getTotalNumber(countHql.toString(), parameter, values, true);
			int totalNumber = (total == null) ? 0 : total.intValue();
			if(totalNumber == 0){
				_log.info("there is no member using language cn");
				//return;
			}
			
			_log.info("there are " + totalNumber + " members using language cn"); 
			int pageNumber = totalNumber/PAGE_SIZE.intValue() + 1;
			_log.info("there are " + pageNumber + " pages");
			for(int i = 1; i <= pageNumber; i++){
				list = service.listObject(listHql.toString(), parameter, values, new Long(i), PAGE_SIZE, true, AGMemberTemp.class);
				if(list == null || list.size() < 1){
					_log.info("there are no members using language cn and the job will exit");
					break;
				}
				
				_log.info("there are " + list.size() + " members need to be sent email"); 
				
				for(Iterator it = list.iterator(); it.hasNext();) {
					AGMemberTemp m = (AGMemberTemp)it.next();
					if(m != null) {
						try {
							if(!sentSet.contains(m.getEmailAddress().trim().toLowerCase())){
								MailServiceUtil.sendSignupMail(m);
								success++; 
								_log.info("send " + ARTICLE_ID + " email success, memberId = " + m.getMemberId());
								//write email in file
								bw_s.write(m.getEmailAddress());
								bw_s.newLine();
								bw_s.flush();
								if(success % PAGE_SIZE.intValue() == 0) {
									_log.info("by now has sent " + ARTICLE_ID + " emails(success/failure/skip): " + success + "/" + failure + "/" + skip);
									_log.info("begin sleeping ..." + sleepTime + " millisecond");
									Thread.sleep(sleepTime);
								}
							}
							else{
								skip++;
								//write email in filter file
								bw_u.write(m.getEmailAddress());
								bw_u.newLine();
								bw_u.flush();
							}
						}
						catch(Exception e){
							failure++;
							_log.error("send " + ARTICLE_ID + " email failure, memberId = " + m.getMemberId(), e);
						}
					}
				}
				list.clear();
			}
			_log.info("total sent " + ARTICLE_ID + " emails(success/failure/skip): " + success + "/" + failure + "/" + skip);
		}
		catch (IOException e) {
			_log.error("operating file error!", e);
			System.out.println(e);
		}
		catch(Exception e){
			_log.error("query the members using language cn error!", e);
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
		}
		return set;
	}

	private static String getPath() {
		String path = SendEmailToNoTSignInCN.class.getClassLoader().getResource(".").getPath();
		return path;
	}

}
