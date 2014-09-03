package com.amaxgs.addon;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

/**
 * @author author E-mail:zhaon12@gmail.com
 * @version 1.0
 * @createDate createDate:2007-8-6 上午08:56:27
 * @content
 */
public class ViewbarTimeTrackChecker {
	protected static Properties ADDON_PROPERTIES = new Properties();
	public static BufferedReader in;
	private  static String START_DATE ;
	private  static String END_DATE = ADDON_PROPERTIES
			.getProperty("end.date");

	static {
		try {
			InputStream is = ClassLoader
					.getSystemResourceAsStream("addon_date.properties");
			ADDON_PROPERTIES.load(is);
			is.close();
		} catch (Exception e) {
		}
	}
	private static void loadProps() {
		try {
			InputStream is = ClassLoader
					.getSystemResourceAsStream("addon_date.properties");
			ADDON_PROPERTIES.load(is);
			is.close();
		} catch (Exception e) {
		}
	}

	private static void redirectIn(String[] args) {
		InputStream is = System.in;
		try {
			if (args.length > 0) {
				is = ClassLoader.getSystemResourceAsStream(args[0]);
			}
		} catch (Exception e) {
			is = System.in;
		}
		in = new BufferedReader(new InputStreamReader(is));
	}

	public static void main(String[] args) throws Exception {
		Calendar startDate = Calendar.getInstance();
		Calendar endDate = Calendar.getInstance();
		Calendar nowDate = Calendar.getInstance();
		Configuration config = null;
		SessionFactory sessionFactory;
		Session session = null;
		Transaction tx = null;
		SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		SimpleDateFormat totalFormat = new SimpleDateFormat("yyyyMM");
		
		
		loadProps();
		redirectIn(args);
		START_DATE = ADDON_PROPERTIES.getProperty("start.date");
		END_DATE = ADDON_PROPERTIES.getProperty("end.date");
		System.out.print("Please input the start date 'YYYY/MM/DD' (" + START_DATE + "): ");
		String start = in.readLine();
		if (start.equals("")) {
			start = START_DATE;
		}

		System.out.print("Please input the end date 'YYYY/MM/DD' (" + END_DATE + "): ");
		String end = in.readLine();
		if (end.equals("")) {
			end = END_DATE;
		}
		startDate.setTime(new Date(start));
		endDate.setTime(new Date(end));
		if (startDate.getTime().after(endDate.getTime())) {
			System.out.println("The start date is after end date");
			return;
		}
		try {
			config = new Configuration().configure("hibernate-addon.cfg.xml");
			sessionFactory = config.buildSessionFactory();
			session = sessionFactory.openSession();

			
			//move subtotal data
			while (totalFormat.format(startDate.getTime()).compareTo(totalFormat.format(endDate.getTime())) <= 0) {
				String moveToTotalFormat ="";
				String moveFromTotalFormat = "";
				String moveToTable="";
				String moveFromTable = "";
				
				if (totalFormat.format(startDate.getTime()).equals(totalFormat.format(nowDate.getTime()))){
					moveFromTable = "VB_Time_Subtotal";
					moveFromTotalFormat = totalFormat.format(startDate.getTime());
					startDate.add(Calendar.MONTH, 1);
				}else{
					startDate.add(Calendar.MONTH, 1);
					moveFromTotalFormat = totalFormat.format(startDate.getTime());
					if (totalFormat.format(startDate.getTime()).equals(totalFormat.format(nowDate.getTime()))){
						moveFromTable = "VB_Time_Subtotal";
						startDate.add(Calendar.MONTH, 1);
					}else{
						moveFromTable = "VB_Time_Subtotal_" + moveFromTotalFormat;
					}
				}
				StringBuffer minDateSQL = new StringBuffer("select min(surfDate) from ");
				minDateSQL.append(moveFromTable);			
				Query q = session.createSQLQuery(minDateSQL.toString());
				List l = q.list();
				if (l.get(0) == null){
					continue;
				}
				Date tempDate = new Date(dateFormat.format((Date) l.get(0)));
				Calendar minDate = Calendar.getInstance();
				minDate.setTime(tempDate);
				
				if (totalFormat.format(minDate.getTime()).equals(moveFromTotalFormat)){
					continue;
				}else{
					while(!totalFormat.format(minDate.getTime()).equals(moveFromTotalFormat)){
						moveToTotalFormat = totalFormat.format(minDate.getTime());
						moveToTable = "VB_Time_Subtotal_" + moveToTotalFormat;
						
						tx = session.beginTransaction();
						StringBuffer updateSQL = new StringBuffer("update ");
						updateSQL.append(moveToTable).append(" a1 set second_= (select (a1.second_ + a2.second_) from  ");
						updateSQL.append(moveFromTable).append(" a2 where a1.memberId = a2.memberId and a1.surfDate = a2.surfDate and ");
						updateSQL.append(" DATE_FORMAT(a2.surfDate,'%Y%m') = '");
						updateSQL.append(moveToTotalFormat).append("' ) ");
						session.createSQLQuery(updateSQL.toString()).executeUpdate();
						StringBuffer deleteSQL = new StringBuffer("delete a1.* from ");
						deleteSQL.append(moveFromTable).append(" a1 , ");
						deleteSQL.append(moveToTable).append(" a2 where a1.memberId = a2.memberId and a1.surfDate = a2.surfDate");
						deleteSQL.append(" and DATE_FORMAT(a1.surfDate,'%Y%m') = '").append(moveToTotalFormat).append("'");
						session.createSQLQuery(deleteSQL.toString()).executeUpdate();
						StringBuffer insertSQL = new StringBuffer("insert into "); 
						insertSQL.append(moveToTable).append(" (memberId,surfDate,year,month,day,second_) ");
						insertSQL.append(" select memberId,surfDate,year,month,day,second_ from ");
						insertSQL.append(moveFromTable).append(" where DATE_FORMAT(surfDate,'%Y%m') = '");
						insertSQL.append(moveToTotalFormat).append("'");
						session.createSQLQuery(insertSQL.toString()).executeUpdate();
						StringBuffer deleteFrom = new StringBuffer("delete from ");
						deleteFrom.append(moveFromTable).append(" where DATE_FORMAT(surfDate,'%Y%m') = '");
						deleteFrom.append(moveToTotalFormat).append("'");
						session.createSQLQuery(deleteFrom.toString()).executeUpdate();
						
						tx.commit();
						
						minDate.add(Calendar.MONTH, 1);
					}
					
				}	
			}
			
			startDate.setTime(new Date(start));
			endDate.setTime(new Date(end));
			//move time track data
			while (startDate.getTime().before(endDate.getTime())
					|| startDate.getTime().equals(endDate.getTime())) {
				String moveFromFormat;
				String moveFromTable;
				
				tx = session.beginTransaction();
				
				moveFromFormat = bartDateFormat.format(startDate.getTime());
				if (bartDateFormat.format(startDate.getTime()).equals(
						bartDateFormat.format(nowDate.getTime()))) {
					moveFromTable = "VB_Time_Track";
				} else {
					moveFromTable = "VB_Time_Track_" + moveFromFormat;
				}
				
				
				StringBuffer minDateSQL = new StringBuffer("select min(beginTime) from ");
				minDateSQL.append(moveFromTable);
				
				Query q = session.createSQLQuery(minDateSQL.toString());
				List l = q.list();
				Date tempDate = new Date(dateFormat.format((Date) l.get(0)));
				Calendar minDate = Calendar.getInstance();
				
				minDate.setTime(tempDate);
				if (bartDateFormat.format(minDate.getTime()).equals(bartDateFormat.format(startDate.getTime()))) {
					startDate.add(Calendar.DAY_OF_YEAR, 1);
					continue;
				} else {
					while (!bartDateFormat.format(minDate.getTime()).equals(bartDateFormat.format(startDate.getTime()))) {
						String tempMinDate = bartDateFormat.format(minDate
								.getTime());
						
						StringBuffer insertSQl = new StringBuffer("insert into ");
						insertSQl.append("VB_Time_Track_").append(tempMinDate);
						insertSQl.append(" (memberId,submitId,beginTime,endTime,point,status) ");
						insertSQl.append("select memberId,submitId,beginTime,endTime,point,status from ");
						insertSQl.append(moveFromTable);
						insertSQl.append(" where  DATE_FORMAT(beginTime,'%Y%m%d') = '").append(tempMinDate).append("'");
						session.createSQLQuery(insertSQl.toString()).executeUpdate();
						StringBuffer deletSQL = new StringBuffer("delete from ");
						deletSQL.append(moveFromTable).append(" where DATE_FORMAT(beginTime,'%Y%m%d')= '");
						deletSQL.append(tempMinDate).append("'");
						session.createSQLQuery(deletSQL.toString()).executeUpdate();
						minDate.add(Calendar.DAY_OF_YEAR, 1);
					}
				}
				tx.commit();
				startDate.add(Calendar.DAY_OF_YEAR, 1);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			try {
				tx.rollback();
			} catch (Exception e1) {
			}
		} finally {
			try {
				session.close();
			} catch (Exception e2) {
			}
		}
	}
}
