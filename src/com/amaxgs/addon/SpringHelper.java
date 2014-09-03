package com.amaxgs.addon;

import com.liferay.portal.util.PropsUtil;

public class SpringHelper {

//	private static ApplicationContext ctx = SpringUtil.getContext();
//	private static AppendClassPathApplicationContext internalCtx = new AppendClassPathApplicationContext(null, true, ctx);

//	public static void addConfigure(String configureFile) {
//		internalCtx.addConfigure(configureFile);
//	}

	public static void addSpringConfig(String fileName) {
		String springConfigs = PropsUtil.get(PropsUtil.SPRING_CONFIGS);
		PropsUtil.set(PropsUtil.SPRING_CONFIGS, springConfigs + "," + fileName);
	}

	public static void addHibernateConfig(String fileName) {
		String hibernateConfigs = PropsUtil.get(PropsUtil.HIBERNATE_CONFIGS);
		PropsUtil.set(PropsUtil.HIBERNATE_CONFIGS, hibernateConfigs + "," + fileName);
	}

//	private final static class AppendClassPathApplicationContext extends ClassPathXmlApplicationContext {
//		public AppendClassPathApplicationContext(String[] configLocations, boolean refresh, ApplicationContext parent) throws BeansException {
//			super(configLocations, refresh, parent);
//		}
//
//		public void addConfigure(String configureFile) {
//			String[] configLocations = this.getConfigLocations();
//			List list = new ArrayList();
//			for(int i = 0; i < configLocations.length; i++) list.add(configLocations[i]);
//			list.add(configureFile);
//			
//			this.refresh();
//		}
//	}
}
