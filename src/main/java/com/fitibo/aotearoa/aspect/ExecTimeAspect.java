package com.fitibo.aotearoa.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

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
    public Object costTime(ProceedingJoinPoint joinPoint) {
        return logCostTime(joinPoint);
    }

    @Around("mapperTime()")
    public Object mapperCostTime(ProceedingJoinPoint joinPoint) {
        return logCostTime(joinPoint);
    }

    private Object logCostTime(ProceedingJoinPoint joinPoint) {
        Object output = null;
        try {
            long start = System.currentTimeMillis();
            output = joinPoint.proceed();
            long elapsedTime = System.currentTimeMillis() - start;
            String className = joinPoint.getTarget().getClass().getSimpleName();
            logger.info(String.format("method [%s.%s()] execution time:%sms", className, joinPoint.getSignature().getName(), elapsedTime));
        } catch (Throwable throwable) {
            logger.error("aop record method exec time error", throwable);
        }
        return output;
    }
}
