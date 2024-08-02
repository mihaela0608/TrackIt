package com.example.trackit.service;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;

@Aspect
@Component
public class DeleteUserAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteUserAspect.class);
    @Pointcut("execution(* com.example.trackit.service.impl.UserServiceImpl.deleteUser(..))")
    void trackUserDeleteMethod(){}

    @AfterThrowing(value = "trackUserDeleteMethod()", throwing = "error")
    public void onError(JoinPoint joinPoint, Throwable error){
        LOGGER.warn("User access delete URL");
    }

}
