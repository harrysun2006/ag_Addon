package com.agloco.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javassist.ClassPool;
import javassist.CtClass;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Table;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;

import com.agloco.Constants;
import com.liferay.portal.spring.util.SpringUtil;
import com.liferay.portal.util.PropsUtil;
import com.liferay.util.StringUtil;

public class HibernateUtil {

	public static final String DYNA_POCLASS_PREFIX = "com.dynapo.";
	private static final String LIFERAY_DATASOURCE = "liferayDataSource";
	private static final String SPRING_HIBERNATE_SESSION_FACTORY = PropsUtil.get(PropsUtil.SPRING_HIBERNATE_SESSION_FACTORY);
	private static final Pattern HBM_CLASS_PATTERN = Pattern.compile("<class(.*?)name\\s*=\\s*\"(.*?)\"(.*?)>.*?</class>", Pattern.DOTALL + Pattern.MULTILINE);
	private static final Pattern HBM_TABLE_PATTERN = Pattern.compile("table\\s*=\\s*\"(.*?)\"", Pattern.DOTALL + Pattern.MULTILINE);
	private static final Pattern HBM_XML_PATTERN = Pattern.compile("<\\?xml.*?\\?>", Pattern.DOTALL + Pattern.MULTILINE);
	private static final Pattern HBM_DTD_PATTERN = Pattern.compile("<!DOCTYPE.*?>", Pattern.DOTALL + Pattern.MULTILINE);
	private static final Pattern HBM_HBM_PATTERN = Pattern.compile("<hibernate-mapping.*?>", Pattern.DOTALL + Pattern.MULTILINE);
	private static final String HBM_XML = "<?xml version=\"1.0\"?>";
	private static final String HBM_DTD = "<!DOCTYPE hibernate-mapping PUBLIC \"-//Hibernate/Hibernate Mapping DTD 3.0//EN\" \"http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd\">";
	private static final String HBM_HBM_OPEN = "<hibernate-mapping>";
	private static final String HBM_HBM_CLOSE = "</hibernate-mapping>";
	
	private static final String DEFAULT_AGLOCO_DB_NAME  = "default.agloco.database.name";
	private static final String DEFAULT_VIEWBAR_DB_NAME = "default.viewbar.database.name";
	private static String AGLOCO_DB_NAME  = null;
	private static String VIEWBAR_DB_NAME = null;
	
	private static final Log _log = LogFactory.getLog(HibernateUtil.class);

	private HibernateUtil() {
		
	}

	public static SessionFactoryImplementor getSessionFactory() {
		return getSessionFactory(SPRING_HIBERNATE_SESSION_FACTORY);
	}

	public static SessionFactoryImplementor getSessionFactory(String sessionFactoryName) {
		ApplicationContext ctx = SpringUtil.getContext();
		LocalSessionFactoryBean lsfb = (LocalSessionFactoryBean)ctx.getBean(sessionFactoryName);
		return (SessionFactoryImplementor)lsfb.getObject();
	}

	public static String generateSchemaCreationScript(String className) {
		return generateSchemaCreationScript(null, className, null);
	}

	public static String generateSchemaCreationScript(String className, String tableName) {
		return generateSchemaCreationScript(null, className, tableName);
	}

	public static String generateSchemaCreationScript(Configuration config, String className) {
		return generateSchemaCreationScript(config, className, null);
	}

	public static String generateSchemaCreationScript(Configuration config, String className, String tableName) {
		if(config == null) {
			String sessionFactoryName = PropsUtil.get(PropsUtil.SPRING_HIBERNATE_SESSION_FACTORY);
			ApplicationContext ctx = SpringUtil.getContext();
			LocalSessionFactoryBean lsfb = (LocalSessionFactoryBean)ctx.getBean(sessionFactoryName);
			config = lsfb.getConfiguration();
		}
		return generateSchemaCreationScript2(config, className, tableName, false);
	}

	public static String createTable(String className) {
		return createTable(null, className, null);
	}

	public static String createTable(String className, String tableName) {
		return createTable(null, className, tableName);
	}

	public static String createTable(Configuration config, String className) {
		return createTable(config, className, null);
	}

	public static String createTable(Configuration config, String className, String tableName) {
		if(config == null) {
			String sessionFactoryName = PropsUtil.get(PropsUtil.SPRING_HIBERNATE_SESSION_FACTORY);
			ApplicationContext ctx = SpringUtil.getContext();
			LocalSessionFactoryBean lsfb = (LocalSessionFactoryBean)ctx.getBean(sessionFactoryName);
			config = lsfb.getConfiguration();
		}
		return generateSchemaCreationScript2(config, className, tableName, true);
	}

	/**
	 * @param config
	 * @param className
	 * @param tableName
	 * @return
	 */
	/*
	private static String generateSchemaCreationScript1(Configuration config, String className, String tableName, boolean script) {
		String[] sqls = {};
		try {
			Properties props = config.getProperties();
//		props.remove(Environment.CONNECTION_PROVIDER);
//		props.put(Environment.DATASOURCE, "java:comp/env/jdbc/LiferayPool");
			props.put(Environment.HBM2DDL_AUTO, String.valueOf(script));
			PersistentClass clazz = config.getClassMapping(className);
			Table table = clazz.getTable();
			if(tableName == null) tableName = table.getName();
			Map tables = new Hashtable();
			tables.put(tableName, table);
			LocalSessionFactoryBean dynaLsfb = new DynaHibernateConfiguration(props, tables);
			dynaLsfb.afterPropertiesSet();
			Configuration dynaConfig = dynaLsfb.getConfiguration();
			Dialect dialect = Dialect.getDialect(dynaConfig.getProperties());
			sqls = dynaConfig.generateSchemaCreationScript(dialect);
			if(_log.isDebugEnabled()) {
				_log.debug("total " + sqls.length + " tables!");
				for(int i = 0; i < sqls.length; i++) {
					_log.debug("sqls[" + i + "] = " + sqls[i]);
				}
			}
			if(script) {
				SchemaExport se = new SchemaExport(dynaConfig);
				se.setDelimiter(";");
				se.create(script, false);
			}
		} catch(Exception e) {
			_log.error(e, e);
		}
		return (sqls.length > 0) ? sqls[0] : "";
	}
	*/

	/**
	 * @param config
	 * @param className
	 * @param tableName
	 * @return
	 */
	private static String generateSchemaCreationScript2(Configuration config, String className, String tableName, boolean script) {
		String sql = "";
		DataSource ds = null;
		Connection conn = null;
		Statement stmt = null;
		try {
			PersistentClass clazz = config.getClassMapping(className);
			Table table = clazz.getTable();
			String oldName = table.getName();
			if(tableName == null) tableName = table.getName();
			table.setName(tableName);
			Properties props = config.getProperties();
			Dialect dialect = Dialect.getDialect(props);
			String catalog = props.getProperty(Environment.DEFAULT_CATALOG);
			String schema = props.getProperty(Environment.DEFAULT_SCHEMA);
			sql = table.sqlCreateString(dialect, getSessionFactory(), catalog, schema);
			table.setName(oldName);
			if(script) {
				ApplicationContext ctx = SpringUtil.getContext();
				ds = (DataSource) ctx.getBean(LIFERAY_DATASOURCE);
				conn = ds.getConnection();
				stmt = conn.createStatement();
				stmt.executeUpdate(sql);
			}
		} catch(Exception e) {
			_log.error(e, e);
		} finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) {
					conn.close();
					conn = null;
				}
			} catch ( Exception e ) {
				_log.error( "Could not close connection", e);
			}
		}
		return sql;
	}

	public static void alterMapping(String className, String tableName) throws IOException {
		dynaMapping(null, className, tableName, true, className);
	}

	public static void alterMapping(Configuration config, String className, String tableName) throws IOException {
		dynaMapping(config, className, tableName, true, className);
	}

	public static String addMapping(String className, String tableName) throws IOException {
		return dynaMapping(null, className, tableName, false, null);
	}

	public static void addMapping(String className, String tableName, String newName) throws IOException {
		dynaMapping(null, className, tableName, false, newName);
	}

	public static void addMapping(Configuration config, String className, String tableName, String newName) throws IOException {
		dynaMapping(config, className, tableName, false, newName);
	}

	private static String dynaMapping(Configuration config, String className, String tableName, boolean overriding, String newName) throws IOException {
		LocalSessionFactoryBean lsfb = null;
		if(config == null) {
			String sessionFactoryName = PropsUtil.get(PropsUtil.SPRING_HIBERNATE_SESSION_FACTORY);
			ApplicationContext ctx = SpringUtil.getContext();
			lsfb = (LocalSessionFactoryBean)ctx.getBean(sessionFactoryName);
			config = lsfb.getConfiguration();
		}
		PersistentClass clazz = config.getClassMapping(className);
		Table table = clazz.getTable();
		if(overriding) {
			table.setName(tableName);
		} else {
			if(newName == null) newName = DYNA_POCLASS_PREFIX + tableName;
			clazz = config.getClassMapping(newName);
			if(clazz == null) {
				// use javassist to create a dynamic class
				ClassPool cp = ClassPool.getDefault();
				try {
					CtClass cc = cp.get(className);
					cc.replaceClassName(className, newName);
					cc.toClass();
				} catch(Exception e) {
					_log.error("can NOT create dynamic class " + newName, e);
					throw new IOException("can NOT create dynamic class " + newName);
				}
				// add newClass ==> tableName mapping into hibernate
				String xml = getHbmXml(className, tableName, newName);
				if(xml == null) throw new IOException("can NOT generate dynamic hbm mapping from " + className); 
				else config.addXML(xml);
				className = newName;
			}
		}
		config.buildMappings();
		if(lsfb != null) lsfb.afterPropertiesSet();
//		for(Iterator it = config.getClassMappings(); it.hasNext();) {
//			clazz = (PersistentClass) it.next();
//			table = clazz.getTable();
//			if(table.getName().startsWith("AA_DynaTable")) {
//				System.out.println(clazz.getClassName() + " ==> " + table.getName());
//			}
//		}
		return className;
	}

	private static String getHbmXml(String className, String tableName, String newName) throws IOException {
		ClassLoader classLoader = HibernateUtil.class.getClassLoader();
		String[] configs = StringUtil.split(PropsUtil.get(PropsUtil.HIBERNATE_CONFIGS));
		StringBuffer sb = new StringBuffer();
		byte[] b = new byte[1024];
		int len = 0;
		String xml = "";
		Matcher m1, m2, m3;
		for (int i = 0; i < configs.length; i++) {
			try {
				sb.delete(0, sb.length());
				InputStream is = classLoader.getResourceAsStream(configs[i]);
				while((len = is.read(b)) > 0) {
					sb.append(new String(b, 0, len));
				}
				is.close();
				m1 = HBM_CLASS_PATTERN.matcher(sb);
				while(m1.find()) {
					if(className.endsWith(m1.group(2))) {
						m2 = HBM_TABLE_PATTERN.matcher(m1.group(1));
						if(!m2.find()) m2 = HBM_TABLE_PATTERN.matcher(m1.group(3));
						if(m2.find()) {
							StringBuffer r = new StringBuffer();
							m3 = HBM_XML_PATTERN.matcher(sb);
							if(m3.find()) r.append(m3.group());
							else r.append(HBM_XML);
							r.append("\r\n");
							m3 = HBM_DTD_PATTERN.matcher(sb);
							if(m3.find()) r.append(m3.group());
							else r.append(HBM_DTD);
							r.append("\r\n");
							m3 = HBM_HBM_PATTERN.matcher(sb);
							if(m3.find()) r.append(m3.group());
							else r.append(HBM_HBM_OPEN);
							r.append("\r\n");
							r.append(m1.group());
							r.append("\r\n");
							r.append(HBM_HBM_CLOSE);
							xml = r.toString();
							xml = xml.replaceAll(m1.group(2), newName);
							xml = xml.replaceAll(m2.group(1), tableName);
							return xml;
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	//add at 2007-04-24
	public static String getAglocoDBName() {
		return getAglocoDBName(PropsUtil.get(DEFAULT_AGLOCO_DB_NAME));
	}

	public static String getViewbarDBName() {
		return getViewbarDBName(PropsUtil.get(DEFAULT_VIEWBAR_DB_NAME));
	}
	
	private static String getAglocoDBName(String defaultDBName) {
		if(AGLOCO_DB_NAME == null) {
			AGLOCO_DB_NAME = getDataBaseName(Constants.AGLOCO_DATASOURCE, defaultDBName); 
		}
		return AGLOCO_DB_NAME;
	}

	private static String getViewbarDBName(String defaultDBName) {
		if(VIEWBAR_DB_NAME  == null) {
			VIEWBAR_DB_NAME = getDataBaseName(Constants.VIEWBAR_DATASOURCE, defaultDBName); 
		}
		return VIEWBAR_DB_NAME;
	}
	
	
	private synchronized static String getDataBaseName(String dataSource, String defaultDBName){
		String dbName = defaultDBName;
		try {
			DataSource ds = (DataSource)SpringUtil.getContext().getBean(dataSource);
			if(ds != null && ds.getConnection() != null){
				dbName = ds.getConnection().getCatalog();
			}
		} catch (Exception e) {
			if(_log.isErrorEnabled()){
				_log.error("get database name of " + dataSource + " error! use default db name:" + defaultDBName, e);
			}
		}
		
		System.out.println("get database name of " + dataSource + " : " + dbName + " success!" );
		if(_log.isInfoEnabled()){
			_log.info("get database name of " + dataSource + " : " + dbName + " success!");
		}
		return dbName;
	}
	
	/**
	 * @deprecated Table.getCheckConstraintsIterator() and Table.getIdentifierValue() are not supported in Hibernate 3.1.3-
	 * @author harry_sun
	 *
	 */
	/*
	private static class DynaHibernateConfiguration extends LocalSessionFactoryBean {
		private Properties props;
		private Map tables;

		public DynaHibernateConfiguration(Properties props, Map tables) {
			this.props = props;
			this.tables = tables;
		}

		protected Configuration newConfiguration() throws HibernateException {
			Configuration config = super.newConfiguration();
			config.setProperties(props);

			DataSource ds = getConfigTimeDataSource();
			if(ds == null) {
				ApplicationContext ctx = SpringUtil.getContext();
				ds = (DataSource) ctx.getBean(LIFERAY_DATASOURCE);
				setDataSource(ds);
			}

			String catalog = props.getProperty(Environment.DEFAULT_CATALOG);
			String schema = props.getProperty(Environment.DEFAULT_SCHEMA);
			Mappings mappings = config.createMappings();
			if(tables != null) {
				String tableName;
				Table table, newTable;
				PrimaryKey pk, newPk;
				Column column;
				Constraint constraint;
				Index index;
				UniqueKey uk;
				for(Iterator keys = tables.keySet().iterator(); keys.hasNext(); ) {
					tableName = (String) keys.next();
					table = (Table) tables.get(tableName);
					// must addTable to generate ddl script
					newTable = mappings.addTable(schema, catalog, tableName, null, false);
					// add PK
					pk = table.getPrimaryKey();
					newPk = new PrimaryKey();
					newPk.setTable(newTable);
					for(Iterator it = pk.getColumnIterator(); it.hasNext(); ) {
						column = (Column) it.next();
						newPk.addColumn(column);
					}
					newTable.setPrimaryKey(newPk);
					// add columns
					for(Iterator it = table.getColumnIterator(); it.hasNext(); ) {
						column = (Column) it.next();
						newTable.addColumn(column);
					}
					// add constraints
					for(Iterator it = table.getCheckConstraintsIterator(); it.hasNext(); ) {
						constraint = (Constraint) it.next();
						newTable.addCheckConstraint(constraint.toString());
					}
					// add indexes
					for(Iterator it = table.getIndexIterator(); it.hasNext(); ) {
						index = (Index) it.next();
						newTable.addIndex(index);
					}
					// add unique keys
					for(Iterator it = table.getUniqueKeyIterator(); it.hasNext(); ) {
						uk = (UniqueKey) it.next();
						newTable.addUniqueKey(uk);
					}
					// add other attributes
					newTable.setAbstract(table.isAbstract());
					newTable.setCatalog(table.getCatalog());
					newTable.setComment(table.getComment());
					newTable.setName(tableName);
					newTable.setQuoted(table.isQuoted());
					newTable.setRowId(table.getRowId());
					newTable.setSchema(table.getSchema());
					newTable.setSubselect(table.getSubselect());
					newTable.setIdentifierValue(table.getIdentifierValue());
				}
			}
			return config;
		}
	}
	*/
	


}
