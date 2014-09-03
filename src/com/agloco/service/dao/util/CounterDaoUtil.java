package com.agloco.service.dao.util;

import org.springframework.context.ApplicationContext;

import com.agloco.model.AGCounter;
import com.agloco.service.dao.CounterDao;
import com.liferay.portal.spring.util.SpringUtil;
/**
 * 
 * @author terry_zhao
 *
 */
public class CounterDaoUtil {

	private CounterDao counterDao;

	public static String generateMemberCode(String className){
		return getCounterDao().generateMemberCode(className);
	}
	
	public static String generateUserId(){
		return getCounterDao().generateUserId();
	}
	
	//add at 2007-03-05
	public static String generateAdminMemberCode(){
		return getCounterDao().generateAdminMemberCode();
	}
	public static String generateAdminUserId(){
		return getCounterDao().generateAdminUserId();
	}
	public static AGCounter getAGCounter(String pk){
		return getCounterDao().getAGCounter(pk);
	}
	
	public static CounterDao getCounterDao(){
		ApplicationContext ctx = SpringUtil.getContext();
		CounterDaoUtil util = (CounterDaoUtil) ctx.getBean(CounterDaoUtil.class.getName());
		return util.counterDao;
	}
	
	public void setCounterDao(CounterDao counterDao) {
		this.counterDao = counterDao;
	}
}
