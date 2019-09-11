package com.vetweb.api.config.scheduling;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

public class AutoWiringSpringBeanJobFactory extends SpringBeanJobFactory implements ApplicationContextAware {
	
	private transient AutowireCapableBeanFactory beanFactory;
	
	public void setContext(ApplicationContext applicationContext) throws BeansException {
		beanFactory = applicationContext.getAutowireCapableBeanFactory();
	}
	
	@Override
	protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
		Object job = super.createJobInstance(bundle);
		beanFactory.autowireBean(job);
		return job;
	}

}
