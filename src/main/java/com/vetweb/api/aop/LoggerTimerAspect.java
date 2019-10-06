package com.vetweb.api.aop;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class LoggerTimerAspect {
	
	@Pointcut("execution(* com.vetweb.api.resources..*.*(..))")
	public void everyMethod() {}

	@Around("everyMethod()")
	public Object logAndCount(ProceedingJoinPoint joinPoint) throws Throwable {
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		log.info(String.format("Method %s called at %s, return type is %s", 
				methodSignature.getName(), LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")), methodSignature.getReturnType()));
		long start = System.currentTimeMillis();
		Object returningObj = joinPoint.proceed();
		long end = System.currentTimeMillis();
		long timeToComplete = (start - end)/1000;
		log.info(String.format("Method took %d to complete execution, returning %s", timeToComplete, returningObj));
		return returningObj;
	}

}
