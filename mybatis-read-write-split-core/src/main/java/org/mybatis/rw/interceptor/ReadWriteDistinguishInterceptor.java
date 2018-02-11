package org.mybatis.rw.interceptor;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.rw.constant.DbOperationType;
import org.mybatis.rw.holder.DataSourceInfoHolder;

import java.util.Properties;


@Intercepts({
        @Signature(
                type = Executor.class,
                method = "update",
                args = {MappedStatement.class, Object.class}
        ),
        @Signature(
                type = Executor.class,
                method = "query",
                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})

public class ReadWriteDistinguishInterceptor implements Interceptor {

    public Object intercept(Invocation invocation) throws Throwable {

        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        System.out.println(mappedStatement.getSqlCommandType());

        if (SqlCommandType.SELECT == mappedStatement.getSqlCommandType()) {
            DataSourceInfoHolder.setDbOperationType(DbOperationType.READ);
        } else {
            DataSourceInfoHolder.setDbOperationType(DbOperationType.UPDATE);
        }

        return invocation.proceed();
    }

    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    public void setProperties(Properties properties) {
        String dialect = properties.getProperty("dialect");
        System.out.println("mybatis intercept dialect:" + dialect);
    }
}
