package com.sparcs.tracer;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Aspect
public class TracerAspect {

    final static Logger LOG = LoggerFactory.getLogger("TRACER");

    //@Pointcut("execution(public * com.sparcs..*(..))")
    @Pointcut("within(com.sparcs..*)")
    public void publicMethod() {}

    // https://github.com/nickvl/aop-logging
    @Around("publicMethod()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {

        if (LOG.isTraceEnabled()) {
            LOG.trace("Enter: {}.{}() with argument[s] = {}", joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
        }

        try {

            Object result = joinPoint.proceed();

            if (LOG.isTraceEnabled()) {
                LOG.trace("Exit: {}.{}() with result = {}", joinPoint.getSignature().getDeclaringTypeName(),
                        joinPoint.getSignature().getName(), result);
            }

            return result;

        } catch (IllegalArgumentException e) {
            LOG.error("Illegal argument: {} in {}.{}()", Arrays.toString(joinPoint.getArgs()),
                    joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());

            throw e;
        }
    }
}
