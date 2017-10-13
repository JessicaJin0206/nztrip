package com.fitibo.aotearoa.aspect;

import com.google.common.base.Joiner;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by zhouqianhao on 11/03/2017.
 */
@Aspect
@Component
public class ExecTimeAspect {
    private static final Logger logger = LoggerFactory.getLogger(ExecTimeAspect.class);

    @Pointcut("@annotation(com.fitibo.aotearoa.annotation.ExecTime)")
    public void annotationPointCut() {
    }

    @Pointcut("execution(public * com.fitibo.aotearoa.mapper..*.*(..))")
    public void mapperTime() {
    }

    /**
     * 统计方法执行的时长
     *
     * @param joinPoint the join point
     * @return object
     */
    @Around("annotationPointCut()")
    public Object costTime(ProceedingJoinPoint joinPoint) throws Throwable {
        return logCostTime(joinPoint);
    }

    @Around("mapperTime()")
    public Object mapperCostTime(ProceedingJoinPoint joinPoint) throws Throwable {
        return logCostTime(joinPoint);
    }

    private Object logCostTime(ProceedingJoinPoint joinPoint) throws Throwable {
        Object output;
        try {
            long start = System.currentTimeMillis();
            output = joinPoint.proceed();
            long elapsedTime = System.currentTimeMillis() - start;
            String className = joinPoint.getTarget().getClass().getSimpleName();
            String args = Arrays.stream(joinPoint.getArgs())
                    .map(input -> {
                        if (input != null) {
                            return input.toString();
                        } else {
                            return "null";
                        }
                    })
                    .collect(Collectors.joining(","));
            Optional<Class<?>> realClass = Arrays.stream(joinPoint.getTarget().getClass().getInterfaces()).findFirst();
            if (elapsedTime > 500) {
                logger.warn(String.format("method [%s.%s(%s)] execution time:%sms",
                        realClass.map(Class::getSimpleName).orElse(className),
                        joinPoint.getSignature().getName(),
                        args,
                        elapsedTime));
            }
        } catch (Throwable throwable) {
            logger.error("aop record method exec time error", throwable);
            throw throwable;
        }
        return output;
    }
}
