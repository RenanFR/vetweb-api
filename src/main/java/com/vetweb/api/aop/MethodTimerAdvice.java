package com.vetweb.api.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MethodTimerAdvice implements MethodInterceptor {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MethodTimerAdvice.class);

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		long start = System.currentTimeMillis();
		Object joinPoint = invocation.proceed();
		long end = System.currentTimeMillis();
		long timeToComplete = start - end;
		LOGGER.info(String.format("Method took %x to complete execution", timeToComplete));
		return joinPoint;
	}

}
