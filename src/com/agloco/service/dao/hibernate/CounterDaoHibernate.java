package com.agloco.service.dao.hibernate;

import org.hibernate.LockMode;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.agloco.Constants;
import com.agloco.model.AGCounter;
import com.agloco.service.dao.CounterDao;
import com.agloco.service.util.CodeServiceUtil;
import com.liferay.portal.model.User;

/**
 * 
 * @author terry_zhao
 * 
 */
public class CounterDaoHibernate extends HibernateDaoSupport implements
		CounterDao {

	private static final String MIN_MEMBER_CODE = "AAAA0000";
	private static final String COUNTER_NAME = "com.agloco.model.AGMemberTemp";
	
	private static final String DEFAULT_CURRENT_USER_ID = "100000";
	private static final int MINIMUM_INCREMENT_SIZE = 1;
	private static final String USER_NAME = User.class.getName();
	
//	public synchronized String generateMemberCode(String className){
//		
//		WordUtil.reloadBadPatterns();
//		WordUtil.reloadReservedPatterns();
//		
//		Connection con = getSession().connection();
//		Statement stmt = null;
//		ResultSet rs = null;
//		
//		String lockTable = "LOCK TABLES AG_Counter WRITE";
//		String unlockTable = "UNLOCK TABLES";
//		String getCounter = "select value_ from AG_Counter where name='" + COUNTER_NAME + "'";
//		String _value = null;
//		
//		try {
//			
//			stmt = con.createStatement();
//			stmt.execute(lockTable);
//			rs = stmt.executeQuery(getCounter);
//			while(rs != null && rs.next()){
//				_value = rs.getString(1);
//			}
//			
//			if(StringUtils.isBlank(_value)){
//				String insertCounter = "insert into AG_Counter(name,value_) values('" + COUNTER_NAME + "','" 
//																					  + MIN_MEMBER_CODE + "')";
//				stmt.executeUpdate(insertCounter);
//				_value = MIN_MEMBER_CODE;
//			}
//			
//			String newValue = CodeServiceUtil.generateCode(_value);
//			String updateCounter = "update AG_Counter set value_='" + newValue + 
//									"' where name='" + COUNTER_NAME + "'";
////			System.out.println("######"+updateCounter);
//			stmt.executeUpdate(updateCounter);
//			_value = newValue;
//		
//		} catch (SQLException e) {
//			throw new LockTableException();
//			
//		}
//		finally{
//			
//			try {
//				
//				stmt.execute(unlockTable);
//				if(rs != null){
//					rs.close();
//				}
//				if(stmt != null){
//					stmt.close();
//				}
//				
//			} catch (SQLException e) {
//			
//				throw new LockTableException();
//			}
//			
//		}
//		
//		return _value;
//	}

	public synchronized String generateMemberCode(String className) {
		
		
		AGCounter counter = (AGCounter) getHibernateTemplate().get(AGCounter.class, className,LockMode.UPGRADE);

		// if have no record, new one
		if (counter == null) {
			counter = new AGCounter();
			counter.setName(className);
			counter.setValue(MIN_MEMBER_CODE);
			getHibernateTemplate().save(counter);
			return counter.getValue();
		}
		// if have record, generate a new code and update it
		String newCode = CodeServiceUtil.generateCode(counter.getValue());
		counter.setValue(newCode);
		getHibernateTemplate().saveOrUpdate(counter);
		return counter.getValue();
	}
	
	public synchronized String generateUserId(){
		
		AGCounter counter = (AGCounter)getHibernateTemplate().get(AGCounter.class, USER_NAME, LockMode.UPGRADE);
		
		// if have no record, new one
		if (counter == null) {
			counter = new AGCounter();
			counter.setName(USER_NAME);
			counter.setValue(DEFAULT_CURRENT_USER_ID);
			getHibernateTemplate().save(counter);
			return counter.getValue();
		}
		
		// if have record, generate a new code and update it
		int currentId = Integer.parseInt(counter.getValue()) + MINIMUM_INCREMENT_SIZE;
		counter.setValue(new Integer(currentId).toString());
		getHibernateTemplate().saveOrUpdate(counter);
		return counter.getValue();
		
	}

	//add at 2007-03-05
	public synchronized String generateAdminMemberCode(){
		AGCounter counter = (AGCounter) getHibernateTemplate().get(AGCounter.class, Constants.ADMIN_USER_MEMBER_CODE_COUNTER,LockMode.UPGRADE);

		// if have no record, new one
		if (counter == null) {
			counter = new AGCounter();
			counter.setName(Constants.ADMIN_USER_MEMBER_CODE_COUNTER);
			counter.setValue(Constants.ADMIN_USER_MEMBER_CODE_MIN_NUMBER_VALUE);
			getHibernateTemplate().save(counter);
			return counter.getValue();
		}
		// if have record, generate a new code and update it
		int currentId = Integer.parseInt(counter.getValue()) + Constants.ADMIN_USER_MEMBER_CODE_MINIMUM_INCREMENT_SIZE;
		counter.setValue(new Integer(currentId).toString());
		getHibernateTemplate().saveOrUpdate(counter);
		return counter.getValue();
	}
	
	public synchronized String generateAdminUserId(){
		AGCounter counter = (AGCounter) getHibernateTemplate().get(AGCounter.class, Constants.ADMIN_USER_USER_ID_COUNTER,LockMode.UPGRADE);

		// if have no record, new one
		if (counter == null) {
			counter = new AGCounter();
			counter.setName(Constants.ADMIN_USER_USER_ID_COUNTER);
			counter.setValue(Constants.ADMIN_USER_USER_ID_MIN_NUMBER_VALUE);
			getHibernateTemplate().save(counter);
			return counter.getValue();
		}
		// if have record, generate a new code and update it
		int currentId = Integer.parseInt(counter.getValue()) + Constants.ADMIN_USER_USER_ID_MINIMUM_INCREMENT_SIZE;
		counter.setValue(new Integer(currentId).toString());
		getHibernateTemplate().saveOrUpdate(counter);
		return counter.getValue();
	}

	public AGCounter getAGCounter(String pk){
		return (AGCounter)getHibernateTemplate().get(AGCounter.class, pk);
	}
}
