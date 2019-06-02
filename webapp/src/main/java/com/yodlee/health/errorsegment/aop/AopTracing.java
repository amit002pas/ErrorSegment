package com.yodlee.health.errorsegment.aop;

import javax.inject.Named;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * 
 * @author DSingh1 AopTracing: class for centralize logging and tracing
 *
 */
@Named
@Aspect
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class AopTracing {

	Logger logger = LoggerFactory.getLogger(AopTracing.class);

	@Around("execution(* com.yodlee.iae.presr..*(..))")
	public Object LoggingForRTDA(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		String methodName = proceedingJoinPoint.getSignature().getName();
		logger.info("method invoked  " + methodName);
		
		Object result = null;
		result = proceedingJoinPoint.proceed();
		logger.info("returned from method  " + methodName);
		
		return result;
	}
}
