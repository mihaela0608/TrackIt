package com.example.trackit.service;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TrackRegistrationAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrackRegistrationAspect.class);

    @Around("execution(* com.example.trackit.service.impl.UserServiceImpl.registerUser(..))")
    public Object trackUserRegistrationLessThan3Sec(ProceedingJoinPoint pjp) throws Throwable {
       long start = System.currentTimeMillis();
       Object proceed = pjp.proceed();
       long executionTime = System.currentTimeMillis() - start;
       if (executionTime > 3000){
           LOGGER.warn("Registration took {} ms to execute", executionTime);
       }
       return proceed;
    }
}
