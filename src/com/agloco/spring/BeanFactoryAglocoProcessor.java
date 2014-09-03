package com.agloco.spring;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.ManagedList;

import com.agloco.Constants;
import com.liferay.portal.util.PropsUtil;
import com.liferay.util.StringPool;

public class BeanFactoryAglocoProcessor implements BeanFactoryPostProcessor {

	private static Log _log = LogFactory.getLog(BeanFactoryAglocoProcessor.class);
	private final static String beanNameSessionFactory = "liferaySessionFactory";
	private static ConfigurableListableBeanFactory beanFactory;

	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
			throws BeansException {
		BeanFactoryAglocoProcessor.beanFactory = beanFactory;
		hookHibernateConfiguration();
		hookBeans();
		hookBeansPropertyValues();
	}

	private void hookHibernateConfiguration() {
		try {
			AbstractBeanDefinition bd = (AbstractBeanDefinition) beanFactory.getBeanDefinition(beanNameSessionFactory);
			bd.setBeanClass(HibernateConfiguration.class);
		} catch(Exception e) {
			_log.error("Hook Hibernate Configuration Failed!", e);
		}
	}

	private void hookBeans() {
		try {
			String s = PropsUtil.get(Constants.SPRING_BEAN_HOOK);
			if(s == null) s = "";
			String[] beans = s.split(",");
			String id, clazz;
			AbstractBeanDefinition bd;
			for(int i = 0; i < beans.length; i++) {
				id = PropsUtil.get(Constants.SPRING_BEAN_ID_PREFIX + beans[i]);
				clazz = PropsUtil.get(Constants.SPRING_BEAN_CLASS_PREFIX + beans[i]);
				if(id == null) {
					_log.warn(beans[i] + "'s bean id not defined!");
					continue;
				}
				if(clazz == null) {
					_log.warn(beans[i] + "'s bean class not defined!");
					continue;
				}
				try {
					bd = (AbstractBeanDefinition) beanFactory.getBeanDefinition(id);
					bd.setBeanClass(Class.forName(clazz));
					_log.info("bean " + id + " changed class to: " + clazz);
				} catch(Exception e) {
					_log.error("bean " + id + " override failed!", e);
				}
			}
		} catch(Exception e) {
			_log.error("hook beans failed!", e);
		}
	}
	
	private void hookBeansPropertyValues(){
		try {
			String s = PropsUtil.get(Constants.SPRING_BEAN_PROPERTYVALUES_HOOK);
			if (s == null || s.equals("")) return;
			String[] beans = s.split(",");
			String value, id;
			AbstractBeanDefinition targetBean, valueBean;
			MutablePropertyValues propertyValues;
			for (int i = 0; i < beans.length; i++) {
				id = PropsUtil.get(Constants.SPRING_BEAN_PROPERTYVALUES_ID	+ beans[i]);
				try {
					targetBean = (AbstractBeanDefinition) beanFactory.getBeanDefinition(id);
					propertyValues = targetBean.getPropertyValues();
					String property = PropsUtil.get(Constants.SPRING_BEAN_PROPERTYVALUES_PROPERTY	+ beans[i]);
					String[] properties = property.split(",");
					for (int j = 0; j < properties.length; j++) {
						value = PropsUtil.get(Constants.SPRING_BEAN_PROPERTYVALUES_VALUE + beans[i] + "." + properties[j]);
						String[] values = value.split(StringPool.COMMA);
						ManagedList ml = new ManagedList();
						for(int k = 0; k < values.length; k++){
							valueBean = (AbstractBeanDefinition) beanFactory.getBeanDefinition(values[k]);
							if(valueBean != null){
								ml.add(valueBean);	
							}
							
						}
						
						propertyValues.addPropertyValue(properties[j], ml);
						_log.info("property[" + properties[j] + " = " + value + "] added to " + id);
					}
					targetBean.setPropertyValues(propertyValues);
					
				} catch (Exception e) {
					_log.error("bean: " + id + " add properties failed!", e);
				}
			}
		} catch (Exception e) {
			_log.error("hook beans properties failed!", e);
		}
	}
	
}
