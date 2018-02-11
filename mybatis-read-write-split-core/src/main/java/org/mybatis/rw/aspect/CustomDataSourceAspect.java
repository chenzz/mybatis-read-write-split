package org.mybatis.rw.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.mybatis.rw.anno.DataSource;
import org.mybatis.rw.holder.DataSourceInfoHolder;
import org.mybatis.rw.util.AopUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;

import java.lang.reflect.Method;

@Aspect
public class CustomDataSourceAspect implements Ordered {

    public static final Logger LOGGER = LoggerFactory.getLogger(CustomDataSourceAspect.class);

    @Pointcut("@annotation(org.mybatis.rw.anno.DataSource)")
    public void dataSourceSourcePointCut() {
    }

    @Around("dataSourceSourcePointCut()")
    public Object proceed(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        try {
            LOGGER.debug("set database connection to multi read");

            Method method = AopUtils.getMethodFromTarget(proceedingJoinPoint);
            DataSource dataSource = method.getAnnotation(DataSource.class);

            DataSourceInfoHolder.setDataSourceType(dataSource.value());
            Object result = proceedingJoinPoint.proceed();
            return result;
        } finally {
            DataSourceInfoHolder.clearDataSourceType();
            LOGGER.debug("restore database connection");
        }
    }



    @Override
    public int getOrder() {
        return 0;
    }
}