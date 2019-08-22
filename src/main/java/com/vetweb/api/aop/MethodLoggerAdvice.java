package com.vetweb.api.aop;

import java.time.LocalDateTime;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MethodLoggerAdvice implements MethodInterceptor {//Around advice, AOP Alliance
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MethodLoggerAdvice.class);

	@Override
	public Object invoke(MethodInvocation joinPoint) throws Throwable {
			//Receive target method invocation as parameter, allows around advice (Controls intercepted method proceed moment)
		LOGGER.info(String.format("Method %s called at %s, return type is %s", 
					joinPoint.getMethod().getName(), LocalDateTime.now().toString(), joinPoint.getMethod().getReturnType()));
			//Advice logic, showing original join point information
		return joinPoint.proceed();//Original method execution
	}

}
