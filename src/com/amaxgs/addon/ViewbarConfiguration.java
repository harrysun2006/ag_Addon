package com.amaxgs.addon;

import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;

import com.liferay.portal.kernel.util.StackTraceUtil;
import com.liferay.portal.spring.hibernate.HibernateConfiguration;
import com.liferay.portal.util.PropsUtil;
import com.liferay.util.StringUtil;

public class ViewbarConfiguration extends LocalSessionFactoryBean {

	protected Configuration newConfiguration() {
		Configuration cfg = new Configuration();

		try {
			ClassLoader classLoader = getClass().getClassLoader();

			String[] configs = StringUtil.split(
				PropsUtil.get(PropsUtil.HIBERNATE_CONFIGS));

			for (int i = 0; i < configs.length; i++) {
				try {
					InputStream is =
						classLoader.getResourceAsStream(configs[i]);

					if (is != null) {
						cfg = cfg.addInputStream(is);

						is.close();
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}

			cfg.setProperties(PropsUtil.getProperties());
		}
		catch (Exception e) {
			_log.error(StackTraceUtil.getStackTrace(e));
		}

		return cfg;
	}

	private static Log _log = LogFactory.getLog(HibernateConfiguration.class);

}