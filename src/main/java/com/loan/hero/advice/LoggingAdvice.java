package com.loan.hero.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Aspect
@Component
@RequiredArgsConstructor
public class LoggingAdvice {
    private final ObjectMapper objectMapper;
    private final Logger log = LoggerFactory.getLogger(LoggingAdvice.class);

    @Pointcut(value = "execution(* com.loan.hero.*.*.*.*(..))")
    public void heroPointcut() {
    }

    @Around("heroPointcut()")
    public Object heroLogger(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getTarget().toString();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        log.info(" Class: {}, Method invoked: {}, Arguments passed: {}",
                className, methodName, objectMapper.writeValueAsString(arguments));
        Object returnedProceed = joinPoint.proceed();
        log.info(" Class: {}, Method invoked: {}, Response returned: {}",
                className, methodName, objectMapper.writeValueAsString(returnedProceed));
        return returnedProceed;
    }


}
