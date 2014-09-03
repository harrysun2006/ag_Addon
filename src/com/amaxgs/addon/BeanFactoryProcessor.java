package com.amaxgs.addon;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.support.RootBeanDefinition;

public class BeanFactoryProcessor implements BeanFactoryPostProcessor {

	private static Log _log = LogFactory.getLog(BeanFactoryProcessor.class);

	private final static String beanNameSchedulerFactory = "org.springframework.scheduling.quartz.SchedulerFactoryBean";
	private final static String aglocoMailQCF = "com.agloco.mail.MailQCF";
	private final static String addonMailQCF = "com.addon.mail.MailQCF";
	private final static String propNameTriggers = "triggers";
	private final static String[] beanNameCronTriggers = {
		"sendMailTriggerByWeek", 
		"sendMailJobByWeek",
		"sendSignUpEmailTaskByWeek",
		"updateAGMemberCountTrigger",
		"updateAGMemberCountJob",
		"updateAGMemberCountTask",
		"dailyReportTrigger",
		"dailyReport",
		"dailyReportTask",
		"weeklyReportTrigger",
		"weeklyReport",
		"weeklyReportTask",
		"monthlyReportTrigger",
		"monthlyReport",
		"monthlyReportTask",
		"scanMailQueueTrigger",
		"scanMailQueueJob",
		"scanMailQueueTask",
	};
	private DefaultListableBeanFactory beanFactory;

	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
			throws BeansException {
		this.beanFactory = (DefaultListableBeanFactory) beanFactory;
		hookSchedulerFactory();
		hookCronTriggers();
		hookMailQCF();
	}

	private void hookSchedulerFactory() {
		try {
			AbstractBeanDefinition bd = (AbstractBeanDefinition) beanFactory.getBeanDefinition(beanNameSchedulerFactory);
			MutablePropertyValues props = bd.getPropertyValues();
			PropertyValue[] propValues = props.getPropertyValues();
			PropertyValue propValue = null;
			for(int i = 0; i < propValues.length; i++) {
				propValue = propValues[i];
				if(propNameTriggers.equals(propValue.getName())) {
					ManagedList triggers = (ManagedList) propValue.getValue();
					triggers.clear();
					propValue = new PropertyValue(propValue.getName(), triggers);
					props.setPropertyValueAt(propValue, i);
				}
			}
		} catch(Exception e) {
			System.out.println(e);
			_log.error(e, e);
		}
	}

	private void hookCronTriggers() {
		RootBeanDefinition blank = new RootBeanDefinition(Object.class);
		beanFactory.setAllowBeanDefinitionOverriding(true);
		for(int i = 0; i < beanNameCronTriggers.length; i++) {
			try {
				beanFactory.registerBeanDefinition(beanNameCronTriggers[i], blank);
			} catch(Exception e) {
				System.out.println(e);
				_log.error(e, e);
			}
		}
	}

	private void hookMailQCF() {
		copyProperties(addonMailQCF, aglocoMailQCF);
	}

	private void copyProperties(String srcBeanName, String destBeanName) {
		copyProperties(srcBeanName, destBeanName, true);
	}

	private void copyProperties(String srcBeanName, String destBeanName, boolean overwrite) {
		try {
			AbstractBeanDefinition sbd = (AbstractBeanDefinition) beanFactory.getBeanDefinition(srcBeanName);
			AbstractBeanDefinition dbd = (AbstractBeanDefinition) beanFactory.getBeanDefinition(destBeanName);
			if(overwrite) {
				dbd.setPropertyValues(sbd.getPropertyValues());
			} else {
			}
		} catch(Exception e) {
			System.out.println(e);
			_log.error(e, e);
		}
	}
}
